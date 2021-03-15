package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server() {
        try {
            Controller controller = new Controller();
            ServerSocket serverSocket = new ServerSocket(1024);
            ServerSocket serverSocketScheduler = new ServerSocket(1025);
            System.out.println("Запущен");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(10000000);
                Socket clientSocketScheduler = serverSocketScheduler.accept();
                clientSocket.setSoTimeout(10000000);
                ServerThread serverThread = new ServerThread(clientSocket, clientSocketScheduler, controller);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Не получилось прослушать порт");
        }
    }
}
