package service;

import com.mongodb.Mongo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import db.DBDriver;
import model.Article;
import model.Data;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;

public class DataService {

    private DBDriver dbDriver;

    MongoDatabase mongoDatabase = dbDriver.getDatabaseInstance();

    public DataService(DBDriver dbDriver) {
        this.dbDriver = dbDriver;
    }

    public void addData(Data data){

        MongoCollection<Data> collection = mongoDatabase.getCollection("data", Data.class);

        collection.insertOne(data);
    }

    public ArrayList<Data> getDatas() {

        MongoCollection<Data> collection = mongoDatabase.getCollection("data", Data.class);

        ArrayList<Data> list = new ArrayList<Data>();

        try(MongoCursor<Data> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Data data = cursor.next();
                list.add(data);
            }
        }

        return list;
    }

    public String sendRecommendations(ArrayList<Article> articles) {

        Iterator<Article> iter = articles.iterator();
        StringBuilder sb = new StringBuilder();

        while(iter.hasNext()) {
            int articleID = iter.next().getArticleID();

            MongoCollection<Data> collection = mongoDatabase.getCollection("data", Data.class);

            Document queryFilter =  new Document("articleID", articleID);

            FindIterable<Data> result = collection.find(queryFilter).limit(1);

            Data data = result.first();
            String html = createMail(data);
            sb.append(html);
            sb.append("\n");
        }

        return sb.toString();
    }

    public boolean dataExist(Data data) {

        MongoCollection<Data> collection = mongoDatabase.getCollection("data", Data.class);

        Document queryFilter =  new Document("title", data.getTitle());

        FindIterable result = collection.find(queryFilter).limit(1);

        return result.first() == null ? false : true;
    }

    public String createMail(Data data) {

        // verilerden mail formati olusturmasini bekleriz.
        String mainTopic = data.getMainTopic();
        String title = data.getTitle();
        String url = data.getArticleLink();

        String html = "<h2> " + mainTopic + " </h2>" + "\n"
                + "<h4> " + title + " </h4>" + "\n"
                + "<h5> " + url + " </h5>";

        return html;
    }
}
