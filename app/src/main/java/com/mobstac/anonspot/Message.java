package com.mobstac.anonspot;

/**
 * Created by aakash on 13/1/16.
 */
public class Message {
    private User user;
    private String message;

    public Message() {
        //nothing
    }
    public Message(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getUser() {
        return this.user.toString();
    }
    public String getMessage() {
        return message;
    }
}
