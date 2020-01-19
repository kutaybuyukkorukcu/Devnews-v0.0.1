package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Data;
import model.Like;
import model.Link;

import java.util.ArrayList;

public class LikeService {

    public void addLike(Like like, MongoDatabase database) {
        MongoCollection<Like> collection = database.getCollection("like", Like.class);

        collection.insertOne(like);
    }

    public ArrayList<Like> getLikes(MongoDatabase database) {
        MongoCollection<Like> collection = database.getCollection("like", Like.class);

        ArrayList<Like> list = new ArrayList<Like>();

        try(MongoCursor<Like> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Like link = cursor.next();
                list.add(link);
            }
        }

        return list;
    }
}
