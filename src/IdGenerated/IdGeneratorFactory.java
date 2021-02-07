package IdGenerated;

import main.Task;

import java.util.ArrayList;
import java.util.Random;

public class IdGeneratorFactory {
    private final Random r;

    public IdGeneratorFactory() {
        r = new Random();
    }

        public int createId(ArrayList<Task> t) {

        int c = r.nextInt(3);
        if (c == 0) {
            ByDateIdGenerator byDateIdGenerator = new ByDateIdGenerator();
            return byDateIdGenerator.generateId(t);
        }
        if (c == 1) {
            BySequenceIdGenerator bySequenceIdGenerator = new BySequenceIdGenerator();
            return bySequenceIdGenerator.generateId(t);
        }
        if (c == 2) {
            RandomIdGenerator randomIdGenerator = new RandomIdGenerator();
            return randomIdGenerator.generateId(t);
        }
        return r.nextInt();
    }
}
