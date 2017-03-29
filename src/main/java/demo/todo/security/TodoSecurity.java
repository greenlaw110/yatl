package demo.todo.security;

import act.aaa.ActAAAService;
import act.aaa.DynamicPermissionCheckHelperBase;
import demo.todo.model.User;
import demo.todo.model.UserLinked;
import org.osgl.aaa.Principal;
import org.osgl.util.C;
import org.osgl.util.S;

import java.util.Set;

import static demo.todo.security.TodoPermission.*;

public class TodoSecurity extends ActAAAService.Base<User> {


    /**
     * In this simple Todo app every signed up user get granted
     * all of the following permissions
     */
    private static final Set<String> DEFAULT_PERMS = C.set(
            PERM_CREATE_TODO_ITEM,
            PERM_DELETE_TODO_ITEM,
            PERM_UPDATE_TODO_ITEM,
            PERM_VIEW_TODO_ITEM
    );

    /**
     * We tell act-aaa `email` is the key to extract the user from database
     */
    @Override
    protected String userKey() {
        return "email";
    }

    /**
     * Just return the default permission set
     */
    @Override
    protected Set<String> permissionsOf(User user) {
        return DEFAULT_PERMS;
    }

    /**
     * inject the logic of password verification into act-aaa
     */
    @Override
    protected boolean verifyPassword(User user, char[] password) {
        return user.verifyPassword(password);
    }

    /**
     * This will help to check a protected resource against the current logged in user
     * if the permission been authorised is dynamic
     */
    public static class DynamicPermissionChecker extends DynamicPermissionCheckHelperBase<UserLinked> {
        @Override
        public boolean isAssociated(UserLinked userLinked, Principal principal) {
            return S.eq(userLinked.userId(), principal.getName());
        }
    }

}