package com.github.Frenadol.model;

import java.time.LocalDateTime;

public class Message {
    private User sender;
    private User receiver;
    private String content;
    private LocalDateTime timestamp;

    public Message(User sender, User receiver, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}