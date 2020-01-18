package service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Data;
import model.Link;
import org.bson.Document;

import java.util.ArrayList;

public class LinkService {

    public void addLink(Link link, MongoDatabase database) {
        MongoCollection<Link> collection = database.getCollection("link", Link.class);

//        Document query = new Document();

        collection.insertOne(link);
    }

    public ArrayList<String> getLinks(MongoDatabase database) {
        MongoCollection<Link> collection = database.getCollection("link", Link.class);

        Document queryFilter =  new Document("isNew", 1);

        ArrayList<String> urls = new ArrayList<String>();

        try(MongoCursor<Link> cursor = collection.find(queryFilter).iterator()) {
            while (cursor.hasNext()) {
                Link link = cursor.next();
                urls.add(link.getLink());
            }
        }

        return urls;
    }
}
