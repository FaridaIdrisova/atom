package ru.atom.chat.client;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.io.IOException;


public class ChatClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    //POST host:port/chat/signup?name=my_name&password=my_password
    public static Response signup(String name, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/signup?name=" + name + "&password=" + password)
                .build();

        return client.newCall(request).execute();
    }

    //POST host:port/chat/login?name=my_name&password=my_password
    public static Response login(String name, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/login?name=" + name + "&password=" + password)
                .build();

        return client.newCall(request).execute();
    }

    //GET host:port/chat/online
    public static Response viewOnline() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/online")
                .addHeader("host", HOST + PORT)
                .build();

        return client.newCall(request).execute();
    }

    //GET host:port/chat/allusers
    public static Response viewAllUsers() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/allusers")
                .addHeader("host", HOST + PORT)
                .build();

        return client.newCall(request).execute();
    }

    //POST host:port/chat/logout?name=my_name&password=my_password
    public static Response logout(String name, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/logout?name=" + name + "&password=" + password)
                .build();

        return client.newCall(request).execute();
    }

    //POST host:port/chat/say?name=my_name&password=my_password
    //Body: "msg='my_message'"
    public static Response say(String name, String password, String message) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/say?name=" + name + "&password=" + password + "&msg=" + message)
                .build();

        return client.newCall(request).execute();
    }

    //GET host:port/chat/chat
    public static Response viewChat() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/chat")
                .addHeader("host", HOST + PORT)
                .build();
        return client.newCall(request).execute();
    }

    public static Response sendPrivateMessage(String username, String password, String message, String recipient)
            throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/sendMessageTo?username=" + username +
                        "&password=" + password + "&message=" + message + "&recipient=" + recipient)
                .build();

        return client.newCall(request).execute();
    }

    public static Response getPrivateMessages(String username, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/getPrivateMessages?username=" + username +
                        "&password=" + password)
                .build();

        return client.newCall(request).execute();
    }
}