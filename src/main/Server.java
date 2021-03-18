package main;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    Connection lastConnection;

    public Server() {
        try {
            lastConnection = new Connection();
            Controller controller = new Controller();
            System.out.println("Запущен");
            while (true) {
                clientManager();
                ServerSocket serverSocket = new ServerSocket(lastConnection.getClientConnection());
                ServerSocket serverSocketScheduler = new ServerSocket(lastConnection.getSchedulerConnection());
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(15000);
                Socket clientSocketScheduler = serverSocketScheduler.accept();
                clientSocket.setSoTimeout(15000);
                ServerThread serverThread = new ServerThread(clientSocket, clientSocketScheduler, controller);
                serverThread.begin();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Не получилось прослушать порт");
        }
    }

    void clientManager() throws IOException {
        Connection tempConnection = new Connection();
        ServerSocket getClientConnection = new ServerSocket(1024);
        Socket clientSocket = getClientConnection.accept();
        System.out.println("Клиент подключился к порту 1024 для получения портов подключения");
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        tempConnection.setClientConnection(lastConnection.getClientConnection() + 2);
        tempConnection.setSchedulerConnection(lastConnection.getSchedulerConnection() + 2);
        oos.writeObject(tempConnection);
        lastConnection = tempConnection;
        getClientConnection.close();
        clientSocket.close();
        System.out.println("Были отправлены порты: " + lastConnection.getClientConnection() + " " + lastConnection.getSchedulerConnection());
    }
}
