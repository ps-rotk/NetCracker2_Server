package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(1024);
            System.out.println("Запущен");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("подключение");
                clientSocket.setSoTimeout(600000);
                Socket clientSocketScheduler = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket, clientSocketScheduler);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Не получилось прослушать порт");
            System.exit(-1);
        }
    }
}
