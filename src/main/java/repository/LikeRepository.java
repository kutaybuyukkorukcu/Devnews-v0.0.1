package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import domain.Like;
import org.bson.Document;
import utils.initializeDB;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

public class LikeRepository implements IRepository<Like>{

    protected final MongoDatabase database;

    public LikeRepository() {
        database = initializeDB.getDatabase();
    }

    @Override
    public void add(Like like) {
        MongoCollection<Like> collection = database.getCollection("like", Like.class);

        collection.insertOne(like);
    }

    @Override
    public void update(Like like) {

    }

    @Override
    public Like findById(int id) {
        return null;
    }

    @Override
    public List<Like> findAll() {
        MongoCollection<Like> collection = database.getCollection("like", Like.class);

        MongoCursor<Like> cursor = collection.find().iterator();
        List<Like> likeList = new ArrayList<>();

        while (cursor.hasNext()) {
            Like like = cursor.next();
            likeList.add(like);
        }

        return likeList;
    }

    public List<Like> findAllByIsNew() {
        MongoCollection<Like> collection = database.getCollection("like", Like.class);

        Document queryFilter = new Document("isNew", true);

        MongoCursor<Like> cursor = collection.find(queryFilter).iterator();
        List<Like> likeList = new ArrayList<>();

        while (cursor.hasNext()) {
            Like like = cursor.next();
            likeList.add(like);
        }

        return likeList;
    }
}
