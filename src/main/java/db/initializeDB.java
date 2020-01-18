package db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Counter;
import model.Data;
import model.Link;

public class initializeDB {

    public static void createCounter(MongoDatabase database, boolean flag) {
        if (!flag) {
            database.createCollection("counter");
            MongoCollection<Counter> _collection = database.getCollection("counter", Counter.class);
            Counter counter = new Counter();
            // Create a queryID , articleID -> queryID = "const" value for querying, articleID = "int" i will inc 1 everytime
            counter.setCounterName("articleID");
            counter.setCounterValue(0);
            _collection.insertOne(counter);
        }
    }

    public static void createData(MongoDatabase database, boolean flag) {
        if (!flag) {
            database.createCollection("data");
//            MongoCollection<Data> _collection = database.getCollection("data", Data.class);
//            Data data = new Data();
//            // -- Eklemeler yapacagim.
//            // Create a queryID , articleID -> queryID = "const" value for querying, articleID = "int" i will inc 1 everytime
//            _collection.insertOne(data);
        }
    }

    public static void createLink(MongoDatabase database, boolean flag) {
        if (!flag) {
            database.createCollection("link");
//            MongoCollection<Link> _collection = database.getCollection("link", Link.class);
        }
    }

    public static boolean checkDB(MongoClient mongoClient) {
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
