package IdGenerated;

import main.Interface.IdGenerated;
import main.Task;

import java.util.ArrayList;
import java.util.Random;

public class RandomIdGenerator implements IdGenerated {
    public RandomIdGenerator(){}
    @Override
    public int generateId(ArrayList<Task> t) {
        Random rand = new Random();
        return rand.nextInt(9999 - 1000) + 1000;
    }
}
