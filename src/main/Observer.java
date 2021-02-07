package main;

import main.Interface.IObserver;

public class Observer implements IObserver {
    private Controller controller;


    public Observer(Controller controller){
        this.controller = controller;
        controller.getScheduler().addObserver(this);
    }
    @Override
    public void update(Task task) {
        controller.setPerformed(task.getId(), true);
    }
}
