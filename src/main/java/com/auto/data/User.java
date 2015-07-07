package com.auto.data;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final long id;
    private final String userName;
    private final String password;
    private final List<Subscription> subscriptionList;

    public User(long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        subscriptionList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void addSubscription(Subscription subscription) {
        subscriptionList.add(subscription);
    }

    public void removeSubscription(Subscription subscription) {
        subscriptionList.remove(subscription);
    }

    public void clearSubscriptions() {
        subscriptionList.clear();
    }
}
