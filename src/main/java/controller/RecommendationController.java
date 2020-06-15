package controller;

import com.google.gson.Gson;
import exception.GetRecommendationHttpException;
import exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.RecommendationService;
import service.LikeService;
import utils.StandardResponse;
import utils.StatusResponse;
import utils.initializeLists;

import static spark.Spark.get;

public class RecommendationController {

    protected final LikeService likeService;
    protected final RecommendationService recommendationService;

    protected static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    public RecommendationController() {

        likeService = new LikeService();
        recommendationService = new RecommendationService();
        get("/v1/recommend", (request, response) -> {

            response.type("application/json");

            initializeLists.recommendedArticles.clear();

            // TODO : based on user ID, update user's old liked articles isNew 1 to 0

            try {
                likeService.addLikedArticlesIntoLikeCollection();
                recommendationService.getRecommendations();
                recommendationService.topRecommendationsIntoArticleList();

            } catch (GetRecommendationHttpException | ResourceNotFoundException e) {
                logger.error("An exception occurred!", e);

                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(),
                            new Gson().toJsonTree(initializeLists.recommendedArticles.toString())));
            });
    }
}
