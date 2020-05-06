package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Input your login: ");
            writer.println(scanner.nextLine());
            for (; ; ) {
                writer.println(scanner.nextLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
