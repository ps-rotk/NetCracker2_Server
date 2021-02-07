package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;

    public Server() {
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(1024);
        } catch (IOException e) {
            System.out.println("Не получилось прослушать порт: 1024");
            System.exit(-1);
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                try {
                    ServerThread serverThread = new ServerThread(clientSocket);
                } finally {
                    clientSocket.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка подключения к 1024 порту");
                System.exit(-1);
            }
        }
    }
}
