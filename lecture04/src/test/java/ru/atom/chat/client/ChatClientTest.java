package ru.atom.chat.client;

import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.chat.server.ChatApplication;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatClientTest {
    private static String USER_NAME_1 = "Van Darkholme";
    private static String USER_1_PASSWORD = "dungeon master";

    private static String USER_NAME_2 = "Billy Harrington";
    private static String USER_2_PASSWORD = "Nico-Nico";
    private static String MESSAGE_TO_VAN = "I am boss of the gym!";

    private static String TEST_NAME_IN_CHAT = "Test_name";
    private static String TEST_PASSWORD_IN_CHAT = "Test_password";

    @Test
    public void signup() throws IOException {
        Response response = ChatClient.signup(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        System.out.println();
        Assert.assertTrue(response.code() == 200
                || body.equals("This user already exists"));
    }

    @Test
    public void login() throws IOException {
        ChatClient.signup(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT);
        Response response = ChatClient.login(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        System.out.println();
        Assert.assertTrue(response.code() == 200
                || body.equals("This user doesn't exists")
                || body.equals("Already logged in."));
    }

    @Test
    public void viewOnline() throws IOException {
        Response response = ChatClient.viewOnline();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void viewAllUsers() throws IOException {
        Response response = ChatClient.viewAllUsers();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void logout() throws IOException {
        ChatClient.signup(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT);
        ChatClient.login(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT);
        Response response = ChatClient.logout(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void say() throws IOException {
        ChatClient.signup(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT);
        ChatClient.login(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT);
        Response response = ChatClient.say(TEST_NAME_IN_CHAT, TEST_PASSWORD_IN_CHAT, MESSAGE_TO_VAN);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void viewChat() throws IOException {
        Response response = ChatClient.viewChat();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void sendPrivateMessageTest() throws IOException {
        ChatClient.signup(USER_NAME_2, USER_2_PASSWORD);
        ChatClient.login(USER_NAME_2, USER_2_PASSWORD);

        ChatClient.signup(USER_NAME_1, USER_1_PASSWORD);
        ChatClient.login(USER_NAME_1, USER_1_PASSWORD);

        Response response = ChatClient.sendPrivateMessage(USER_NAME_2, USER_2_PASSWORD, MESSAGE_TO_VAN, USER_NAME_1);

        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());

        Response newResponse = ChatClient.getPrivateMessages(USER_NAME_1, USER_1_PASSWORD);
        String actualResult = newResponse.body().string();
        System.out.println(actualResult);

        String expectedResult = "[" + USER_NAME_2 + "]: " + MESSAGE_TO_VAN + "\n";
        Assert.assertEquals(expectedResult, actualResult);
    }
}