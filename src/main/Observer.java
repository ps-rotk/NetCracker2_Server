package main;

import main.Interface.IObserver;

import java.io.IOException;

public class Observer implements IObserver {
    private Controller controller;


    public Observer(Controller controller){
        this.controller = controller;
        controller.getScheduler().addObserver(this);
    }
    @Override
    public void update(Task task) throws IOException {
        controller.isNotification(task.getId(), true);
    }

}
