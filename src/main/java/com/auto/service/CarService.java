package com.auto.service;

import com.auto.data.Car;
import com.auto.request.CarByIdRequest;
import com.auto.request.IDsRequest;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CarService {

    private int makrId;
    private int modelId;
    private int fromYear;
    private int toYear;

    public int getCount() throws IOException {
        Gson gson = new Gson();
        IDsRequest iDsRequest = new IDsRequest.Builder().page(0).markId(makrId).modelId(modelId).fromYear(fromYear).toYear(toYear).build();
        String firstPageJson = iDsRequest.getIDs();
        JsonObject jsonObject = gson.fromJson(firstPageJson, JsonObject.class);
        int count = jsonObject.get("result").getAsJsonObject().get("search_result").getAsJsonObject().get("count").getAsInt();
        return count;
    }

    public List<Car> getCars() throws IOException {
        Gson gson = new Gson();
        IDsRequest iDsRequest = new IDsRequest.Builder().page(0).markId(makrId).modelId(modelId).fromYear(fromYear).toYear(toYear).build();
        String firstPageJson = iDsRequest.getIDs();
        JsonObject jsonObject = gson.fromJson(firstPageJson, JsonObject.class);
        int count = jsonObject.get("result").getAsJsonObject().get("search_result").getAsJsonObject().get("count").getAsInt();
//        JsonArray firstPageIds = jsonObject.get("result").getAsJsonObject().get("search_result").getAsJsonObject().get("ids").getAsJsonArray();

        int totalPages = count / 10;
        List<Car> carList = new ArrayList<>();
        Stopwatch stopwatch = Stopwatch.createUnstarted();

        for (int i = 0; i <= totalPages; i++) {
            System.out.println("----------------------------------------");
            System.out.println("*** Getting cars from page:" + (i+1) + " ***");
            stopwatch.start();

            iDsRequest = new IDsRequest.Builder().page(i).markId(makrId).modelId(modelId).fromYear(fromYear).toYear(toYear).build();
            String idsJSON = iDsRequest.getIDs();
            JsonObject resultsObject = gson.fromJson(idsJSON, JsonObject.class);
            JsonArray asJsonArray = resultsObject.get("result").getAsJsonObject().get("search_result").getAsJsonObject().get("ids").getAsJsonArray();

            for (JsonElement element : asJsonArray) {
                CarByIdRequest request = new CarByIdRequest(element.getAsInt());
                String carJSON = request.getCar();
                JsonObject autoData = gson.fromJson(carJSON, JsonObject.class).get("result").getAsJsonObject().get("auto_data").getAsJsonObject();
                Car car = new Car.Builder()
                        .carId(autoData.get("auto_id").getAsInt())
                        .markId(autoData.get("marka_id").getAsInt())
                        .modelId(autoData.get("model_id").getAsInt())
                        .version(autoData.get("version").getAsString())
                        .year(autoData.get("yers").getAsInt())
                        .engineVolume(autoData.get("engineVolume").getAsDouble())
                        .price(autoData.get("price").getAsInt())
                        .build();

                carList.add(car);
            }

            stopwatch.stop();
            System.out.println("----------------------------------------");
            System.out.println("*** Page " + (i+1) + "/" + (totalPages+1) + " done in " + stopwatch.elapsed(TimeUnit.SECONDS) + " seconds ***");
            stopwatch.reset();
        }

        return carList;
    }

    private CarService(Builder builder) {
        makrId = builder.makrId;
        modelId = builder.modelId;
        fromYear = builder.fromYear;
        toYear = builder.toYear;
    }


    public static final class Builder {
        private int makrId;
        private int modelId;
        private int fromYear;
        private int toYear;

        public Builder() {
        }

        public Builder makrId(int makrId) {
            this.makrId = makrId;
            return this;
        }

        public Builder modelId(int modelId) {
            this.modelId = modelId;
            return this;
        }

        public Builder fromYear(int fromYear) {
            this.fromYear = fromYear;
            return this;
        }

        public Builder toYear(int toYear) {
            this.toYear = toYear;
            return this;
        }

        public CarService build() {
            return new CarService(this);
        }
    }
}
