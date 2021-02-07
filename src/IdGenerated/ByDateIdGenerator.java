package IdGenerated;

import main.Interface.IdGenerated;
import main.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ByDateIdGenerator implements IdGenerated {
    public ByDateIdGenerator(){}
    @Override
    public int generateId(ArrayList<Task> t) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getDayOfMonth() * (int) (Math.random() * 10) + localDateTime.getMinute();
    }
}
