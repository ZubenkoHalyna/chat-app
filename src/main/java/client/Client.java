package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final int PORT = 8082;

    public void start(String hostname, int port) {
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("You successfully connected to the server");
            new ReadThread(socket).start();
            new WriteThread(socket).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.print("Input server address: ");
        new Client().start(new Scanner(System.in).nextLine(), PORT);
    }
}
