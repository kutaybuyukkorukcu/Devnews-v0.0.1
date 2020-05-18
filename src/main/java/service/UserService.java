package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import
import model.Data;
import model.User;
import org.bson.Document;
import utils.StandardResponse;
import utils.StatusResponse;

import javax.print.Doc;
import java.util.ArrayList;

public class UserService {

    public StandardResponse addUser(User user, MongoDatabase database) {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        Document queryByUsername = new Document("username", user.getUsername());

        Boolean userExist = collection.find(queryByUsername).equals(user);

        if (userExist) {
            new StandardResponse(StatusResponse.ERROR, "User exists!");
        }

        collection.insertOne(user);

        new StandardResponse(StatusResponse.SUCCESS, "User created!");
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
}