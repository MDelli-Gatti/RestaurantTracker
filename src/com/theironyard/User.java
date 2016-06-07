package com.theironyard;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by michaeldelli-gatti on 6/7/16.
 */
public class User {
    String name;
    String password;
    AbstractList<Restaurant> restaurants = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
