package com.mobstac.anonspot;

/**
 * Created by aakash on 13/1/16.
 */
public class User {
    public String name;
    public String gender;

    public User() {
        //nothing
    }
    public User(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getName() {
        return this.name;
    }
    public Strin getGender() {
        return this.gender;
    }
}
