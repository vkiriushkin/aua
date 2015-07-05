package com.auto;

import com.auto.data.Car;
import com.auto.data.Criterion;
import com.auto.data.Model;
import com.auto.data.Mark;
import com.auto.request.CarByIdRequest;
import com.auto.request.IDsRequest;
import com.auto.request.ModelsRequest;
import com.auto.request.MarksRequest;
import com.auto.service.CarService;
import com.auto.service.MarkService;
import com.auto.service.ModelService;
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
        int fromYear = 2005;
        int toYear = 2011;

        Gson gson = new Gson();

        Stopwatch overalStopWatch = Stopwatch.createStarted();
        System.out.println("----------------------------------------");
        System.out.println("*** Getting marks ***");

        MarkService markService = new MarkService();
//        List<Mark> marksList = markService.loadAllMarks();
        List<Mark> marksList = markService.getAllMarks();

        System.out.println("----------------------------------------");
        System.out.println("=== Found total marks: " + marksList.size() + " ===");

        System.out.println("----------------------------------------");
        System.out.println("*** Getting models by mark ***");

        //filter list of models based on required
        Stream<Mark> marksStream = marksList.stream().filter(mark -> mark.getName().equalsIgnoreCase(requiredMark));
        int markId = marksStream.findFirst().get().getId();

        ModelService modelService = new ModelService();
//        List<Model> modelsList = modelService.getModelsByMarkId(markId);
        List<Model> modelsList = modelService.loadAllModels(markId);

        System.out.println("----------------------------------------");
        System.out.println("=== Found total models: " + modelsList.size() + " ===");

        System.out.println("----------------------------------------");
        System.out.println("*** Getting total number of ids by mark and model ***");

        //filter list of marks based on required
        Stream<Model> modelsStream = modelsList.stream().filter(model -> model.getName().equalsIgnoreCase(requiredModel));
        System.out.println("----------------------------------------");
        Model model = modelsStream.findFirst().get();

        Criterion criterion = new Criterion.Builder().makrId(markId).modelId(model.getId()).fromYear(fromYear).toYear(toYear).build();
        CarService carService = new CarService();
        System.out.println("=== Found total ads: " + carService.loadCount(criterion) + " ===");

        List<Car> carList = carService.loadCars(criterion);

        System.out.println("----------------------------------------");
        System.out.println("*** Saving cars to DB ***");
        carList.forEach(carService::saveCar);

        System.out.println("----------------------------------------");
        System.out.println("*** Total average price is " + (int)carList.stream().mapToInt(Car::getPrice).average().getAsDouble()+ "$ ***");
        overalStopWatch.stop();
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        Map<Integer, List<Car>> collectByYear = carList.stream().collect(Collectors.groupingBy(Car::getYear));

        for (Map.Entry<Integer, List<Car>> entry : collectByYear.entrySet()) {
            int averagePrice = (int) entry.getValue().stream().filter(car -> car.getPrice() > 0).mapToInt(Car::getPrice).average().getAsDouble();
            System.out.println("*** Year:" +entry.getKey() + ", number of ads: " + entry.getValue().size() + ", price:" + averagePrice+ "$ ***");
//            int filteredPrice = (int) entry.getValue().stream()
//                    .filter(car -> (car.getPrice() > averagePrice * 0.7) && (car.getPrice() < averagePrice * 1.3))
//                    .mapToInt(Car::getPrice).average().getAsDouble();
//            System.out.println("----------------------------------------");
//            System.out.println("*** Year:" + entry.getKey() + ", number of ads: " + entry.getValue().size() + ", price:" + filteredPrice+ "$ FILTERED***");
        }
//
//        System.out.println("----------------------------------------");
//        carList.stream().mapToInt(Car::getPrice).sorted().forEach(c -> System.out.println(c));
        System.out.println("----------------------------------------");
        System.out.println("Total time:" + overalStopWatch.elapsed(TimeUnit.SECONDS) + " seconds");
    }

}
