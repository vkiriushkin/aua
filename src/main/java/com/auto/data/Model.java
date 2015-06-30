package com.auto.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vkiriushkin on 6/15/15.
 */
public class Model {

    @SerializedName("value")
    private int id;
    private String name;
    private int count;

    public Model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
