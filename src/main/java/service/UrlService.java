package service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import db.DBDriver;
import model.Url;
import org.bson.Document;

import java.util.ArrayList;

public class UrlService {

    private DBDriver dbDriver;

    public UrlService(DBDriver dbDriver) {
        this.dbDriver = dbDriver;
    }

    MongoDatabase mongoDatabase = dbDriver.getDatabaseInstance();

    public void addUrls(Url url) {

        MongoCollection<Url> collection = mongoDatabase.getCollection("url", Url.class);

        collection.insertOne(url);
    }

    public ArrayList<String> getUrlsAsList() {

        MongoCollection<Url> collection = mongoDatabase.getCollection("url", Url.class);

        Document queryFilter =  new Document("isNew", 1);

        ArrayList<String> urls = new ArrayList<String>();

        try(MongoCursor<Url> cursor = collection.find(queryFilter).iterator()) {
            while (cursor.hasNext()) {
                Url url = cursor.next();
                urls.add(url.getUrl());
            }
        }

        return urls;
    }
}
