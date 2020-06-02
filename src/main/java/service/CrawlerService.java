package service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import model.Counter;
import model.Data;
import model.Like;
import model.Url;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import helper.Validator;
import utils.initializeDB;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.stream.Stream;

public class CrawlerService {

    protected final Validator validator;
    protected final MongoDatabase database;

    public CrawlerService() {
        validator = new Validator();
        database = initializeDB.getDatabase();
    }

    public Data urlToData(String url) {

        Data data = new Data();

        StringBuilder topics = new StringBuilder();

        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO : logging and handling
            e.printStackTrace();
        }

        String topic = doc.select("div.article__category > a[title]").text();
        String mainTopic = validator.validate(topic);

        String title = doc.select("div.actions__left > h1").text();
        String author = doc.select("span.author__name > a").text();

        Elements ul = doc.select("div.topics > ul");
        Elements li = ul.select("li");
        for(Element item : li) {
            topics.append(item.text() + "|");
        }

        String relatedTopics = validator.removeLastChar(topics.toString());

        int articleID = getNextArticleIdSequence();

        // articleLink yine DB'den geliyor.
        data.setArticleID(articleID);
        data.setArticleLink(url);
        data.setAuthor(author);
        data.setTitle(title);
        data.setMainTopic(mainTopic);
        data.setRelatedTopics(relatedTopics);
        data.setIsNew(1);

        return data;
    }

    public void writeDatas(Data data) {
        Path path = Paths.get("src/main/resources/articles.csv");
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(data.getArticleID()) + "\t");
        sb.append(data.getTitle() + "\t");
        sb.append(data.getMainTopic() + "\t");
        sb.append(data.getAuthor() + "\t");
        sb.append(data.getRelatedTopics());

        try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
            writer.newLine();
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> fileToList() {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/urls.txt"))) {
            ArrayList<String> list = new ArrayList<>();

            stream
                    .filter(s -> s.endsWith("/"))
                    .forEach(list::add);

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<String>();
    }

    public Url urlToUrlCollection(String url) {
        Url _url = new Url();

        _url.setUrl(url);
        _url.setIsNew(1);

        return _url;
    }

    public Like urlToLikeCollection(String url) {

        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        org.bson.Document queryFilter =  new org.bson.Document("articleLink", url);

        FindIterable<Data> result = collection.find(queryFilter).limit(1);

        if (result != null) {
            Like like = new Like();

            Data data = result.first();
            like.setTitle(data.getTitle());
            like.setMainTopic(data.getMainTopic());
            like.setIsNew(1);

            return like;
        }

        return new Like();
    }

    public void writeLikes(Like like) {

        Path path = Paths.get("src/main/resources/likes.csv");

        StringBuilder sb = new StringBuilder();
        sb.append(like.getTitle() + "\t");
        sb.append(like.getMainTopic());

        try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
            writer.newLine();
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    // Create's a collection named counter if there's none.
    // Increments counterValue by 1 and returns it.
    // Purpose of this collection : Defines an articleID for each article.
     */
    public int getNextArticleIdSequence() {
        MongoCollection<Counter> collection = database.getCollection("counter", Counter.class);

        org.bson.Document query = new org.bson.Document("counterName", "articleID");
        org.bson.Document update = new org.bson.Document();
        org.bson.Document inside = new org.bson.Document();
        inside.put("counterValue", 1);
        update.put("$inc", inside);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);

        Counter doc = collection.findOneAndUpdate(query, update, options);
        return doc.getCounterValue();
    }
}
