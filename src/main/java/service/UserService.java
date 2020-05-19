package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import model.User;
import org.bson.Document;
import utils.StandardResponse;
import utils.StatusResponse;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class UserService {

    public void createOrUpdateUser(User user, MongoDatabase database) {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        Document queryByUsername = new Document("username", user.getUsername());

        User userExist = collection.find(queryByUsername).first();

        // temporary code.
        if (userExist == null) {
            collection.insertOne(user);
        }

        // TODO : Var olan kayidin icerdigi datayi degistirip tekrar insertOne ile update edebiliyor muyum?
        collection.insertOne(user);
    }

    public ArrayList<User> getUsers(MongoDatabase database) {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        ArrayList<User> list = new ArrayList<User>();

        try(MongoCursor<User> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                User user = cursor.next();
                list.add(user);
            }
        }

        return list;
    }

    public Optional<User> findUser(int id, MongoDatabase database) {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        Document queryById = new Document("articleId", id);
        Document queryByIsActive = new Document("isActive", true);

        User user = collection.find(queryById).filter(queryByIsActive).first();

        return Optional.ofNullable(user);
    }
}