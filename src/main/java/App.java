import controller.*;
import helper.CorsFilter;
import helper.JwtAuthentication;
import utils.initializeDB;

import utils.*;

public class App {

    public static void main(String[] args) {

        initializeDB.createCounter();
        initializeDB.createArticle();
        initializeDB.createUrl();
        initializeDB.createLike();
        initializeDB.createUser();

        initializeLists.generateLists();

        final CorsFilter corsFilter = new CorsFilter();
        final JwtAuthentication jwtAuthentication = new JwtAuthentication();

        corsFilter.apply();

//        before("/v1/*", (request, response) -> {
//            String jwt = jwtAuthentication.resolveToken(request);
//
//            // JWT unsuccessfully authenticated
//            // TODO : decodeJWT'den exception donunce program nasil davraniyor?
//            if (jwt.isEmpty() && !jwtAuthentication.decodeJWT(jwt)) {
//                halt(404, "Jwt unsuccessfuly authenticated");
//            }
//        });

        final CrawlerController crawlerController = new CrawlerController();
        final ArticleController articleController = new ArticleController();
        final RecommendationController recommendationController = new RecommendationController();
        final UrlController urlController = new UrlController();
        final UserController userController = new UserController();
    }
}