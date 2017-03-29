package demo.todo.controller;

import act.controller.Controller;
import act.db.DbBind;
import demo.todo.model.TodoItem;
import org.osgl.aaa.AAA;
import org.osgl.mvc.annotation.DeleteAction;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.mvc.annotation.PutAction;

import javax.inject.Inject;

import static act.controller.Controller.Util.notFoundIfNull;
import static demo.todo.security.TodoPermission.*;

@Controller("/todo")
public class TodoController extends AuthenticatedController {

    @Inject
    private TodoItem.Dao dao;

    @GetAction
    public Iterable<TodoItem> myItems() {
        AAA.requirePermission(me, PERM_VIEW_TODO_ITEM);
        return dao.findBy("owner", me.email);
    }

    @PostAction
    public TodoItem add(String subject) {
        AAA.requirePermission(me, PERM_CREATE_TODO_ITEM);
        TodoItem todoItem = new TodoItem(subject);
        todoItem.owner = me.email;
        return dao.save(todoItem);
    }

    @PutAction("{id}")
    public TodoItem update(@DbBind("id") TodoItem todo, String subject) {
        notFoundIfNull(todo);
        AAA.requirePermission(todo, PERM_UPDATE_TODO_ITEM);
        todo.subject = subject;
        return dao.save(todo);
    }

    @DeleteAction("{id}")
    public void delete(@DbBind("id") TodoItem todo) {
        notFoundIfNull(todo);
        AAA.requirePermission(todo, PERM_DELETE_TODO_ITEM);
        dao.delete(todo);
    }

}
