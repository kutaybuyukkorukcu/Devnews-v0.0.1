import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.sun.tools.javac.Main;
import model.*;
import db.initializeDB;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.bson.Document;
import service.ArticleService;
import service.DataService;
import service.LikeService;
import service.UrlService;
import utils.initializeLists;

import java.util.ArrayList;
import java.util.Iterator;

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

        get("/likes", (request, response) -> {
            // Like collectionundaki linkleri ceksin.
            // cekilen linkleri crawllasin
            // crawldan main_topic ve title donup hem dosyaya yazsin hem de database Like collectionuna
            response.type("application/json");

            ArrayList<String> urls = urlService.getUrlsAsList(database);

            for (String url : urls) {
                Like like = crawler.urlToLikeCollection(url, database);
                like.toString();
                crawler.writeLikes(like);
                likeService.addLike(like, database);
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(likeService.getLikesAsList(database))));
        });

        get("/son", (request, response) -> {
            ArrayList<Like> likes = likeService.getLikesAsList(database);
            Iterator<Like> iter = likes.iterator();

            while(iter.hasNext()) {
                Like like = iter.next();
                JsonObject jsonObject = articleService.getRecommendations(like.getTitle());

                // == instead of equals() maybe?
                if (like.getMainTopic().equals(MainTopics.DEVELOPMENT.getMainTopic())) {
                    articleService.JSONArrayToList(jsonObject, initializeLists.development);
                } else if (like.getMainTopic().equals(MainTopics.ARCHITECTURE.getMainTopic())) {
                    articleService.JSONArrayToList(jsonObject, initializeLists.architecture);
                } else if (like.getMainTopic().equals(MainTopics.AI.getMainTopic())) {
                    articleService.JSONArrayToList(jsonObject, initializeLists.ai);
                } else if (like.getMainTopic().equals(MainTopics.CULTURE.getMainTopic())) {
                    articleService.JSONArrayToList(jsonObject, initializeLists.culture);
                } else if (like.getMainTopic().equals(MainTopics.DEVOPS.getMainTopic())) {
                    articleService.JSONArrayToList(jsonObject, initializeLists.devops);
                } else {
                    articleService.JSONArrayToList(jsonObject, new ArrayList<Article>());
                }
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(likeService.getLikesAsList(database))));
        });

        get("/son1", (request, response) -> {

            initializeLists.development = articleService.returnRecommendations(initializeLists.development);
            initializeLists.architecture = articleService.returnRecommendations(initializeLists.architecture);
            initializeLists.ai = articleService.returnRecommendations(initializeLists.ai);
            initializeLists.culture = articleService.returnRecommendations(initializeLists.culture);
            initializeLists.devops = articleService.returnRecommendations(initializeLists.devops);

            articleService.cekVeYaz(initializeLists.development,database);
            articleService.cekVeYaz(initializeLists.architecture,database);
            articleService.cekVeYaz(initializeLists.ai,database);
            articleService.cekVeYaz(initializeLists.culture,database);
            articleService.cekVeYaz(initializeLists.devops,database);

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(likeService.getLikesAsList(database))));
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

}