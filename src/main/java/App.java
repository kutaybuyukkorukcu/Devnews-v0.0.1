import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.bson.Document;

public class App {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase("infoq").withCodecRegistry(pojoCodecRegistry);

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

        counterValue(database, flag);
//         Collection data icin ayri bir fonksiyon olucak.
//        if (!flag) {
//            database.createCollection("data", DataDTO.class);
//        }

//        MongoCollection<DataDTO> collection = database.getCollection("data", DataDTO.class);
//
//        Document document = new Document();
//        Document query = new Document();
//
//        document.put("articleID", 1);
//        document.put("title", "Yo");
//        document.put("mainTopic", "development");
//        document.put("author", "kutay");
//        document.put("relatedTopics", "AI|DEVELOPMENT|MOBILE");
//        document.put("articleLink", "www.google.com");
//
//        query.put("title", "Yo");
//
//        MongoCursor<DataDTO> cursor = collection.find(query).iterator();
//
//        //        collection.insertOne(document);
//
//        try {
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next().toJson());
//            }
//        }finally {
//            mongoClient.close();
//        }
    }

    public static void crawlingDataToPOJO(MongoDatabase database, boolean flag) {

        if (!flag) {
            database.createCollection("data");
            MongoCollection<Data> _collection = database.getCollection("data", Data.class);
            Data data = new Data();
            // -- Eklemeler yapacagim.
            // Create a queryID , articleID -> queryID = "const" value for querying, articleID = "int" i will inc 1 everytime
            _collection.insertOne(data);
        }
    }
    /*
    // Create's a collection named counter if there's none.
    // Increments counterValue by 1 and returns it.
    // Purpose of this collection : Defines an articleID for each article.
     */
    public static int counterValue(MongoDatabase database, boolean flag) {

        if (!flag) {
            database.createCollection("counter");
            MongoCollection<Counter> _collection = database.getCollection("counter", Counter.class);
            Counter counter = new Counter();
            // Create a queryID , articleID -> queryID = "const" value for querying, articleID = "int" i will inc 1 everytime
            counter.setCounterName("counterName");
            counter.setCounterValue(1);
            _collection.insertOne(counter);
        }

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