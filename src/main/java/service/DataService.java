package service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Article;
import model.Data;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;

import static com.mongodb.client.model.Filters.eq;

public class DataService {

    public void addData(Data data, MongoDatabase database){
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        collection.insertOne(data);
    }

    public ArrayList<Data> getDatas(MongoDatabase database) {
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        ArrayList<Data> list = new ArrayList<Data>();

        try(MongoCursor<Data> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Data data = cursor.next();
                list.add(data);
            }
        }

        return list;
    }

    public void sendRecommendations(ArrayList<Article> articles, MongoDatabase database) {

        Iterator<Article> iter = articles.iterator();

        while(iter.hasNext()) {
            int articleID = iter.next().getArticleID();

            MongoCollection<Data> collection = database.getCollection("data", Data.class);

            Document queryFilter =  new Document("articleID", articleID);

            FindIterable<Data> result = collection.find(queryFilter).limit(1);

            Data data = result.first();
            Path path = Paths.get("src/main/resources/recommendations.txt");

            StringBuilder sb = new StringBuilder();
            sb.append(data.getMainTopic() + "\t");
            sb.append(data.getTitle() + "\t");
            sb.append(data.getArticleLink());

            // StandardOpenOption'a CREATE_NEW ekle.
            // Set kontrolu yapilsin. Ayni articleID'ye sahipler yazilmasin.
            try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
                writer.newLine();
                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean dataExist(Data data, MongoDatabase database) {
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        Document queryFilter =  new Document("title", data.getTitle());

        FindIterable result = collection.find(queryFilter).limit(1);

        return result.first() == null ? false : true;
    }
}
