package demo.todo.model;

import act.db.ebean2.EbeanDao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "todo")
public class TodoItem implements UserLinked {

    @Id
    @GeneratedValue
    public long _id;

    public String subject;

    public String owner;

    @Override
    public String userId() {
        return owner;
    }

    public TodoItem(String subject) {
        this.subject = subject;
    }

    public static class Dao extends EbeanDao<Long, TodoItem> {

    }


}
