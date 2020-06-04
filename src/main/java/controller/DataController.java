package controller;

import com.google.gson.Gson;
import domain.Data;
import service.DataService;
import utils.StandardResponse;
import utils.StatusResponse;

import java.util.List;
import java.util.Optional;

import static spark.Spark.get;

public class DataController {

    protected final DataService dataService;

    public DataController() {

        dataService = new DataService();

        // Get datas stored in Data collection
        get("/v1/datas", (request, response) -> {
            response.type("application/json");

            List<Data> dataList = dataService.getDatas();

            if (dataList.isEmpty()) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, StatusResponse.ERROR.getStatusCode(),
                                StatusResponse.ERROR.getMessage()));
            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, StatusResponse.SUCCESS.getStatusCode(),
                            StatusResponse.SUCCESS.getMessage(),new Gson().toJsonTree(dataService.getDatas())));
        });
    }
}
