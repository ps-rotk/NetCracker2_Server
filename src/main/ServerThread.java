package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class ServerThread extends Thread {

    Socket clientSocket;
    Controller controller;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    Socket clientSocketScheduler;
    ObjectInputStream objectInputStreamScheduler;
    ObjectOutputStream objectOutputStreamScheduler;
    ObserverNotification observerNotification;

    public ServerThread(Socket clientSocket, Socket clientSocketScheduler, Controller controller) throws IOException, ClassNotFoundException {
        this.clientSocketScheduler = clientSocketScheduler;
        this.clientSocket = clientSocket;
        this.controller = controller;
        objectInputStreamScheduler = new ObjectInputStream(this.clientSocketScheduler.getInputStream());
        objectOutputStreamScheduler = new ObjectOutputStream(this.clientSocketScheduler.getOutputStream());
        objectOutputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(this.clientSocket.getInputStream());
        observerNotification = new ObserverNotification(this);
        this.start();
    }

    @Override
    public void run() {
        try {
            ArrayList<Task> arrayList = null;
            System.out.println("Соединение с сервером успешно установлено");
            while (true) {
                try {
                    String t = (String) objectInputStream.readObject();
                    String[] mainMas = t.split("\n");
                    switch (mainMas[0]) {
                        case "1":
                            System.out.println("Принято сообщение о получении списка задач");
                            objectOutputStream.writeObject(getStringFromList(controller.getListTasks()));
                            objectOutputStream.flush();
                            System.out.println("Выполнено");
                            break;
                        case "2":
                            Task task = new Task(controller.setNewId(), getLocalDataTime(mainMas[1], mainMas[2]), mainMas[3], mainMas[4]);
                            System.out.println("Принято сообщение о добавлении задачи " + task.toString());
                            controller.addTask(task);
                            objectOutputStream.writeObject("Задача добавлена");
                            objectOutputStream.flush();
                            System.out.println("Выполнено");
                            break;
                        case "3":
                            System.out.println("Принято сообщение об обновлении задачи");
                            controller.updateTask(new Task(controller.getTaskById(Integer.parseInt(mainMas[1])).getId(), getLocalDataTime(mainMas[2], mainMas[3]), mainMas[4], mainMas[5]));
                            objectOutputStream.flush();
                            objectOutputStream.writeObject("Задача обновлена");
                            objectOutputStream.flush();
                            System.out.println("Выполнено");
                            break;
                        case "4":
                            System.out.println("Принято сообщение о получении списка задач по дате");
                            objectOutputStream.writeObject(getStringFromList(controller.getTaskByDate(getLocalData(mainMas[1]))));
                            objectOutputStream.flush();
                            System.out.println("Выполнено");
                            break;
                        case "5":
                            System.out.println("Принято сообщение о получении списка задач по дате и типу");
                            objectOutputStream.writeObject(getStringFromList(controller.getTaskByQuery(getLocalData(mainMas[1]), mainMas[2])));
                            objectOutputStream.flush();
                            System.out.println("Выполнено");
                            break;
                        case "6":
                            System.out.println("Принято сообщение об удалении задачи");
                            controller.deleteTaskById(controller.getTaskById(Integer.parseInt(mainMas[1])).getId());
                            objectOutputStream.writeObject("Задача удалена");
                            objectOutputStream.flush();
                            System.out.println("Выполнено");
                            break;

                        case "7":
                            System.out.println("Принято сообщение о получении списка  устаревших и выполненых задач"); //TODO: не работает
                            arrayList = controller.checkOldTask();
                            objectOutputStream.writeObject(getStringFromList(arrayList));
                            objectOutputStream.flush();
                            System.out.println("Выполнено");
                            break;
                        case "delete":
                            System.out.println("Удаление");
                            for (Task task1 : arrayList) {
                                controller.deleteTaskById(task1.getId());
                            }
                            System.out.println("Выполнено");
                            break;
                        case "8":
                            System.out.println("Сеанс оборван");
                            controller.exit();
                            clientSocket.close();
                            clientSocketScheduler.close();
                            this.interrupt();
                            return;
                        default:
                            System.out.println("Неверное число, повторите ввод");
                            break;

                    }
                } catch (IOException e) {
                    System.out.println("Сеанс оборван");
                    clientSocket.close();
                    clientSocketScheduler.close();
                    this.interrupt();
                    return;
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void isNotification(Task task) throws IOException {
        String sendTask = task.toString();
        objectOutputStreamScheduler.writeObject(sendTask);
        objectOutputStreamScheduler.flush();
    }

    public Controller getController() {
        return controller;
    }

    private LocalDateTime getLocalDataTime(String date, String time) throws IOException {
        String[] helpMas = date.split("\\.");
        LocalDate localDate = LocalDate.of(Integer.parseInt(helpMas[2]), Integer.parseInt(helpMas[1]), Integer.parseInt(helpMas[0]));
        helpMas = time.split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(helpMas[0]), Integer.parseInt(helpMas[1]));
        return LocalDateTime.of(localDate, localTime);
    }

    private LocalDate getLocalData(String date) throws IOException {
        String[] helpMas = date.split("\\.");
        return LocalDate.of(Integer.parseInt(helpMas[2]), Integer.parseInt(helpMas[1]), Integer.parseInt(helpMas[0]));
    }

    private String getStringFromList(ArrayList<Task> listTask) {
        StringBuilder s = new StringBuilder();
        if (listTask != null) {
            for (Task task : listTask) {
                s.append(task.toString());
                s.append("\n");
            }
        }
        return s.toString();
    }
}