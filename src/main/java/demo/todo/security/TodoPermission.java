package demo.todo.security;

import act.cli.Command;
import act.cli.Required;
import act.db.ebean2.EbeanDao;
import act.job.OnAppStart;
import act.util.SimpleBean;
import act.util.Stateless;
import org.osgl.aaa.Permission;
import org.osgl.util.C;
import org.osgl.util.E;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "perm")
public class TodoPermission implements SimpleBean, Permission {

    public static final String PERM_CREATE_TODO_ITEM = "create-todo-item";
    public static final String PERM_UPDATE_TODO_ITEM = "update-todo-item";
    public static final String PERM_VIEW_TODO_ITEM = "view-todo-item";
    public static final String PERM_DELETE_TODO_ITEM = "delete-todo-item";

    @Id
    public String name;

    public boolean dynamic;

    public TodoPermission(String name, boolean dynamic) {
        this.name = name;
        this.dynamic = dynamic;
    }

    @Override
    public boolean isDynamic() {
        return dynamic;
    }

    @Override
    public Set<Permission> implied() {
        return C.set();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setProperty(String key, String value) {
        E.unsupport();
    }

    @Override
    public void unsetProperty(String key) {
        E.unsupport();
    }

    @Override
    public String getProperty(String key) {
        return null;
    }

    @Override
    public Set<String> propertyKeys() {
        return C.set();
    }

    @Stateless
    public static class Dao extends EbeanDao<String, TodoPermission> {

        @Command(name = "perm.create", help = "create permission")
        public Permission create(
                @Required("specify permission name") String name,
                @Required("specify whether this permission is dynamic or not") boolean dynamic
        ) {
            TodoPermission permission = new TodoPermission(name, dynamic);
            return save(permission);
        }

        @OnAppStart
        public void ensureDefaultPermissions() {
            if (0 < count()) {
                return;
            }
            create(PERM_CREATE_TODO_ITEM, false);
            create(PERM_VIEW_TODO_ITEM, true);
            create(PERM_UPDATE_TODO_ITEM, true);
            create(PERM_DELETE_TODO_ITEM, true);
        }
    }


}
