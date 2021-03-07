package main.Interface;

import main.Task;

import java.io.IOException;

public interface IObservable {
    void addObserver(IObserver o);

    void removeObserver(IObserver o);

    void notifyObservers(Task task) throws IOException;
}