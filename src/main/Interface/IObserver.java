package main.Interface;

import main.Task;

import java.io.IOException;

public interface IObserver {
    void update(Task task) throws IOException;
}
