package ru.atom.chat.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Controller
@RequestMapping("chat")
public class ChatController {
    private ArrayList<ChatMessage> privateMessages = new ArrayList<ChatMessage>();
    private Queue<String> messages = new ConcurrentLinkedQueue<>();
    private Map<String, String> registeredUsers = new ConcurrentHashMap<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();

    /**
     * curl -X POST -i localhost:8080/chat/signup -d "name=NAME&password=PASSWORD"
     */
    @RequestMapping(
            path = "signup",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> signup(@RequestParam("name") String name,
                                         @RequestParam("password") String password) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name.");
        }

        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name.");
        }

        if (password.length() < 8) {
            return ResponseEntity.badRequest().body("Too short password.");
        }

        if (registeredUsers.containsKey(name)) {
            return ResponseEntity.badRequest().body("This user already exists");
        }

        registeredUsers.put(name, getSha256(password));
        messages.add("[" + name + "] registered! Welcome to our chat!");

        return ResponseEntity.ok().build();
    }

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=NAME&password=PASSWORD"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name,
                                        @RequestParam("password") String password) {
        if (!registeredUsers.containsKey(name)) {
            return ResponseEntity.badRequest().body("This user doesn't exists");
        }

        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in.");
        }

        usersOnline.put(name, getSha256(password));
        messages.add("[" + name + "] logged in");

        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        String responseBody = String.join("\n", usersOnline.keySet().stream()
                .sorted().collect(Collectors.toList()));

        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -i localhost:8080/chat/allusers
     */
    @RequestMapping(
            path = "allusers",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity allUsers() {
        String responseBody = String.join("\n", registeredUsers.keySet().stream()
                .sorted().collect(Collectors.toList()));

        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=NAME&password=PASSWORD"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(@RequestParam("name") String name,
                                         @RequestParam("password") String password) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged out.");
        }

        if (getSha256(password).equals(usersOnline.get(name))) {
            usersOnline.remove(name);
            messages.add("[" + name + "] logged out.");
        } else {
            return ResponseEntity.badRequest().body("Wrong password.");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=NAME&password=PASSWORD&msg=MESSAGE"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> say(@RequestParam("name") String name,
                                      @RequestParam("password") String password,
                                      @RequestParam("msg") String msg) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("User logged out.");
        }

        if (getSha256(password).equals(usersOnline.get(name))) {
            messages.add("[" + name + "]: " + msg);
        } else {
            return ResponseEntity.badRequest().body("Wrong password.");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity chat() {
        String responseBody = String.join("\n", messages);

        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/sendMessageTo -d
     * "username=USERNAME&password=PASSWORD&message=MESSAGE&recipient=RECIPIENT"
     */
    @RequestMapping(
            path = "sendMessageTo",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity sendMessageTo(@RequestParam("username") String username,
                                        @RequestParam("password") String password,
                                        @RequestParam("message") String message,
                                        @RequestParam("recipient") String recipient) {
        String validationErrorMessage = validateUser(username, password, true);
        if (!validationErrorMessage.equals("")) {
            return ResponseEntity.badRequest().body(validationErrorMessage);
        }

        if (!this.registeredUsers.containsKey(recipient)) {
            return ResponseEntity.badRequest().body("Recipient is not found.");
        }

        ChatMessage msg = new ChatMessage(message, username, recipient);
        this.privateMessages.add(msg);

        return ResponseEntity.ok().build();
    }

    /**
     * curl -X POST -i localhost:8080/chat/getPrivateMessages -d "username=USERNAME&password=PASSWORD"
     */
    @RequestMapping(
            path = "getPrivateMessages",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity getPrivateMessages(@RequestParam("username") String username,
                                             @RequestParam("password") String password) {
        String validationErrorMessage = this.validateUser(username, password, true);
        if (!validationErrorMessage.equals("")) {
            return ResponseEntity.badRequest().body(validationErrorMessage);
        }

        String responseString = "";
        for (int i = 0; i < this.privateMessages.size(); i++) {
            if (this.privateMessages.get(i).getRecipient().equals(username)) {
                responseString += this.formatPrivateMessage(this.privateMessages.get(i));
            }
        }

        return ResponseEntity.ok(responseString);
    }

    private String getSha256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(str.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String validateUser(String userName, String password, Boolean isOnline) {

        if (isOnline && !usersOnline.containsKey(userName)) {
            return "User logged out.";
        }

        if (!isOnline && !registeredUsers.containsKey(userName)) {
            return "User doesn't exist.";
        }

        if (!getSha256(password).equals(registeredUsers.get(userName))) {
            return "Wrong password.";
        }

        return "";
    }

    private String formatPrivateMessage(ChatMessage message) {
        return "[" + message.getSender() + "]: " + message.getMessage() + "\n";
    }
}