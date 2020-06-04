package controller;

import com.google.gson.Gson;
import domain.Url;
import service.CrawlerService;
import service.UrlService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.List;
import java.util.Optional;

import static spark.Spark.get;
import static spark.Spark.post;

public class UrlController {

    UrlService urlService;
    CrawlerService crawlerService;

    public UrlController() {
        urlService = new UrlService();
        crawlerService = new CrawlerService();

        // Reads each url from text file and then inserts the urls into the Url collection
        get("/v1/urls", (request, response) -> {
            response.type("application/json");

            List<String> articleLinkList = crawlerService.getArticleLinksFromFileAsList();

            for (String articleLink : articleLinkList) {
                Optional<Url> url = crawlerService.articleLinkToUrl(articleLink);

                if (!url.isPresent()) {
                    return new Gson().toJson(
                            new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                    StatusResponse.ERROR.getMessage()));
                }

                urlService.addUrl(url.get());
            }

            List<String> allArticleLinkList = urlService.getArticleLinksAsList();

            if (allArticleLinkList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(), new Gson().toJsonTree(allArticleLinkList)));
        });

        post("/v1/urls", (request, response) -> {
            response.type("application/json");

            Url url = new Gson().fromJson(request.body(), Url.class);
            urlService.addUrl(url);

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage()));
        });
    }
}