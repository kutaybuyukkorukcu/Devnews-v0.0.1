package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Url;
import org.bson.Document;

import java.util.ArrayList;

public class UrlService {

    public void addUrl(Url url, MongoDatabase database) {
        MongoCollection<Url> collection = database.getCollection("url", Url.class);

        collection.insertOne(url);
    }

    public ArrayList<String> getUrlsAsList(MongoDatabase database) {
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
