package repository;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import domain.Article;
import domain.Counter;
import domain.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import utils.initializeDB;

public class ArticleRepository implements IRepository<Article>{

    protected final MongoDatabase database;

    public ArticleRepository() {
        database = initializeDB.getDatabase();
    }

    @Override
    public void add(Article article) {
        MongoCollection<Article> collection = database.getCollection("article", Article.class);

        collection.insertOne(article);
    }

    @Override
    public void update(Article article) {

    }

    @Override
    public Article findById(int id) {
        return null;
    }

    @Override
    public List<Article> findAll() {
        MongoCollection<Article> collection = database.getCollection("article", Article.class);

        MongoCursor<Article> cursor = collection.find().iterator();
        List<Article> articleList = new ArrayList<>();

        while (cursor.hasNext()) {
            Article article = cursor.next();
            articleList.add(article);
        }

        return articleList;
    }

    public Article findByTitle(String title) {
        MongoCollection<Article> collection = database.getCollection("article", Article.class);

        Document queryByTitle = new Document("title", title);

        Article article = collection.find(queryByTitle).first();

        return article;
    }

    public Article findByArticleId(int articleId) {
        MongoCollection<Article> collection = database.getCollection("article", Article.class);

        Document queryByArticleId = new Document("articleId", articleId);

        Article article = collection.find(queryByArticleId).first();

        return article;
    }

    public Article findByArticleLink(String link) {
        MongoCollection<Article> collection = database.getCollection("article", Article.class);

        Document queryByArticleLink =  new Document("articleLink", link);

        Article article = collection.find(queryByArticleLink).first();

        return article;
    }

    public int getNextArticleIdSequence() {
        MongoCollection<Article> collection = database.getCollection("article", Article.class);

        if (collection.find().first() == null) {
            return 1;
        }

        Article article = collection.find().sort(new Document("_id", -1)).first();

        return article.getArticleId() + 1;
    }
}
