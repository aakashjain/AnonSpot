package com.mobstac.anonspot.models;

/**
 * Created by aakash on 14/1/16.
 */
public class ChatMessage {
    private String gender;
    private String name;
    private String message;

    public ChatMessage() {}
    public ChatMessage(String gender, String name, String message) {
        this.gender = gender;
        this.name = name;
        this.message = message;
    }

    public String getGender() {
        return gender;
    }
    public String getName() {
        return name;
    }
    public String getMessage() {
        return message;
    }
}
