package controller;

import com.google.gson.Gson;
import service.RecommendationService;
import service.LikeService;
import utils.StandardResponse;
import utils.StatusResponse;
import utils.initializeLists;

import static spark.Spark.get;

public class RecommendationController {

    protected final LikeService likeService;
    protected final RecommendationService recommendationService;

    public RecommendationController() {

        likeService = new LikeService();
        recommendationService = new RecommendationService();

        get("/v1/recommend", (request, response) -> {

            response.type("application/json");

            initializeLists.recommendedArticles.clear();

            likeService.addLikedDataToDatabase();
            recommendationService.getRecommendations();
            recommendationService.topRecommendationsToDataList();

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(),
                            new Gson().toJsonTree(initializeLists.recommendedArticles)));
        });
    }
}
