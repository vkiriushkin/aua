package com.auto.data;

/**
 * Created by vkiriushkin on 6/15/15.
 */
public class Model {

    private String name;
    private int value;
    private int count;

    public Model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", count=" + count +
                '}';
    }
}
