import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import model.Counter;
import db.initializeDB;
import model.Data;
import model.Like;
import model.Link;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.bson.Document;
import service.DataService;
import service.LikeService;
import service.LinkService;

import java.util.ArrayList;

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
        initializeDB.createLink(database, flag);
        initializeDB.createLike(database, flag);


        final LikeService likeService = new LikeService();
        final LinkService linkService = new LinkService();
        final DataService dataService = new DataService();
        final Crawler crawler = new Crawler();

        get("/datas", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(dataService.getData(database))));
        });

        post("/links", (request, response) -> {
            response.type("application/json");

            Link link = new Gson().fromJson(request.body(), Link.class);
            linkService.addLink(link, database);

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        get("/tolink", (request, response) -> {
            response.type("application/json");
            ArrayList<String> list = crawler.txtToLink1();

            for (String url : list) {
                Link link = crawler.txtToLink2(url);
                linkService.addLink(link, database);
            }

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        get("/crawl", (request, response) -> {
            response.type("application/json");

            ArrayList<String> urls = linkService.getLinks(database);

            for (String url : urls) {
                int articleID = counterValue(database);
                Data data = crawler.castToPojo(url, articleID);
                crawler.CSVWriter(data);
                dataService.addData(data, database);
            }

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(dataService.getData(database))));
        });

        get("/likes", (request, response) -> {
            // mongodb'deki linkleri ceksin.
            // cekilen linkleri crawllasin
            // crawldan main_topic ve title donup hem dosyaya yazsin hem de database Like collectionuna
            response.type("application/json");

            ArrayList<String> urls = linkService.getLinks(database);

            for (String url : urls) {

            }

            return 0;
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