import com.google.gson.Gson;
import com.google.gson.JsonObject;

import controller.*;
import helper.CorsFilter;
import helper.JwtAuthentication;
import io.jsonwebtoken.Jwt;
import service.*;
import model.*;
import utils.initializeDB;

import utils.*;

import java.util.*;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {

        initializeDB.createCounter();
        initializeDB.createData();
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
        final DataController dataController = new DataController();
        final RecommendationController recommendationController = new RecommendationController();
        final UrlController urlController = new UrlController();
        final UserController userController = new UserController();
    }
}