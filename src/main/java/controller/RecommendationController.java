package controller;

import com.google.gson.Gson;
import service.ArticleService;
import service.LikeService;
import utils.StandardResponse;
import utils.StatusResponse;
import utils.initializeLists;

import static spark.Spark.get;

public class RecommendationController {

    protected final LikeService likeService;
    protected final ArticleService articleService;

    public RecommendationController() {

        likeService = new LikeService();
        articleService = new ArticleService();

        get("/v1/recommend", (request, response) -> {

            response.type("application/json");

            initializeLists.recommendedArticles.clear();

            likeService.addLikedDataToDatabase();
            articleService.getRecommendedArticles();
            articleService.recommendedArticlesToList();

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(),
                            new Gson().toJsonTree(initializeLists.recommendedArticles)));
        });
    }
}
