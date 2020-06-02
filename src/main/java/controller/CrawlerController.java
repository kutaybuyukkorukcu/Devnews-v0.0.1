package controller;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import model.Counter;
import model.Data;
import model.Url;
import org.bson.Document;
import service.CrawlerService;
import service.DataService;
import service.UrlService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.ArrayList;

import static spark.Spark.get;

public class CrawlerController {

    CrawlerService crawlerService;
    UrlService urlService;
    DataService dataService;

    public CrawlerController() {

        crawlerService = new CrawlerService();
        urlService = new UrlService();
        dataService = new DataService();

        // Reads each url from database, crawls it and then
        // -> Appends each formatted data into articles.csv file
        // -> Inserts each formatted data into Data collection
        // Use /crawl for generating .csv file which contains all articles
        get("/v1/crawl", (request, response) -> {
            response.type("application/json");

            ArrayList<String> urls = urlService.getUrlsAsList();

            for (String url : urls) {
                Data data = crawlerService.urlToData(url);
                crawlerService.writeDatas(data);
                dataService.addData(data);
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(), new Gson().toJsonTree(dataService.getDatas())));
        });
    }
}
