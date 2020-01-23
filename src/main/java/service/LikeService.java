package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Like;
import org.bson.Document;

import java.util.ArrayList;

public class LikeService {

    public void addLike(Like like, MongoDatabase database) {
        MongoCollection<Like> collection = database.getCollection("like", Like.class);

        collection.insertOne(like);
    }

    public ArrayList<Like> getLikesAsList(MongoDatabase database) {
        MongoCollection<Like> collection = database.getCollection("like", Like.class);

        Document queryFilter =  new Document("isNew", 1);
        ArrayList<Like> list = new ArrayList<Like>();

        try(MongoCursor<Like> cursor = collection.find(queryFilter).iterator()) {
            while (cursor.hasNext()) {
                Like like = cursor.next();
                list.add(like);
            }
        }

        return list;
    }

    public void updateIsNew() {

    }
}
