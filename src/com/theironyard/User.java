package com.theironyard;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by michaeldelli-gatti on 6/7/16.
 */
public class User {
    int id;
    String name;
    String password;

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
