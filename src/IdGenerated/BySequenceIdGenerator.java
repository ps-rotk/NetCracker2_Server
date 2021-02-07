package IdGenerated;


import main.Interface.IdGenerated;
import main.Task;

import java.util.ArrayList;

public class BySequenceIdGenerator implements IdGenerated {
    public BySequenceIdGenerator(){}
    @Override
    public int generateId(ArrayList<Task> t) {
        return t.size() + 1;
    }
}
