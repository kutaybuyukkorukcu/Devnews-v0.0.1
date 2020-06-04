package service;

import com.google.gson.*;
import com.mongodb.client.MongoDatabase;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import domain.Article;
import domain.Data;
import domain.Like;
import repository.DataRepository;
import utils.MainTopics;
import utils.initializeDB;
import utils.initializeLists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleService {

    protected final LikeService likeService;
    protected final DataService dataService;
    protected final DataRepository dataRepository;

    public ArticleService() {
        likeService = new LikeService();
        dataService = new DataService();
        dataRepository = new DataRepository();
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
    public JsonObject getRecommendation(String title) {

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:5000/api/recommend")
                    .queryString("title", title)
                    .asJson();

            JsonNode jsonNode = jsonResponse.getBody();
            String jsonString = jsonNode.getObject().toString();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);

            return jsonObject;
        } catch (UnirestException e) {
             // TODO : handling
            e.printStackTrace();
        }

        return JsonNull.INSTANCE.getAsJsonObject();
    }

    public ArrayList<Article> getTopRecommendations(ArrayList<Article> articles) {

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

    public void recommendationsToArticleList(ArrayList<Article> articles, ArrayList<Data> recommendedArticles) {

        Iterator<Article> iter = articles.iterator();

        while(iter.hasNext()) {
            int articleID = iter.next().getArticleId();

            Data data = dataRepository.findByArticleId(articleID);

            recommendedArticles.add(data);
        }

    }

    public void getRecommendedArticles() {
        List<Like> likes =  likeService.getNewLikes();

        // TODO : exception handling if likes empty, or something else
        Iterator<Like> iter = likes.iterator();

        while(iter.hasNext()) {
            Like like = iter.next();
            JsonObject jsonObject = getRecommendation(like.getTitle());

            if (jsonObject.isJsonNull()) {
                // TODO : exception handling
            }

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
        initializeLists.development = getTopRecommendations(initializeLists.development);
        initializeLists.architecture = getTopRecommendations(initializeLists.architecture);
        initializeLists.ai = getTopRecommendations(initializeLists.ai);
        initializeLists.culture = getTopRecommendations(initializeLists.culture);
        initializeLists.devops = getTopRecommendations(initializeLists.devops);


        recommendationsToArticleList(initializeLists.development, initializeLists.recommendedArticles);
        recommendationsToArticleList(initializeLists.architecture, initializeLists.recommendedArticles);
        recommendationsToArticleList(initializeLists.ai, initializeLists.recommendedArticles);
        recommendationsToArticleList(initializeLists.culture, initializeLists.recommendedArticles);
        recommendationsToArticleList(initializeLists.devops, initializeLists.recommendedArticles);


//        Mail mail = new Mail();
//        mail.sendMail(utils.initializeLists.recommendedArticles.toString());

        // TODO : recommendedArticles.toString() -> mail formatina donusturecek bir fonksiyon yaz.
    }
}
