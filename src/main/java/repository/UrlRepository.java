package repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import domain.Like;
import domain.Url;
import org.bson.Document;
import utils.initializeDB;

import java.util.ArrayList;
import java.util.List;

public class UrlRepository implements IRepository<Url>{

    protected final MongoDatabase database;

    public UrlRepository() {
        database = initializeDB.getDatabase();
    }

    @Override
    public void add(Url url) {
        MongoCollection<Url> collection = database.getCollection("url", Url.class);

        collection.insertOne(url);
    }

    @Override
    public void update(Url url) {

    }

    @Override
    public Url findById(int id) {
        return null;
    }

    @Override
    public List<Url> findAll() {
        MongoCollection<Url> collection = database.getCollection("url", Url.class);

        MongoCursor<Url> cursor = collection.find().iterator();
        List<Url> urlList = new ArrayList<>();

        while (cursor.hasNext()) {
            Url url = cursor.next();
            urlList.add(url);
        }

        return urlList;
    }

    public List<Url> findAllByIsNew() {
        MongoCollection<Url> collection = database.getCollection("url", Url.class);

        Document queryFilter = new Document("isNew", true);

        MongoCursor<Url> cursor = collection.find(queryFilter).iterator();
        List<Url> urlList = new ArrayList<>();

        while (cursor.hasNext()) {
            Url url = cursor.next();
            urlList.add(url);
        }

        return urlList;
    }
}
