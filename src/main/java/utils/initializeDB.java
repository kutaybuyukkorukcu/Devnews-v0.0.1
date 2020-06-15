package utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import domain.Counter;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class initializeDB {

    final static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    final static CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    final static MongoDatabase database = mongoClient.getDatabase("infoq").withCodecRegistry(pojoCodecRegistry);

    public static MongoDatabase getDatabase() {
        return database;
    }
// Collection exists template.collectionExists
    public static void createCounter() {
        if (!checkDB()) {
            database.createCollection("counter");
            MongoCollection<Counter> collection = database.getCollection("counter", Counter.class);
            Counter counter = new Counter();
            // Create a queryID , articleID -> queryID = "const" value for querying, articleID = "int" i will inc 1 everytime
            counter.setCounterName("articleID");
            counter.setCounterValue(0);
            collection.insertOne(counter);
        }
    }

    public static void createArticle() {
        if (!checkDB()) {
            database.createCollection("article");
//    TODO :        MongoCollection<Article> _collection = database.getCollection("data", Article.class);
//            Article data = new Article();
//            // -- Eklemeler yapacagim.
//            // Create a queryID , articleID -> queryID = "const" value for querying, articleID = "int" i will inc 1 everytime
//            _collection.insertOne(data);
        }
    }

    public static void createUrl() {
        if (!checkDB()) {
            database.createCollection("url");
        }
    }

    public static void createLike() {
        if (!checkDB()) {
            database.createCollection("like");
        }
    }

    public static void createUser() {
        if (!checkDB()) {
            database.createCollection("user");
        }
    }

    public static boolean checkDB() {
        MongoCursor<String> databases = mongoClient.listDatabaseNames().iterator();

        // Making sure code won't try to create db once again.
        boolean flag = false;

        while (databases.hasNext()) {
            if (databases.next().equals("infoq")) {
                flag = true;
                return flag;
            }
        }
        return flag;
    }
}
