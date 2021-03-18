package main;

import main.Interface.IObservable;
import main.Interface.IObserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Server {

    Connection lastConnection;
    Controller controller;
    ObserverNotification observerNotification;
    ArrayList<ServerThread> listConnection;
    public Server() {
        try {
            lastConnection = new Connection();
            controller = new Controller();
            listConnection = new ArrayList<>();
            observerNotification = new ObserverNotification(this, controller);
            System.out.println("Запущен");
            while (true) {
                clientManager();
                ServerSocket serverSocket = new ServerSocket(lastConnection.getClientConnection());
                ServerSocket serverSocketScheduler = new ServerSocket(lastConnection.getSchedulerConnection());
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(25000);
                Socket clientSocketScheduler = serverSocketScheduler.accept();
                clientSocketScheduler.setSoTimeout(25000);
                ServerThread serverThread = new ServerThread(clientSocket, clientSocketScheduler, controller);
                listConnection.add(serverThread);
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


    public void isNotification(Task task) throws IOException {
        System.out.println("Размер листа "+listConnection.size());
        for(ServerThread serverThread : listConnection){
            try{ serverThread.isNotification(task);}
            catch (IOException e){
                continue;
            }
        }
    }
}
