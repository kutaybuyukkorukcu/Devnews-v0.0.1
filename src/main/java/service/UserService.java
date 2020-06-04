package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import domain.User;
import org.bson.Document;
import repository.UserRepository;
import utils.initializeDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserService {

    protected final UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    public void addOrUpdateUser(User user) {

        int incrementedId = userRepository.getNextIdSequence();
        user.setId(incrementedId);

        User userExist = userRepository.findByUsername(user.getUsername());

        // temporary code.
        if (userExist == null) {
            userRepository.add(user);
        } else {
            userRepository.update(user);
        }
    }

    public List<User> getUsers() {
        List<User> userList = userRepository.findAll();

        if (userList == null) {
            return Collections.emptyList();
        }

        return userList;
    }

    public Optional<User> findUserByUsername(String username) {
// TODO : remove user collection from infoq database and re-create it. Then check the name of the user field active or isActive
// TODO : and rename the active/isActive field based on the name from db collection.
        User user = userRepository.findByUsername(username);

        return Optional.ofNullable(user);
    }

    public Optional<User> findUserById(int id) {
        User user = userRepository.findById(id);

        return Optional.ofNullable(user);
    }
}