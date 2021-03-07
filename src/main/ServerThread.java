package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ServerThread extends Thread {

    private Socket clientSocket;
    private Controller controller;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket clientSocketScheduler;
    private ObjectInputStream objectInputStreamScheduler;
    private ObjectOutputStream objectOutputStreamScheduler;
    private ObserverNotification observerNotification;

    public ServerThread(Socket clientSocket, Socket clientSocketScheduler) throws IOException, ClassNotFoundException {
        this.clientSocket = clientSocket;
        this.clientSocketScheduler = clientSocketScheduler;
        observerNotification = new ObserverNotification(this);
        controller = new Controller();
        this.start();
        objectInputStreamScheduler = new ObjectInputStream(clientSocketScheduler.getInputStream());
        objectOutputStreamScheduler = new ObjectOutputStream(clientSocketScheduler.getOutputStream());

    }

    @Override
    public void run() {
        try {
            System.out.println("Соединение с сервером успешно установлено");
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            while (true) {
                while (objectInputStream.available() > 0) {
                    String t = objectInputStream.readUTF(); //0 - menu, 1 - id, 2 - date, 3 - time, 4 - type, 5 - text
                    String[] mainMas = t.split(" ");
                    switch (Integer.parseInt(mainMas[0])) {
                        case 1:
                            objectOutputStream.writeObject(controller.getListTasks());
                            objectOutputStream.flush();
                            break;
                        case 2:
                            Task task = new Task(controller.setNewId(), getLocalDataTime(t), mainMas[4], mainMas[5]);
                            controller.addTask(task);
                            break;
                        case 3:
                            controller.updateTask(new Task(controller.getTaskById(Integer.parseInt(mainMas[1])).getId(), getLocalDataTime(t), mainMas[4], mainMas[5]));
                            objectOutputStream.flush();
                            break;
                        case 4:
                            objectOutputStream.writeObject(controller.getTaskByDate(getLocalData(t)));
                            objectOutputStream.flush();
                            break;
                        case 5:
                            objectOutputStream.writeObject(controller.getTaskByQuery(getLocalData(t), mainMas[4]));
                            objectOutputStream.flush();
                            break;
                        case 6:
                            controller.deleteTaskById(controller.getTaskById(Integer.parseInt(mainMas[1])).getId());
                            break;
                        case 7:
                            controller.deleteTaskById(controller.getTaskById(Integer.parseInt(mainMas[1])).getId());
                            break;
                        case 8:
                            controller.exit();
                            break;
                        default:
                            System.out.println("Неверное число, повторите ввод");
                            break;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void isNotification(Task task) throws IOException {
        String sendTask = "Notification\n" + task.sendTask();
        objectOutputStreamScheduler.writeObject(sendTask);
        objectOutputStreamScheduler.flush();
    }

    public Controller getController(){
        return controller;
    }
    private LocalDateTime getLocalDataTime(String s) throws IOException {
        String[] mainMas = s.split(" ");//0 - menu, 1 - id, 2 - date, 3 - time, 4 - type, 5 - text
        String[] helpMas = mainMas[2].split("\\.");
        LocalDate localDate = LocalDate.of(Integer.parseInt(helpMas[2]), Integer.parseInt(helpMas[1]), Integer.parseInt(helpMas[0]));
        helpMas = mainMas[3].split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(helpMas[1]), Integer.parseInt(helpMas[0]));
        return LocalDateTime.of(localDate, localTime);
    }

    private LocalDate getLocalData(String s) throws IOException {
        String[] mainMas = s.split(" ");//0 - menu, 1 - id, 2 - date, 3 - time, 4 - type, 5 - text
        String[] helpMas = mainMas[2].split("\\.");
        return LocalDate.of(Integer.parseInt(helpMas[2]), Integer.parseInt(helpMas[1]), Integer.parseInt(helpMas[0]));

    }
}
