package service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Data;
import model.Like;
import model.Url;
import org.bson.Document;
import utils.initializeDB;

import javax.print.Doc;
import java.util.ArrayList;

public class LikeService {

    protected final MongoDatabase database;
    protected final UrlService urlService;
    protected final CrawlerService crawlerService;

    public LikeService() {
        urlService = new UrlService();
        crawlerService = new CrawlerService();
        database = initializeDB.getDatabase();
    }

    public void addLike(Like like) {
        MongoCollection<Like> collection = database.getCollection("like", Like.class);

        collection.insertOne(like);
    }

    public void updateIsNew() {

    }

    public ArrayList<Like> getLikesAsList() {
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

    public void addLikesToDatabase() {
        ArrayList<String> urls = urlService.getUrlsAsList();

        for (String url : urls) {
            Like like = crawlerService.urlToLikeCollection(url);
            addLike(like);
        }
    }
}
