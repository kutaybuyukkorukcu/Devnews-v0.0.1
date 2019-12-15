import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import utils.Validator;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.Consumer;
import org.bson.Document;

public class App {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("infoq");

        MongoCursor<String> databases = mongoClient.listDatabaseNames().iterator();

        // Checking database named infoq.
        // By checking that i'm making sure the code doesn't try to create data collection once again.
        boolean flag = false;

        while (databases.hasNext()) {
            if (databases.next().equals("infoq")) {
                flag = true;
                break;
            }
        }

        // Collection data icin ayri bir fonksiyon olucak.
        if (!flag) {
            database.createCollection("data");
        }

        MongoCollection<DataDTO> collection = database.getCollection("data", DataDTO.class);

        Document document = new Document();
        Document query = new Document();

//        findAndModify ile counter adinda collection olusturup ordan tek tek artan bir ID elde edecegim.

        document.put("articleID", 1);
        document.put("title", "Yo");
        document.put("mainTopic", "development");
        document.put("author", "kutay");
        document.put("relatedTopics", "AI|DEVELOPMENT|MOBILE");
        document.put("articleLink", "www.google.com");

        query.put("title", "Yo");

        MongoCursor<DataDTO> cursor = collection.find(query).iterator();

        //        collection.insertOne(document);

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }finally {
            mongoClient.close();
        }
    }

    /*
    // Create's a collection named counter if there's none.
    // Increments counterValue by 1 and returns it.
    // Purpose of this collection : Defines an articleID for each article.
     */
    public void counterValue(MongoDatabase database, boolean flag) {

        if (!flag) {
            database.createCollection("counter");
            Document document = new Document();
            // Create a queryID , articleID -> queryID = "const" value for querying, articleID = "int" i will inc 1 everytime
            document.put("counterName", "articleID");
            document.put("counterValue", 1);
        }

        MongoCollection collection = database.getCollection("counter");

        Document query = new Document("counterName", "articleID");
        Document update = new Document();
        update.put("$inc", update.put("counterValue", 1));

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);

        Object doc = collection.findOneAndUpdate(query, update, options);
        doc.toString();
    }
}