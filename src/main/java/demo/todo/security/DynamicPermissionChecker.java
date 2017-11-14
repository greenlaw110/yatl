package demo.todo.security;

import act.aaa.DynamicPermissionCheckHelperBase;
import demo.todo.model.UserLinked;
import org.osgl.aaa.Principal;
import org.osgl.util.S;

public class DynamicPermissionChecker extends DynamicPermissionCheckHelperBase<UserLinked> {
    @Override
    public boolean isAssociated(UserLinked userLinked, Principal principal) {
        return S.eq(userLinked.userId(), principal.getName());
    }
}
