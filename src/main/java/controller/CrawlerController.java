package controller;

import com.google.gson.Gson;
import domain.Data;
import service.CrawlerService;
import service.DataService;
import service.UrlService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.List;
import java.util.Optional;

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

            List<String> urlList = urlService.getNewUrlsAsString();

            if (urlList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            for (String url : urlList) {
                Data data = crawlerService.urlToData(url);
                crawlerService.writeDatas(data);
                dataService.addData(data);
            }

            List<Data> dataList = dataService.getDatas();

            if (dataList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(), new Gson().toJsonTree(dataService.getDatas())));
        });
    }
}
