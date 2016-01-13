package com.mobstac.anonspot;

/**
 * Created by aakash on 13/1/16.
 */
public class Message {
    private String name;
    private int gender;
    private String message;

    public Message() {
        //nothing
    }
    public Message(String name, int gender, String message) {
        this.name = name;
        this.gender = gender;
        this.message = message;
    }

    public String getName() {
        return name;
    }
    public int getGender() {
        return gender;
    }
    public String getMessage() {
        return message;
    }
}
