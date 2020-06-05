package service;

import com.google.gson.*;
import domain.Article;
import domain.Recommendation;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import domain.Like;
import repository.ArticleRepository;
import utils.MainTopics;
import utils.initializeLists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationService {

    protected final LikeService likeService;
    protected final ArticleService articleService;
    protected final MailService mailService;
    protected final ArticleRepository articleRepository;

    public RecommendationService() {
        likeService = new LikeService();
        articleService = new ArticleService();
        mailService = new MailService();
        articleRepository = new ArticleRepository();
    }

    public void recommendationIntoRecommendationList(JsonObject jsonObject, List<Recommendation> recommendations) {
    try {

            JsonArray jsonArray =  jsonObject.getAsJsonArray("list");
            Iterator<JsonElement> iter = jsonArray.iterator();

            while(iter.hasNext()) {
                    Recommendation recommendation = new Recommendation();
                    JsonArray arr = (JsonArray) iter.next();
                    recommendation.setArticleId(arr.get(0).getAsInt() + 1); // Because recom.py subtracts 1 from articleID
                    recommendation.setSimilarityScore(arr.get(1).getAsDouble());
                    recommendations.add(recommendation);
                }

    } catch (Exception e) {
        System.out.println(e);
        System.out.println("recommendationIntoRecommendationList");
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
            System.out.println("title : " + title);
            System.out.println(jsonObject.toString());

            return jsonObject;
        } catch (UnirestException e) {
             // TODO : handling
            System.out.println(e);
            System.out.println("getRecommendation");

        }

//        return JsonNull.INSTANCE.getAsJsonObject();
        return new JsonObject();
    }

    public List<Recommendation> getTopRecommendationsFromList(List<Recommendation> recommendations) {
try{
        Comparator<Recommendation> comparator = new Comparator<Recommendation>() {
            @Override
            public int compare(Recommendation i1, Recommendation i2) {
                int a1 = (int) Math.round(i1.getSimilarityScore());
                int a2 = (int) Math.round(i2.getSimilarityScore());
                return a2 - a1;
            }
        };

        // Set kontrolu yapilsin. Ayni articleID'ye sahipler alinmasin.
        return recommendations.stream()
                .sorted(comparator)
                .limit(5)
                .collect(Collectors.toList());
} catch (Exception e) {
    System.out.println(e);
    System.out.println("getTopRecommendationsFromList");
}
return new ArrayList<Recommendation>();
    }

    public void recommendationListToArticleList(List<Recommendation> recommendations, List<Article> recommendedArticles) {
try{

        Iterator<Recommendation> iter = recommendations.iterator();

        while(iter.hasNext()) {
            int articleID = iter.next().getArticleId();

            Article article = articleRepository.findByArticleId(articleID);

            recommendedArticles.add(article);
        }
}catch (Exception e) {
    System.out.println(e);
    System.out.println("recommendationListToArticleList");
}


    }

    public void getRecommendations() {
        try {

        List<Like> likes =  likeService.getNewLikes();

        // TODO : exception handling if likes empty, or something else
        Iterator<Like> iter = likes.iterator();

        while(iter.hasNext()) {
            Like like = iter.next();
            JsonObject jsonObject = getRecommendation(like.getTitle());

//            if (jsonObject.isJsonNull()) {
//                // TODO : exception handling
//                System.out.println(jsonObject.toString());
//            }

            if (like.getMainTopic().equals(MainTopics.DEVELOPMENT.getMainTopic())) {
                recommendationIntoRecommendationList(jsonObject, initializeLists.development);
            } else if (like.getMainTopic().equals(MainTopics.ARCHITECTURE.getMainTopic())) {
                recommendationIntoRecommendationList(jsonObject, initializeLists.architecture);
            } else if (like.getMainTopic().equals(MainTopics.AI.getMainTopic())) {
                recommendationIntoRecommendationList(jsonObject, initializeLists.ai);
            } else if (like.getMainTopic().equals(MainTopics.CULTURE.getMainTopic())) {
                recommendationIntoRecommendationList(jsonObject, initializeLists.culture);
            } else if (like.getMainTopic().equals(MainTopics.DEVOPS.getMainTopic())) {
                recommendationIntoRecommendationList(jsonObject, initializeLists.devops);
            } else {
                recommendationIntoRecommendationList(jsonObject, new ArrayList<>());
            }

            // TODO: i shouldnt check for else
        }
        } catch (NullPointerException e) {
            System.out.println(e);
            System.out.println("getRecommendations");
            throw e;
        }
    }

    public void topRecommendationsIntoArticleList() {
        initializeLists.development = getTopRecommendationsFromList(initializeLists.development);
        initializeLists.architecture = getTopRecommendationsFromList(initializeLists.architecture);
        initializeLists.ai = getTopRecommendationsFromList(initializeLists.ai);
        initializeLists.culture = getTopRecommendationsFromList(initializeLists.culture);
        initializeLists.devops = getTopRecommendationsFromList(initializeLists.devops);

        System.out.println("dev list " + initializeLists.development.toString());
        System.out.println("arch list " + initializeLists.architecture.toString());
        System.out.println("ai list " + initializeLists.ai.toString());
        System.out.println("cult list " + initializeLists.culture.toString());
        System.out.println("devops list " + initializeLists.devops.toString());



        recommendationListToArticleList(initializeLists.development, initializeLists.recommendedArticles);
        recommendationListToArticleList(initializeLists.architecture, initializeLists.recommendedArticles);
        recommendationListToArticleList(initializeLists.ai, initializeLists.recommendedArticles);
        recommendationListToArticleList(initializeLists.culture, initializeLists.recommendedArticles);
        recommendationListToArticleList(initializeLists.devops, initializeLists.recommendedArticles);


//        mailService.sendMail(mailService.createMail
//        mailService.sendMail(utils.initializeLists.recommendedArticles.toString());

        // TODO : recommendedArticles.toString() -> mail formatina donusturecek bir fonksiyon yaz.
    }
}
