package controller;

import com.google.gson.Gson;
import domain.Url;
import service.CrawlerService;
import service.UrlService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.ArrayList;
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

            List<String> urlList = crawlerService.fileToList();

            for (String url : urlList) {
                Url link = crawlerService.urlToUrlCollection(url);
                urlService.addUrl(link);
            }

            List<String> newUrlList = urlService.getNewUrlsAsString();

            if (newUrlList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(), new Gson().toJsonTree(newUrlList)));
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
