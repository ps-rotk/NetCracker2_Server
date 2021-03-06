package main;


import main.Interface.IObservable;
import main.Interface.IObserver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Scheduler extends Thread implements IObservable {
    private List<Task> list;
    private List<IObserver> observers;
    private boolean stop;

    public Scheduler(ArrayList<Task> list) {
        this.list = list;
        stop = false;
        observers = new ArrayList<>();
        this.start();
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void setList(ArrayList<Task> list) {
        this.list = list;
        this.interrupt();
    }

    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Task task) throws IOException {
        for (IObserver observer : observers)
            observer.update(task);
    }

    @Override
    public void run() {
        System.out.println("Scheduler запущен");
        while (!stop) {
            try {
                sleep(2);
            } catch (InterruptedException e) {
                continue;
            }
            if (list.size() != 0) {
                for (Task task : list) {
                    if (!task.getPerformed()) {
                        long time = ChronoUnit.MILLIS.between(LocalDateTime.now(), task.getDate());
                        try {
                            System.out.println("Заснул на " + time + " до " + task.getDate());
                            sleep(time);
                            System.out.println("Задача наступила: " + task.toString());
                            notifyObservers(task);
                            try {
                                sleep(20);
                            } catch (InterruptedException e) {
                                continue;
                            }

                        } catch (InterruptedException | IOException e) {
                            System.out.println("Обновление списка в Scheduler");
                            break;
                        }
                    }
                }
            }

        }
    }
}