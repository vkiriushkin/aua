package com.auto;

import com.auto.data.Car;
import com.auto.data.Model;
import com.auto.data.Mark;
import com.auto.request.CarByIdRequest;
import com.auto.request.IDsRequest;
import com.auto.request.ModelsRequest;
import com.auto.request.MarksRequest;
import com.auto.service.CarService;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
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

        String requiredMark = "Infiniti";
        String requiredModel = "G";
        int fromYear = 2008;
        int toYear = 2008;

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

        CarService carService = new CarService.Builder().makrId(markId).modelId(model.getId()).fromYear(fromYear).toYear(toYear).build();
        System.out.println("=== Found total ads: " + carService.getCount() + " ===");

        List<Car> carList = carService.getCars();

        System.out.println("----------------------------------------");
        System.out.println("*** Total average price is " + (int)carList.stream().mapToInt(Car::getPrice).average().getAsDouble()+ "$ ***");
        overalStopWatch.stop();
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        Map<Integer, List<Car>> collectByYear = carList.stream().collect(Collectors.groupingBy(Car::getYear));

        for (Map.Entry<Integer, List<Car>> entry : collectByYear.entrySet()) {
            int averagePrice = (int) entry.getValue().stream().mapToInt(Car::getPrice).average().getAsDouble();
            System.out.println("*** Year:" +entry.getKey() + ", number of ads: " + entry.getValue().size() + ", price:" + averagePrice+ "$ ***");
            int filteredPrice = (int) entry.getValue().stream()
                    .filter(car -> (car.getPrice() > averagePrice * 0.7) && (car.getPrice() < averagePrice * 1.3))
                    .mapToInt(Car::getPrice).average().getAsDouble();
            System.out.println("----------------------------------------");
            System.out.println("*** Year:" + entry.getKey() + ", number of ads: " + entry.getValue().size() + ", price:" + filteredPrice+ "$ FILTERED***");
        }

        System.out.println("----------------------------------------");
        carList.stream().mapToInt(Car::getPrice).sorted().forEach(c -> System.out.println(c));
        System.out.println("----------------------------------------");
        System.out.println("Total time:" + overalStopWatch.elapsed(TimeUnit.SECONDS) + " seconds");
    }

}
