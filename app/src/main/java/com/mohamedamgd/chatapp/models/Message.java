package com.mohamedamgd.chatapp.models;

public class Message {
    private long sentIn;
    private String userId, author, body;

    public Message() {
    }

    public Message(String userId, String author, String body, long sentIn) {
        this.userId = userId;
        this.author = author;
        this.body = body;
        this.sentIn = sentIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getSentIn() {
        return sentIn;
    }

    public void setSentIn(long sentIn) {
        this.sentIn = sentIn;
    }
}
