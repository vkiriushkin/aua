package com.auto.load.web;

import com.auto.data.Car;
import com.auto.data.Criterion;
import com.auto.request.CarByIdRequest;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CarWebLoader {

    private static CarWebLoader webLoader;
    private Gson gson = new Gson();
    private static int FIRST_PAGE_INDEX = 0;
    private static int PAGE_LIMIT = 10;

    private String COUNT_REQUEST_TEMPLATE = "http://auto.ria.com/blocks_search_ajax/search/?" +
            "page=%d" +
            "&countpage=%d" +
            "&category_id=1" +
            "&marka_id[0]=%d" +
            "&model_id[0]=%d" +
            "&currency=1" +
            "&state[0]=0" +
            "&city[0]=0" +
            "&fuelRatesType=city";

    private String FROM_YEAR_TEMPLATE = "&s_yers[0]=%d";
    private String TO_YEAR_TEMPLATE =  "&po_yers[0]=%d";

    private CarWebLoader() {}

    public static CarWebLoader getInstance() {
        if (webLoader == null)
            webLoader = new CarWebLoader();

        return webLoader;
    }

    private String getPageJSON(Criterion criterion, int pageIndex, int pageLimit) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if (criterion.getFromYear() != 0 && !COUNT_REQUEST_TEMPLATE.contains(String.format(FROM_YEAR_TEMPLATE, criterion.getFromYear())))
            COUNT_REQUEST_TEMPLATE = COUNT_REQUEST_TEMPLATE.concat(String.format(FROM_YEAR_TEMPLATE, criterion.getFromYear()));
        if (criterion.getToYear() != 0 && !COUNT_REQUEST_TEMPLATE.contains(String.format(TO_YEAR_TEMPLATE, criterion.getToYear())))
            COUNT_REQUEST_TEMPLATE = COUNT_REQUEST_TEMPLATE.concat(String.format(TO_YEAR_TEMPLATE, criterion.getToYear()));

        HttpGet httpget = new HttpGet(String.format(COUNT_REQUEST_TEMPLATE, pageIndex, pageLimit, criterion.getMakrId(), criterion.getModelId()));
        System.out.println("Executing request " + httpget.getRequestLine());

        // Create a custom response handler
        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        return httpclient.execute(httpget, responseHandler);
    }

    public int loadCount(Criterion criterion) throws IOException {
        String firstPageJson = getPageJSON(criterion, FIRST_PAGE_INDEX, PAGE_LIMIT);
        JsonObject jsonObject = gson.fromJson(firstPageJson, JsonObject.class);

        return jsonObject.get("result").getAsJsonObject().get("search_result").getAsJsonObject().get("count").getAsInt();
    }

    public List<Car> loadCars(Criterion criterion) throws IOException {
        int count = loadCount(criterion);
        int pageCount = 30;
        int totalPages = count / pageCount;
        List<Car> carList = new ArrayList<>();
        Stopwatch stopwatch = Stopwatch.createUnstarted();

        for (int pageIndex = 0; pageIndex <= totalPages; pageIndex++) {
            System.out.println("----------------------------------------");
            System.out.println("*** Getting cars from page:" + (pageIndex+1) + " ***");
            stopwatch.start();

            String idsJSON = getPageJSON(criterion,pageIndex,pageCount);
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
            System.out.println("*** Page " + (pageIndex+1) + "/" + (totalPages+1) + " done in " + stopwatch.elapsed(TimeUnit.SECONDS) + " seconds ***");
            stopwatch.reset();
        }

        return carList;
    }
}
