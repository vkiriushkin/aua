package com.auto.service;

import com.auto.load.db.UserManager;

public class UserService {

    private UserManager userManager;

    public UserService() {
        userManager = UserManager.getInstance();
    }

    public long createUser(String userName, String password) {
        return userManager.createUser(userName, password);
    }

    public long isRegistered(String userName, String password) {
        return userManager.getUserId(userName, password);
    }

    public static void main(String[] args) {
        UserService service = new UserService();
//        service.createUser("test","test1");
        System.out.println(service.createUser("test2", "test3"));
    }
}
