package ru.atom.chat.server;

public class ChatMessage {
    private String message;
    private String sender;
    private String recipient;

    public ChatMessage(String message, String sender, String recipient) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getMessage() {
        return this.message;
    }

    public String getSender() {
        return this.sender;
    }

    public String getRecipient() {
        return this.recipient;
    }
}
