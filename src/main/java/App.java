import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import model.*;
import db.initializeDB;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.bson.Document;
import service.ArticleService;
import service.DataService;
import service.LikeService;
import service.UrlService;
import utils.Mail;
import utils.initializeLists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static spark.Spark.post;
import static spark.Spark.get;

public class App {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase("infoq").withCodecRegistry(pojoCodecRegistry);

        boolean flag = initializeDB.checkDB(mongoClient);

        initializeDB.createCounter(database, flag);
        initializeDB.createData(database, flag);
        initializeDB.createUrl(database, flag);
        initializeDB.createLike(database, flag);

        initializeLists.generateLists();

        final LikeService likeService = new LikeService();
        final UrlService urlService = new UrlService();
        final DataService dataService = new DataService();
        final ArticleService articleService = new ArticleService();
        final Crawler crawler = new Crawler();

        // Get datas stored in Data collection
        get("/datas", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(dataService.getDatas(database))));
        });

        // Reads each url from text file and then inserts the urls into the Url collection
        get("/urls", (request, response) -> {
            response.type("application/json");

            ArrayList<String> list = crawler.fileToList();

            for (String url : list) {
                Url link = crawler.urlToUrlCollection(url);
                urlService.addUrls(link, database);
            }

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        post("/urls", (request, response) -> {
            response.type("application/json");

            Url url = new Gson().fromJson(request.body(), Url.class);
            urlService.addUrls(url, database);

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        // Reads each url from database, crawls it and then
        // -> Appends each formatted data into articles.csv file
        // -> Inserts each formatted data into Data collection
        // Use /crawl for generating .csv file which contains all articles
        get("/crawl", (request, response) -> {
            response.type("application/json");

            ArrayList<String> urls = urlService.getUrlsAsList(database);

            for (String url : urls) {
                int articleID = counterValue(database);
                Data data = crawler.urlToData(url, articleID);
                crawler.writeDatas(data);
                dataService.addData(data, database);
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(dataService.getDatas(database))));
        });

        get("/recommend", (request, response) -> {

            response.type("application/json");

            getUrls(urlService, crawler, likeService, database);
            getLikesAndRecommend(likeService, articleService, database);
            getRecommends(articleService, dataService, database);

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(urlService.getUrlsAsList(database))));
        });
    }

    /*
    // Create's a collection named counter if there's none.
    // Increments counterValue by 1 and returns it.
    // Purpose of this collection : Defines an articleID for each article.
     */
    public static int counterValue(MongoDatabase database) {
        MongoCollection<Counter> collection = database.getCollection("counter", Counter.class);

        Document query = new Document("counterName", "articleID");
        Document update = new Document();
        Document inside = new Document();
        inside.put("counterValue", 1);
        update.put("$inc", inside);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);

        Counter doc = collection.findOneAndUpdate(query, update, options);
        return doc.getCounterValue();
    }

    public static void getUrls(UrlService urlService, Crawler crawler, LikeService likeService, MongoDatabase database) {
        ArrayList<String> urls = urlService.getUrlsAsList(database);

        for (String url : urls) {
            Like like = crawler.urlToLikeCollection(url, database);
            like.toString();
            crawler.writeLikes(like);
            likeService.addLike(like, database);
        }
    }

    public static void getLikesAndRecommend(LikeService likeService, ArticleService articleService, MongoDatabase database) {
        ArrayList<Like> likes = likeService.getLikesAsList(database);
        Iterator<Like> iter = likes.iterator();

        while(iter.hasNext()) {
            Like like = iter.next();
            System.out.println(like.toString());
            JsonObject jsonObject = articleService.getRecommendations(like.getTitle());

            // == instead of equals() maybe?
            if (like.getMainTopic().equals(MainTopics.DEVELOPMENT.getMainTopic())) {
                articleService.JsonObjectToList(jsonObject, initializeLists.development);
            } else if (like.getMainTopic().equals(MainTopics.ARCHITECTURE.getMainTopic())) {
                articleService.JsonObjectToList(jsonObject, initializeLists.architecture);
            } else if (like.getMainTopic().equals(MainTopics.AI.getMainTopic())) {
                articleService.JsonObjectToList(jsonObject, initializeLists.ai);
            } else if (like.getMainTopic().equals(MainTopics.CULTURE.getMainTopic())) {
                articleService.JsonObjectToList(jsonObject, initializeLists.culture);
            } else if (like.getMainTopic().equals(MainTopics.DEVOPS.getMainTopic())) {
                articleService.JsonObjectToList(jsonObject, initializeLists.devops);
            } else {
                articleService.JsonObjectToList(jsonObject, new ArrayList<Article>());
            }
        }
    }

    public static void getRecommends(ArticleService articleService, DataService dataService, MongoDatabase database) {
        initializeLists.development = articleService.returnRecommendations(initializeLists.development);
        initializeLists.architecture = articleService.returnRecommendations(initializeLists.architecture);
        initializeLists.ai = articleService.returnRecommendations(initializeLists.ai);
        initializeLists.culture = articleService.returnRecommendations(initializeLists.culture);
        initializeLists.devops = articleService.returnRecommendations(initializeLists.devops);

        StringBuilder sb = new StringBuilder();

        sb.append(dataService.sendRecommendations(initializeLists.development,database));
        sb.append(dataService.sendRecommendations(initializeLists.architecture,database));
        sb.append(dataService.sendRecommendations(initializeLists.ai,database));
        sb.append(dataService.sendRecommendations(initializeLists.culture,database));
        sb.append(dataService.sendRecommendations(initializeLists.devops,database));

        Mail mail = new Mail();
        mail.sendMail(sb.toString());
    }
}