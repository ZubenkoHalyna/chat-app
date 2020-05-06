package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class UserThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private BufferedReader reader;

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        String login = null;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            login = receiveMessage();
            printUsers();
            server.login(login, this);
            for (; ; ) {
                String command = receiveMessage();
                String[] commandParts = command.split(":");
                if (commandParts.length != 2) {
                    sendMessage("Illegal command: " + command);
                    continue;
                }
                String targetUser = commandParts[0].trim();
                String content = commandParts[1].trim();
                if (!server.sendMessage(targetUser, login + ": " + content)) {
                    sendMessage("User " + targetUser + " isn't connected");
                }
            }
        } catch (IOException e) {
            server.removeUser(login);
        }
    }

    public void sendMessage(String msg) {
        writer.println(msg);
    }

    public void printUsers() {
        Set<String> users = server.getLoggedUsers();
        if (users.size() == 0) {
            writer.println("No other users connected");
        } else {
            writer.println("Connected users:");
            users.forEach(writer::println);
        }
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }
}
