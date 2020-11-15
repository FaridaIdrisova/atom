package ru.atom.chat.server;

public class ChatMessage {
    private String Message;
    private String Sender;
    private String Recipient;

    public ChatMessage(String message, String sender, String recipient) {
        this.Message = message;
        this.Sender = sender;
        this.Recipient = recipient;
    }

    public String getMessage() {
        return this.Message;
    }

    public String getSender() {
        return this.Sender;
    }

    public String getRecipient() {
        return this.Recipient;
    }
}
