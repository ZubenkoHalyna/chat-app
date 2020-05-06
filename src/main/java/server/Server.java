package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Server {
    public static final int PORT = 8082;
    private static Map<String, UserThread> loggedUsers;

    public void start() {
        loggedUsers = new HashMap<>();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            for (;;) {
                Socket socket = serverSocket.accept();
                System.out.println("Guest user connected");
                new UserThread(socket, this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getLoggedUsers() {
        return loggedUsers.keySet();
    }

    public void removeUser(String login) {
        loggedUsers.remove(login);
        for (UserThread th : loggedUsers.values()) {
            th.sendMessage("User logout: " + login);
        }
    }

    public void login(String login, UserThread userThread) {
        String msg = "User login: " + login;
        System.out.println(msg);
        for (UserThread th : loggedUsers.values()) {
            th.sendMessage(msg);
        }
        loggedUsers.put(login, userThread);
    }

    public void sendMessage(String login, String str) {
        loggedUsers.get(login).sendMessage(str);
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
