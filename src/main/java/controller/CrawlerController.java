package controller;

import com.google.gson.Gson;
import domain.Article;
import service.CrawlerService;
import service.ArticleService;
import service.UrlService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.List;

import static spark.Spark.get;

public class CrawlerController {

    CrawlerService crawlerService;
    UrlService urlService;
    ArticleService articleService;

    public CrawlerController() {

        crawlerService = new CrawlerService();
        urlService = new UrlService();
        articleService = new ArticleService();

        // Reads each url from database, crawls it and then
        // -> Appends each formatted data into articles.csv file
        // -> Inserts each formatted data into Article collection
        // Use /crawl for generating .csv file which contains all articles
        get("/v1/crawl", (request, response) -> {
            response.type("application/json");

            List<String> articleLinkList = urlService.getArticleLinksAsList();

            if (articleLinkList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            for (String articleLink : articleLinkList) {
                Article article = crawlerService.crawlArticleLinkIntoArticle(articleLink);
                crawlerService.writeArticlesIntoCSV(article);
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
    }
}
