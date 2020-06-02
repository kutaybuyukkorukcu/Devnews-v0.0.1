package controller;

import com.google.gson.Gson;
import service.DataService;
import utils.StandardResponse;
import utils.StatusResponse;

import static spark.Spark.get;

public class DataController {

    protected final DataService dataService;

    public DataController() {

        dataService = new DataService();

        // Get datas stored in Data collection
        get("/v1/datas", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(),new Gson().toJsonTree(dataService.getDatas())));
        });
    }
}
