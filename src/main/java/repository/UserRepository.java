package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import domain.Data;
import domain.User;
import org.bson.Document;
import utils.initializeDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<User> {

    protected final MongoDatabase database;

    public UserRepository() {
        database = initializeDB.getDatabase();
    }

    @Override
    public void add(User user) {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        collection.insertOne(user);
    }

    @Override
    public void update(User user) {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        Document queryByUsername = new Document("username", user.getUsername());

        // TODO : ileride sadece username degil baska seyleri de updatelemek isteyebilir. Password eklenecek mesela.
        Document updatedUser = new Document("username", user.getUsername());

        // TODO : Var olan kayidin icerdigi datayi degistirip tekrar insertOne ile update edebiliyor muyum?
        collection.updateOne(queryByUsername, updatedUser);
    }

    @Override
    public User findById(int id) {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        Document queryById = new Document("id", id);
        Document queryByIsActive = new Document("isActive", true);

        User user = collection.find(queryById).filter(queryByIsActive).first();

        return user;
    }

    @Override
    public List<User> findAll() {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        MongoCursor<User> cursor = collection.find().iterator();
        List<User> userList = new ArrayList<>();

        while (cursor.hasNext()) {
            User user = cursor.next();
            userList.add(user);
        }

        return userList;
    }

    public User findByUsername(String username) {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        Document queryByUsername = new Document("username", username);
        Document queryByIsActive = new Document("isActive", true);

        User user = collection.find(queryByUsername).filter(queryByIsActive).first();

        return user;
    }

    public int getNextIdSequence() {
        MongoCollection<User> collection = database.getCollection("user", User.class);

        User user = collection.find().sort(new Document("_id", -1)).first();

        return user.getId() + 1;
    }
}
