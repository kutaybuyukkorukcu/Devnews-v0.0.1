package controller;

import com.google.gson.Gson;
import exception.UserNotFoundException;
import helper.JwtAuthentication;
import domain.User;
import service.UserService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static spark.Spark.post;

public class UserController {

    protected final UserService userService;
    protected final JwtAuthentication jwtAuthentication;

    public UserController() {

        userService = new UserService();
        jwtAuthentication = new JwtAuthentication();

        post("/signin", (request, response) -> {

            try {
                response.type("application/json");

                User requestUser = new Gson().fromJson(request.body(), User.class);

                // Creating static list for jwt. This will change in the future with the extension of domain models.
                List<String> roles = new ArrayList<>();
                roles.add("admin");

                Optional<User> user = userService.findUserByUsername(requestUser.getUsername());

                if (!user.isPresent()) {
                    return new Gson().toJsonTree(
                            new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                    StatusResponse.ERROR.getMessage())
                    );
                }

                String token = jwtAuthentication.createToken(requestUser.getUsername(), roles);

                return new Gson().toJson(
                        new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                                StatusResponse.SUCCESS.getMessage(), new Gson().toJsonTree(token))
                );
            } catch (Exception e) {
                // TODO : handling
                System.out.println(e);
                e.printStackTrace();
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                            StatusResponse.ERROR.getMessage())
            );
        });

        post("/signup", (request, response) -> {
            response.type("application/json");

            User user = new Gson().fromJson(request.body(), User.class);

            userService.addOrUpdateUser(user);

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage())
            );
        });

        get("/v1/users:id", (request, response) -> {
            response.type("application/json");

            int id = Integer.parseInt(request.params(":id"));

            Optional<User> user = userService.findUserById(id);
            user.orElseThrow(UserNotFoundException::new);

            if (!user.isPresent()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.SUCCESS.getMessage())
                );
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(), new Gson().toJsonTree(user.get()))
            );
        });
    }
}
