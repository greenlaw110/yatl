package demo.todo.security;

import act.util.Stateless;
import demo.todo.model.User;
import org.osgl.aaa.*;
import org.osgl.aaa.impl.SimplePrincipal;
import org.osgl.util.C;
import org.osgl.util.E;

import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;

@Stateless
public class TodoA3PersistentService implements AAAPersistentService {

    @Inject
    private TodoPermission.Dao permDao;

    @Inject
    private User.Dao userDao;

    @Override
    public void save(AAAObject aaaObject) {
        E.unsupport();
    }

    @Override
    public void remove(AAAObject aaaObject) {
        E.unsupport();
    }

    @Override
    public <T extends AAAObject> void removeAll(Class<T> clz) {
        E.unsupport();
    }

    @Override
    public <T extends AAAObject> T findByName(String name, Class<T> clz) {
        if (Principal.class.isAssignableFrom(clz)) {
            User user = userDao.findByEmail(name);
            return null == user ? null : (T)principalOf(user);
        } else if (Permission.class.isAssignableFrom(clz)) {
            return (T) permDao.findById(name);
        }
        throw E.unsupport();
    }

    @Override
    public Privilege findPrivilege(int level) {
        throw E.unsupport("privilege not used in this app");
    }

    @Override
    public Iterable<Privilege> allPrivileges() {
        return C.list();
    }

    @Override
    public C.List<Permission> allPermissions() {
        return C.list(permDao.findAll());
    }

    @Override
    public Iterable<Role> allRoles() {
        return C.list();
    }

    @Override
    public Iterable<String> allPrivilegeNames() {
        return C.list();
    }

    @Override
    public Iterable<String> allPermissionNames() {
        return allPermissions().map(Permission::getName);
    }

    @Override
    public Iterable<String> allRoleNames() {
        return null;
    }

    private Principal principalOf(User user) {
        Set<Permission> perms = permissionsOf(user);
        SimplePrincipal retval = new SimplePrincipal(user.email, null, C.list(), perms);
        return retval;
    }

    private Set<Permission> permissionsOf(User user) {
        Set<String> permNames = user.permissions();
        Set<Permission> retval = new HashSet<>();
        for (String permName : permNames) {
            retval.add(permDao.findById(permName));
        }
        return retval;
    }
}
