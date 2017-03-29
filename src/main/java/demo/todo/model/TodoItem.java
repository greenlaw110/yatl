package demo.todo.model;

import act.db.morphia.MorphiaAdaptiveRecord;
import act.db.morphia.MorphiaDao;
import org.mongodb.morphia.annotations.Entity;

@Entity("todo")
public class TodoItem extends MorphiaAdaptiveRecord<TodoItem> implements UserLinked {

    public String subject;

    public String owner;

    @Override
    public String userId() {
        return owner;
    }

    public TodoItem(String subject) {
        this.subject = subject;
    }

    public static class Dao extends MorphiaDao<TodoItem> {

    }


}
