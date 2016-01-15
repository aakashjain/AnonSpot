package com.mobstac.anonspot.models;

/**
 * Created by aakash on 14/1/16.
 */
public class User {

    private String name;
    private String gender;

    private User() {}

    public User(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }
}
