package main;

import main.Interface.IObserver;

import java.io.IOException;

public class ObserverNotification implements IObserver {
    private Server server;

    public ObserverNotification(Server server, Controller controller) {
        this.server = server;
        controller.addObserver(this);
    }

    @Override
    public void update(Task task) throws IOException {
        server.isNotification(task);
    }
}
