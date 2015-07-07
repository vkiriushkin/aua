package com.auto.service;

import com.auto.data.Car;
import com.auto.data.Criterion;
import com.auto.load.db.CarDbManager;
import com.auto.load.web.CarWebLoader;

import java.io.IOException;
import java.util.List;

public class CarService {

    private CarDbManager dbManager;
    private CarWebLoader webLoader;

    public CarService() {
        initLoaders();
    }

    public void initLoaders() {
        dbManager = CarDbManager.getInstance();
        webLoader = CarWebLoader.getInstance();
    }

    public int loadCount(Criterion criterion) throws IOException {
        return webLoader.loadCount(criterion);
    }

    public List<Car> loadCars(Criterion criterion) throws IOException {
        return webLoader.loadCars(criterion);
    }

    public Car loadCarById(long carId) throws IOException {
        return webLoader.loadCarById(carId);
    }

    public void saveCar(Car car) {
        dbManager.saveCar(car);
    }

    public Car getCarById(int carId) {
        return dbManager.getCarById(carId);
    }

    public List<Car> getCars(Criterion criterion) {
        return dbManager.getCars(criterion);
    }

}
