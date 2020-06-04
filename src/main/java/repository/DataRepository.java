package repository;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import domain.Data;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import utils.initializeDB;

public class DataRepository implements IRepository<Data>{

    protected final MongoDatabase database;

    public DataRepository() {
        database = initializeDB.getDatabase();
    }

    @Override
    public void add(Data data) {
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        collection.insertOne(data);
    }

    @Override
    public void update(Data data) {

    }

    @Override
    public Data findById(int id) {
        return null;
    }

    @Override
    public List<Data> findAll() {
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        MongoCursor<Data> cursor = collection.find().iterator();
        List<Data> dataList = new ArrayList<>();

        while (cursor.hasNext()) {
            Data data = cursor.next();
            dataList.add(data);
        }

        return dataList;
    }

    public Data findByTitle(String title) {
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        Document queryByTitle = new Document("title", title);

        Data data = collection.find(queryByTitle).first();

        return data;
    }

    public Data findByArticleId(int articleId) {
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        Document queryByArticleId = new Document("articleId", articleId);

        Data data = collection.find(queryByArticleId).first();

        return data;
    }
}
