package controller;

import com.google.gson.Gson;
import domain.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CrawlerService;
import service.UrlService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static spark.Spark.get;
import static spark.Spark.post;

public class UrlController {

    protected final UrlService urlService;
    protected final CrawlerService crawlerService;
    protected static final Logger logger = LoggerFactory.getLogger(UrlController.class);

    public UrlController() {
        urlService = new UrlService();
        crawlerService = new CrawlerService();

        // Reads each url from text file and then inserts the urls into the Url collection
        get("/v1/urls", (request, response) -> {
            response.type("application/json");

            List<String> articleLinkList;

            try {
                articleLinkList = crawlerService.getArticleLinksFromFileAsList();
            } catch (IOException e) {
                logger.error("An exception occurred!", e);

                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            if (articleLinkList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

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
