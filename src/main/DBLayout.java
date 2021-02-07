package main;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DBLayout implements Serializable {
    private Map<Integer, Task> mapTask;

    ///////////
    //Заполнение конструктора
    private Map<Integer, Task> getAllTasksConstr() throws IOException, ClassNotFoundException {

        File checkExist = new File("Tasks.dat");
        if (checkExist.exists()) {
            if (checkExist.length() == 0) {
                System.out.println("Файл пуст");
                return new HashMap<Integer, Task>();
            } else {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("Tasks.dat"));
                return DBLayout.deserializeListTask(in);
            }
        } else {
            System.out.println("Файла Tasks.dat не существует на данном устройстве. \nФайл Tasks.dat был создан.");
            checkExist.createNewFile();
            return new HashMap<Integer, Task>();
        }
    }

    //сериализация
    private static void serializeListTask(Map<Integer, Task> mapTask, OutputStream out) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(mapTask);
        objectOutputStream.close();
    }

    //десериализация
    private static Map<Integer, Task> deserializeListTask(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        return (Map<Integer, Task>) objectInputStream.readObject();
    }

    //сохранение листа в файл после каждого изменения
    private void saveListTask() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Tasks.dat"));
        serializeListTask(mapTask, out);
        out.close();
    }

////////////

    //конструктор
    public DBLayout() throws IOException, ClassNotFoundException {
        mapTask = getAllTasksConstr();
    }

    //передача всех задач в контроллер
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> list = new ArrayList<Task>(mapTask.values());
        Collections.sort(list, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return list;
    }

    //добавление задачи в мапу
    public void addTask(Task task) throws IOException {
        mapTask.put(task.getId(), task);
        saveListTask();
    }

    //удаление задачи
    public void deleteTask(Integer id) throws IOException {
        mapTask.remove(id);
        saveListTask();
    }

    //обновить таск
    public void updateTask(Task newT) throws IOException {
        mapTask.put(newT.getId(), newT);
        saveListTask();
    }

    //получить мапу задач по определённой дате
    public ArrayList<Task> getTaskByDate(LocalDate date) {
        ArrayList<Task> newTasks = new ArrayList<Task>();
        for (Map.Entry<Integer, Task> integerTaskEntry : mapTask.entrySet()) {
            Task value = integerTaskEntry.getValue();
            if (value.getDate().isAfter(LocalDateTime.of(date, LocalTime.of(0, 0)))
                    && value.getDate().isBefore(LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0)))) {
                newTasks.add(value);
            }
        }
        if (newTasks.size() == 0) {
            return null;
        }
        return newTasks;
    }

    //получить список задач по дате и типу
    public ArrayList<Task> getTaskByDateAndType(LocalDate date, String type) {
        ArrayList<Task> newTasks = new ArrayList<Task>();
        for (Map.Entry<Integer, Task> integerTaskEntry : mapTask.entrySet()) {
            Task value = integerTaskEntry.getValue();
            if (value.getDate().isAfter(LocalDateTime.of(date, LocalTime.of(0, 0)))
                    && value.getDate().isBefore(LocalDateTime.of(date.plusDays(1), LocalTime.of(0, 0)))
                    && value.getType().equals(type)) {
                newTasks.add(value);
            }
        }
        if (newTasks.size() == 0) {
            return null;
        }
        return newTasks;
    }

    //возвращает массив id для проверки в контроллере
    public Integer[] getAllId() {
        Integer[] allId = new Integer[mapTask.size()];
        int i = 0;
        for (Map.Entry<Integer, Task> integerTaskEntry : mapTask.entrySet()) {
            Task value = integerTaskEntry.getValue();
            allId[i] = value.getId();
            i++;
        }
        return allId;
    }

    public Task getTaskById(Integer id) {
        return mapTask.get(id);
    }

    public void setPerformed(int id, boolean check) {
        getTaskById(id).setPerformed(check);
    }

}
