package com.mobstac.anonspot;

/**
 * Created by aakash on 14/1/16.
 */
public class ChatMessage {
    private String name;
    private String message;

    public ChatMessage() {}
    public ChatMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }
    public String getMessage() {
        return message;
    }
}
