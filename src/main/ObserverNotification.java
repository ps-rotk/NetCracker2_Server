package main;

import main.Interface.IObserver;

import java.io.IOException;

public class ObserverNotification implements IObserver {
    private ServerThread serverThread;

    public ObserverNotification(ServerThread serverThread) {
        this.serverThread = serverThread;
        serverThread.getController().addObserver(this);
    }

    @Override
    public void update(Task task) throws IOException {
        serverThread.isNotification(task);
    }
}
