package com.auto.load.db;

import com.auto.data.Car;
import com.auto.data.Criterion;
import com.auto.data.Mark;
import com.auto.persistence.AbstractJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarDbManager {

    private static CarDbManager dbManager;
    private final AbstractJDBCTemplate jdbcTemplate;

    private static final String INSERT_CAR = "INSERT INTO AUTOSTAT.CAR " +
            "(ID,MARKID,MODELID,VERSION,YEAR,ENGINEVOLUME,PRICE)" +
            " VALUES(?,?,?,?,?,?,?);";

    private static final String SELECT_CAR_BY_ID = "SELECT * FROM AUTOSTAT.CAR WHERE ID=?";
    private static final String SELECT_ALL_CARS = "SELECT * FROM AUTOSTAT.CAR";


    private CarDbManager() {
        //TODO:change to spring injection
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        jdbcTemplate = (AbstractJDBCTemplate)context.getBean("autoJDBCTemplate");
    }

    public static CarDbManager getInstance() {
        if (dbManager == null)
            dbManager = new CarDbManager();

        return dbManager;
    }

    public void saveCar(Car car) {
        jdbcTemplate.getJdbcTemplate().update(INSERT_CAR,
                car.getCarId(),
                car.getMarkId(),
                car.getModelId(),
                car.getVersion(),
                car.getYear(),
                car.getEngineVolume(),
                car.getPrice());
    }

    public Car getCarById(int carId) {
        return (Car) jdbcTemplate.getJdbcTemplate().queryForObject(SELECT_CAR_BY_ID, new BeanPropertyRowMapper(Car.class), carId);
    }

    public List<Car> getCars(Criterion criterion) {
        //TODO: check what fields of criterion are present and filter
        List<Car> cars  = jdbcTemplate.getJdbcTemplate().query(SELECT_ALL_CARS, new BeanPropertyRowMapper(Car.class));
        cars.stream().filter(car -> car.getMarkId() == criterion.getMakrId()
                                && car.getModelId() == criterion.getModelId()).collect(Collectors.toList());
        return cars;
    }
}
