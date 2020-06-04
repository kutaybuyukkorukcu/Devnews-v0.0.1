package service;

import com.google.gson.*;
import domain.Recommendation;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import domain.Data;
import domain.Like;
import repository.DataRepository;
import utils.MainTopics;
import utils.initializeLists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationService {

    protected final LikeService likeService;
    protected final DataService dataService;
    protected final MailService mailService;
    protected final DataRepository dataRepository;

    public RecommendationService() {
        likeService = new LikeService();
        dataService = new DataService();
        mailService = new MailService();
        dataRepository = new DataRepository();
    }

    public void JsonObjectToRecommendationList(JsonObject jsonObject, List<Recommendation> recommendations) {

        JsonArray jsonArray =  jsonObject.getAsJsonArray("list");
        Iterator<JsonElement> iter = jsonArray.iterator();


        while(iter.hasNext()) {
                Recommendation recommendation = new Recommendation();
                JsonArray arr = (JsonArray) iter.next();
                recommendation.setArticleId(arr.get(0).getAsInt() + 1); // Because recom.py subtracts 1 from articleID
                recommendation.setSimilarityScore(arr.get(1).getAsDouble());
                recommendations.add(recommendation);
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

    public List<Recommendation> getTopRecommendationsFromList(List<Recommendation> recommendations) {

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
    }

    public void recommendationListToDataList(List<Recommendation> recommendations, List<Data> recommendedArticles) {

        Iterator<Recommendation> iter = recommendations.iterator();

        while(iter.hasNext()) {
            int articleID = iter.next().getArticleId();

            Data data = dataRepository.findByArticleId(articleID);

            recommendedArticles.add(data);
        }

    }

    public void getRecommendations() {
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
                JsonObjectToRecommendationList(jsonObject, initializeLists.development);
            } else if (like.getMainTopic().equals(MainTopics.ARCHITECTURE.getMainTopic())) {
                JsonObjectToRecommendationList(jsonObject, initializeLists.architecture);
            } else if (like.getMainTopic().equals(MainTopics.AI.getMainTopic())) {
                JsonObjectToRecommendationList(jsonObject, initializeLists.ai);
            } else if (like.getMainTopic().equals(MainTopics.CULTURE.getMainTopic())) {
                JsonObjectToRecommendationList(jsonObject, initializeLists.culture);
            } else if (like.getMainTopic().equals(MainTopics.DEVOPS.getMainTopic())) {
                JsonObjectToRecommendationList(jsonObject, initializeLists.devops);
            } else {
                JsonObjectToRecommendationList(jsonObject, new ArrayList<Recommendation>());
            }

            // TODO: i shouldnt check for else
        }
    }

    public void topRecommendationsToDataList() {
        initializeLists.development = getTopRecommendationsFromList(initializeLists.development);
        initializeLists.architecture = getTopRecommendationsFromList(initializeLists.architecture);
        initializeLists.ai = getTopRecommendationsFromList(initializeLists.ai);
        initializeLists.culture = getTopRecommendationsFromList(initializeLists.culture);
        initializeLists.devops = getTopRecommendationsFromList(initializeLists.devops);


        recommendationListToDataList(initializeLists.development, initializeLists.recommendedArticles);
        recommendationListToDataList(initializeLists.architecture, initializeLists.recommendedArticles);
        recommendationListToDataList(initializeLists.ai, initializeLists.recommendedArticles);
        recommendationListToDataList(initializeLists.culture, initializeLists.recommendedArticles);
        recommendationListToDataList(initializeLists.devops, initializeLists.recommendedArticles);


//        mailService.sendMail(mailService.createMail
//        mailService.sendMail(utils.initializeLists.recommendedArticles.toString());

        // TODO : recommendedArticles.toString() -> mail formatina donusturecek bir fonksiyon yaz.
    }
}
