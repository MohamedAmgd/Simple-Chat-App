package com.mohamedamgd.chatapp.models;

/**
 * Copyright 2020 Mohamed Amgd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
