package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Data;
import model.Link;
import org.bson.Document;

public class LinkService {

    public void addLink(Link link, MongoDatabase database) {
        MongoCollection<Link> collection = database.getCollection("link", Link.class);

//        Document query = new Document();

        collection.insertOne(link);
    }
}
