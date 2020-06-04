package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;
import domain.Article;
import domain.Data;
import domain.Like;
import org.bson.Document;
import utils.MainTopics;
import utils.initializeDB;
import utils.initializeLists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleService {

    protected final MongoDatabase database;
    protected final LikeService likeService;
    protected final DataService dataService;

    public ArticleService() {
        database = initializeDB.getDatabase();
        likeService = new LikeService();
        dataService = new DataService();
    }

    public void JsonObjectToList(JsonObject jsonObject, ArrayList<Article> articles) {

        JsonArray jsonArray =  jsonObject.getAsJsonArray("list");
        Iterator<JsonElement> iter = jsonArray.iterator();


        while(iter.hasNext()) {
                Article article = new Article();
                JsonArray arr = (JsonArray) iter.next();
                article.setArticleId(arr.get(0).getAsInt() + 1); // Because recom.py subtracts 1 from articleID
                article.setSimilarityScore(arr.get(1).getAsDouble());
                articles.add(article);
            }

    }


    // Belli bir formata soktuktan sonra hem elimde bulunan makale verilerini hem de kisinin begendiklerini artik recommendation icin yollayabilirim.
    public JsonObject getRecommendations(String title) {

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:5000/api/recommend")
                    .queryString("title", title)
                    .asJson();

            JsonNode jsonNode = jsonResponse.getBody();
            JSONObject jsonObject = jsonNode.getObject();
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (UnirestException e) {
             // TODO : handling
            e.printStackTrace();
        }

        return new JsonObject();
    }

    public ArrayList<Article> returnRecommendations(ArrayList<Article> articles) {

        Comparator<Article> comparator = new Comparator<Article>() {
            @Override
            public int compare(Article i1, Article i2) {
                int a1 = (int) Math.round(i1.getSimilarityScore());
                int a2 = (int) Math.round(i2.getSimilarityScore());
                return a2 - a1;
            }
        };

        // Set kontrolu yapilsin. Ayni articleID'ye sahipler alinmasin.
        return (ArrayList<Article>) articles.stream()
                .sorted(comparator)
                .limit(5)
                .collect(Collectors.toList());
    }

    public void sendRecommendations(ArrayList<Article> articles, ArrayList<Data> recommendedArticles) {

        Iterator<Article> iter = articles.iterator();

        while(iter.hasNext()) {
            int articleID = iter.next().getArticleId();

            MongoCollection<Data> collection = database.getCollection("data", Data.class);

            Document queryFilter =  new Document("articleID", articleID);

            Data data = collection.find(queryFilter).limit(1).first();

            recommendedArticles.add(data);
        }

    }

    public void getRecommendedArticles() {
        List<Like> likes =  likeService.getNewLikes();

        // TODO : exception handling if likes empty, or something else
        Iterator<Like> iter = likes.iterator();

        while(iter.hasNext()) {
            Like like = iter.next();
            JsonObject jsonObject = getRecommendations(like.getTitle());

            if (like.getMainTopic().equals(MainTopics.DEVELOPMENT.getMainTopic())) {
                JsonObjectToList(jsonObject, initializeLists.development);
            } else if (like.getMainTopic().equals(MainTopics.ARCHITECTURE.getMainTopic())) {
                JsonObjectToList(jsonObject, initializeLists.architecture);
            } else if (like.getMainTopic().equals(MainTopics.AI.getMainTopic())) {
                JsonObjectToList(jsonObject, initializeLists.ai);
            } else if (like.getMainTopic().equals(MainTopics.CULTURE.getMainTopic())) {
                JsonObjectToList(jsonObject, initializeLists.culture);
            } else if (like.getMainTopic().equals(MainTopics.DEVOPS.getMainTopic())) {
                JsonObjectToList(jsonObject, initializeLists.devops);
            } else {
                JsonObjectToList(jsonObject, new ArrayList<Article>());
            }

            // TODO: i shouldnt check for else
        }
    }

    public void recommendedArticlesToList() {
        initializeLists.development = returnRecommendations(initializeLists.development);
        initializeLists.architecture = returnRecommendations(initializeLists.architecture);
        initializeLists.ai = returnRecommendations(initializeLists.ai);
        initializeLists.culture = returnRecommendations(initializeLists.culture);
        initializeLists.devops = returnRecommendations(initializeLists.devops);


        sendRecommendations(initializeLists.development, initializeLists.recommendedArticles);
        sendRecommendations(initializeLists.architecture, initializeLists.recommendedArticles);
        sendRecommendations(initializeLists.ai, initializeLists.recommendedArticles);
        sendRecommendations(initializeLists.culture, initializeLists.recommendedArticles);
        sendRecommendations(initializeLists.devops, initializeLists.recommendedArticles);


//        Mail mail = new Mail();
//        mail.sendMail(utils.initializeLists.recommendedArticles.toString());

        // TODO : recommendedArticles.toString() -> mail formatina donusturecek bir fonksiyon yaz.
    }
}
