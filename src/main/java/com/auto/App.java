package com.auto;

import com.auto.data.Car;
import com.auto.data.Model;
import com.auto.data.Mark;
import com.auto.request.CarByIdRequest;
import com.auto.request.IDsRequest;
import com.auto.request.ModelsRequest;
import com.auto.request.MarksRequest;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        String requiredMark = "Audi";
        String requiredModel = "RS6";

        Stopwatch overalStopWatch = Stopwatch.createStarted();
        System.out.println("----------------------------------------");
        System.out.println("*** Getting marks ***");

        MarksRequest marksRequest = new MarksRequest();
        String marksJSON = marksRequest.getModels();
        System.out.println(marksJSON);

        Gson gson = new Gson();

        Type marksListType = new TypeToken<List<Mark>>(){}.getType();
        List<Mark> marksList = gson.fromJson(marksJSON, marksListType);

        System.out.println("----------------------------------------");
        System.out.println("=== Found total marks: " + marksList.size() + " ===");

        System.out.println("----------------------------------------");
        System.out.println("*** Getting models by mark ***");

        //filter list of models based on required
        Stream<Mark> marksStream = marksList.stream().filter(mark -> mark.getName().equalsIgnoreCase(requiredMark));

        int markId = marksStream.findFirst().get().getId();
        ModelsRequest modelsRequest = new ModelsRequest(markId);
        String modelJSON = modelsRequest.getModels();
        System.out.println(modelJSON);

        Type modelListType = new TypeToken<List<Model>>(){}.getType();
        List<Model> modelsList = gson.fromJson(modelJSON, modelListType);

        System.out.println("----------------------------------------");
        System.out.println("=== Found total models: " + modelsList.size() + " ===");

        System.out.println("----------------------------------------");
        System.out.println("*** Getting total number of ids by mark and model ***");

        //filter list of marks based on required
        Stream<Model> modelsStream = modelsList.stream().filter(model -> model.getName().equalsIgnoreCase(requiredModel));
        System.out.println("----------------------------------------");
        Model model = modelsStream.findFirst().get();
        System.out.println("=== Found total ads: " + model.getCount() + " ===");

        int totalPages = model.getCount() / 10;
        List<Car> carList = new ArrayList<>();
        Stopwatch stopwatch = Stopwatch.createUnstarted();

        for (int i = 0; i <= totalPages; i++) {
            System.out.println("----------------------------------------");
            System.out.println("*** Getting cars from page:" + (i+1) + " ***");
            stopwatch.start();

            int modelId = model.getId();
            IDsRequest iDsRequest = new IDsRequest(i, markId, modelId);
            String idsJSON = iDsRequest.getIDs();
            JsonObject resultsObject = gson.fromJson(idsJSON, JsonObject.class);
            JsonArray asJsonArray = resultsObject.get("result").getAsJsonObject().get("search_result").getAsJsonObject().get("ids").getAsJsonArray();
            System.out.println("----------------------------------------");
            System.out.println("=== Found ids: " + asJsonArray.toString() + " ===");

            for (JsonElement element : asJsonArray) {
//            System.out.println("*** Getting ad for id=" + element.getAsInt() + " ***");
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
//                System.out.println(car.toString());
            }
            stopwatch.stop();
            System.out.println("----------------------------------------");
            System.out.println("*** Page " + (i+1) + "/" + (totalPages+1) + " done in " + stopwatch.elapsed(TimeUnit.SECONDS) + " seconds ***");
            stopwatch.reset();
        }

        System.out.println("----------------------------------------");
        System.out.println("*** Total average price is " + (int)carList.stream().mapToInt(Car::getPrice).average().getAsDouble()+ "$ ***");
        overalStopWatch.stop();
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        Map<Integer, List<Car>> collectByYear = carList.stream().collect(Collectors.groupingBy(Car::getYear));
        for (Map.Entry<Integer, List<Car>> entry : collectByYear.entrySet()) {
            int averagePrice = (int) entry.getValue().stream().mapToInt(Car::getPrice).average().getAsDouble();
            System.out.println("*** Year:" +entry.getKey() + ", number of ads: " + entry.getValue().size() + ", price:" + averagePrice+ "$ ***");
        }
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println("Total time:" + overalStopWatch.elapsed(TimeUnit.SECONDS) + " seconds");
    }

}
