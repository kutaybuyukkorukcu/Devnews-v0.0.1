package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import domain.Article;
import exception.GetRecommendationHttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CrawlerService;
import service.ArticleService;
import service.UrlService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;

public class CrawlerController {

    protected final CrawlerService crawlerService;
    protected final UrlService urlService;
    protected final ArticleService articleService;
    protected static final Logger logger = LoggerFactory.getLogger(CrawlerController.class);

    public CrawlerController() {

        crawlerService = new CrawlerService();
        urlService = new UrlService();
        articleService = new ArticleService();

        // Reads each url from database, crawls it and then
        // -> Appends each formatted data into articles.csv file
        // -> Inserts each formatted data into Article collection
        // Use /crawl for generating .csv file which contains all articles
        get("/v1/infoq/articles", (request, response) -> {
            response.type("application/json");

            List<String> articleLinkList = urlService.getArticleLinksAsList();

            if (articleLinkList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            for (String articleLink : articleLinkList) {
                Article article;

                // TODO : crawlArticleLinkIntoArticle() icerisine null deger articleLink verebilriiz.
                // Bunu icerisinde catchlemek lazim.
                try {
                    article = crawlerService.crawlArticleLinkIntoArticle(articleLink);
                } catch (IOException e) {
                    logger.error("An exception occurred!", e);

                    return new Gson().toJson(
                            new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                    StatusResponse.ERROR.getMessage()));
                }

//                crawlerService.writeArticlesIntoCSV(article);
                articleService.addArticle(article);
            }

            List<Article> articleList = articleService.getArticles();

            if (articleList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(), new Gson().toJsonTree(articleService.getArticles())));
        });

        get("/v1/reddit/articles", (request, response) -> {

            response.type("application/type");
            List<Article> testSubredditCards;

            // TODO : getArticleIdSequence() is inconsistent. Check it.

            try {
                String subreddit = request.queryParams("subreddit");

                JsonObject jsonObject = crawlerService.getJson(subreddit);

                testSubredditCards = crawlerService.getArticle(jsonObject);

            } catch (Exception e) {
                System.out.println(e.getMessage());

                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(), new Gson().toJsonTree(testSubredditCards)));
        });
    }
}
