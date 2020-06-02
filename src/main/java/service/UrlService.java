package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Url;
import org.bson.Document;
import utils.initializeDB;

import java.util.ArrayList;

public class UrlService {

    protected final MongoDatabase database;

    public UrlService() {
        database = initializeDB.getDatabase();
    }

    public void addUrl(Url url) {
        MongoCollection<Url> collection = database.getCollection("url", Url.class);

        collection.insertOne(url);
    }

    public ArrayList<String> getUrlsAsList() {
        MongoCollection<Url> collection = database.getCollection("url", Url.class);

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
