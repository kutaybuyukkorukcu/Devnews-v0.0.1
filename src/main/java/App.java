import com.mongodb.*;
import com.mongodb.client.*;
import utils.Validator;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.Consumer;
import org.bson.Document;

public class App {

    public static void main(String[] args) {

        Data data = new Data();

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("infoq");

        MongoCursor<String> wowe = mongoClient.listDatabaseNames().iterator();

        boolean flag = false;

        while (wowe.hasNext()) {
            if (wowe.toString() == "infoq") {
                flag = true;
            }
        }
        System.out.println(flag);

        if (!flag) {
            database.createCollection("data");
        }

        MongoCollection collection = database.getCollection("data");

        Document document = new Document();

        BasicDBObject query = new BasicDBObject();
//        findAndModify ile counter adinda collection olusturup ordan tek tek artan bir ID elde edecegim.
//        document.put("articleID", 1);
//        document.put("title", data.getTitle());
//        document.put("mainTopic", data.getMainTopic());
//        document.put("author", data.getAuthor());
//        document.put("relatedTopics", data.getRelatedTopics());
//        document.put("articleLink", data.getArticleLink());

        document.put("articleID", 1);
        document.put("title", "Yo");
        document.put("mainTopic", "development");
        document.put("author", "kutay");
        document.put("relatedTopics", "AI|DEVELOPMENT|MOBILE");
        document.put("articleLink", "www.google.com");

        query.put("title", "Yo");

        MongoCursor cursor = collection.find(query).iterator();

        while (cursor.hasNext()) {
            System.out.println(cursor.toString());
        }
        //        collection.insertOne(document);

    }

}