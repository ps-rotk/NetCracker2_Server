package main.Interface;

import main.Task;

public interface IObservable {
    void addObserver(IObserver o);

    void removeObserver(IObserver o);

    void notifyObservers(Task task);
}