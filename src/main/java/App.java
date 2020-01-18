import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import model.Counter;
import model.Data;
import db.initializeDB;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.bson.Document;

public class App {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase("infoq").withCodecRegistry(pojoCodecRegistry);

        boolean flag = initializeDB.checkDB(mongoClient);

        initializeDB.createCounter(database, flag);
        initializeDB.createData(database, flag);
        initializeDB.createLink(database, flag);

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