package com.auto.data;

/**
 * Created by vkiriushkin on 6/17/15.
 */
public class Car {

    private int carId;
    private int markId;
    private int modelId;
    private String version;
    private int year;
    private double engineVolume;
    private int price;

    private Car(Builder builder) {
        carId = builder.carId;
        markId = builder.markId;
        modelId = builder.modelId;
        version = builder.version;
        year = builder.year;
        engineVolume = builder.engineVolume;
        price = builder.price;
    }


    public static final class Builder {
        private int carId;
        private int markId;
        private int modelId;
        private String version;
        private int year;
        private double engineVolume;
        private int price;

        public Builder() {
        }

        public Builder carId(int carId) {
            this.carId = carId;
            return this;
        }

        public Builder markId(int markId) {
            this.markId = markId;
            return this;
        }

        public Builder modelId(int modelId) {
            this.modelId = modelId;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder engineVolume(double engineVolume) {
            this.engineVolume = engineVolume;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }

    public int getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", markId=" + markId +
                ", modelId=" + modelId +
                ", version='" + version + '\'' +
                ", year=" + year +
                ", engineVolume=" + engineVolume +
                ", price=" + price +
                '}';
    }
}
