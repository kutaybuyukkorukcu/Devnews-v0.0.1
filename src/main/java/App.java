import controller.*;
import exception.InvalidJwtAuthenticationException;
import helper.CorsFilter;
import helper.JwtAuthentication;
import utils.initializeDB;

import utils.*;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {

        initializeDB.createCounter();
        initializeDB.createArticle();
        initializeDB.createUrl();
        initializeDB.createLike();
        initializeDB.createUser();

        initializeLists.generateLists();

//        final CorsFilter corsFilter = new CorsFilter();
        final JwtAuthentication jwtAuthentication = new JwtAuthentication();

//        corsFilter.apply();

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

//        before("/v1/*", (request, response) -> {
//            String jwt = jwtAuthentication.resolveToken(request);
//
//            try {
//                if (jwt.isEmpty() && !jwtAuthentication.decodeJWT(jwt)) {
//                    halt(404, "Jwt unsuccessfuly authenticated");
//                }
//            } catch (InvalidJwtAuthenticationException e) {
//                return new Gson().toJson(
//                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
//                                StatusResponse.ERROR.getMessage()));
//            }
//        });

//        before("/v1/*", (request, response) -> {
//            String jwt = jwtAuthentication.resolveToken(request);
//
//            try {
//                if (jwt.isEmpty() && !jwtAuthentication.decodeJWT(jwt)) {
//                    halt(404, "Jwt yanlis yo");
//                }
//            } catch (InvalidJwtAuthenticationException e) {
//                halt(400, "Jwt yanlis");
//            }
//        });

        final CrawlerController crawlerController = new CrawlerController();
        final ArticleController articleController = new ArticleController();
        final RecommendationController recommendationController = new RecommendationController();
        final UrlController urlController = new UrlController();
        final UserController userController = new UserController();

    }
}