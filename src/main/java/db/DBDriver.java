package db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Counter;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class DBDriver {

    MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    MongoDatabase mongoDatabase = mongoClient.getDatabase("infoq").withCodecRegistry(pojoCodecRegistry);

    boolean flag = checkDB();

    public DBDriver() {
        createCounter();
        createData();
        createLike();
        createUrl();
    }

    public void createCounter() {

        if (!flag) {
            mongoDatabase.createCollection("counter");
            MongoCollection<Counter> collection = mongoDatabase.getCollection("counter", Counter.class);
            Counter counter = new Counter();
            // Create a queryID , articleID -> queryID = "const" value for querying, articleID = "int" i will inc 1 everytime
            counter.setCounterName("articleID");
            counter.setCounterValue(0);
            collection.insertOne(counter);
        }
    }

    public void createData() {

        if (!flag) {
            mongoDatabase.createCollection("data");
        }
    }

    public void createUrl() {

        if (!flag) {
            mongoDatabase.createCollection("url");
        }
    }

    public void createLike() {

        if (!flag) {
            mongoDatabase.createCollection("like");
        }
    }

    public boolean checkDB() {

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

    public MongoClient getClientInstance() {

        return  mongoClient;
    }

    public MongoDatabase getDatabaseInstance() {

        return mongoDatabase;
    }
}
