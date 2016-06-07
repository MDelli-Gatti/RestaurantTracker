package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    HashMap m = new HashMap();
                    if (username == null) {
                        return new ModelAndView(m, "login.html");
                    }
                    else {
                        User user = users.get(username);
                        m.put("restaurants", user.restaurants);

                        return new ModelAndView(m, "home.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    String name = request.queryParams("username");
                    String pass = request.queryParams("password");
                    if (name == null || pass == null){
                        throw new Exception("name or pass not set");
                    }

                    User user = users.get(name);
                    if (user == null){
                        user = new User(name, pass);
                        users.put(name, user);
                    }
                    else if (!pass.equals(user.password)){
                        throw new Exception("wrong password");
                    }

                    Session session = request.session();
                    session.attribute("username", name);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-restaurant",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null){
                        throw new Exception("not logged in");
                    }

                    String name = request.queryParams("name");
                    String location = request.queryParams("location");
                    int rating = Integer.valueOf(request.queryParams("rating"));
                    String comment = request.queryParams("comments");
                    if (name == null || location == null || comment == null){
                        throw new Exception("invalid form fields");
                    }

                    User user = users.get(username);
                    if (user == null){
                        throw new Exception("User does not exist");
                    }

                    Restaurant r = new Restaurant(name, location, rating, comment);
                    user.restaurants.add(r);

                    response.redirect("/");
                    return "";

                }
        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/delete-restaturant",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        throw new Exception("not logged in");
                    }
                    int id = Integer.valueOf(request.queryParams("id"));

                    User user = users.get(username);
                    if (id <= 0 || id - 1 >= user.restaurants.size() ) {
                        throw new Exception("invalid id");
                    }
                    user.restaurants.remove(id - 1);

                    response.redirect("/");
                    return "";
                }
        );
    }
}
