package controller;

import com.google.gson.Gson;
import domain.Article;
import org.slf4j.LoggerFactory;
import service.ArticleService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.List;

import static spark.Spark.get;

public class ArticleController {

    protected final ArticleService articleService;

    public ArticleController() {

        articleService = new ArticleService();

        // Get datas stored in Article collection
        get("/v1/articles", (request, response) -> {
            response.type("application/json");

            List<Article> articleList = articleService.getArticles();

            if (articleList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(),new Gson().toJsonTree(articleService.getArticles())));
        });
    }
}
