package controller;

import com.google.gson.Gson;
import model.Url;
import service.CrawlerService;
import service.UrlService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.ArrayList;

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

            ArrayList<String> list = crawlerService.fileToList();

            for (String url : list) {
                Url link = crawlerService.urlToUrlCollection(url);
                urlService.addUrl(link);
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage()));
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
