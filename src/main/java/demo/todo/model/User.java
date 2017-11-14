package demo.todo.model;

import act.Act;
import act.cli.Command;
import act.cli.Required;
import act.db.ebean2.EbeanDao;
import act.util.SimpleBean;
import org.apache.bval.constraints.NotEmpty;
import org.osgl.aaa.Permission;
import org.osgl.util.C;
import org.osgl.util.S;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "user")
public class User implements UserLinked, SimpleBean {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    public String email;

    @NotEmpty
    public String firstName;

    @NotEmpty
    public String lastName;

    public boolean activated;

    private String password;

    private String permList;

    public String fullName() {
        return S.concat(lastName, " ", firstName);
    }

    public void setPassword(String password) {
        this.password = Act.crypto().passwordHash(password);
    }

    public boolean verifyPassword(char[] password) {
        return Act.crypto().verifyPassword(password, this.password);
    }

    public Set<String> permissions() {
        return C.set(S.fastSplit(permList, ","));
    }

    public void grant(Permission permission) {
        this.permList = S.join(",", C.newList(permissions()).append(permission.getName()));
    }

    public void revoke(Permission permission) {
        Set<String> perms = new HashSet<>(permissions());
        perms.remove(permission.getName());
        this.permList = S.join(",", perms);
    }

    @Override
    public String userId() {
        return email;
    }

    public static class Dao extends EbeanDao<Long, User> {

        public User findByEmail(String email) {
            return findOneBy("email", email);
        }

        public User authenticate(String email, String password) {
            User user = findByEmail(email);
            return null == user ? null : user.activated ? user.verifyPassword(password.toCharArray()) ? user : null : null;
        }

        public boolean exists(String email) {
            return countBy("email", email) > 0;
        }

        @Command(name = "perm.grant", help = "Grant permission to user")
        public void grantPermission(
                @Required("specify user email") String email,
                @Required("specify permission name") String permission
        ) {
            User user = findByEmail(email);
            if (null != user) {
                user.permList = S.join(",", C.newList(user.permissions()).append(permission));
                save(user);
            }
        }

        @Command(name = "perm.revoke", help = "Revoke permission to user")
        public void revokePermission(
                @Required("specify user email") String email,
                @Required("specify permission name") String permission
        ) {
            User user = findByEmail(email);
            if (null != user) {
                Set<String> perms = new HashSet<>(user.permissions());
                perms.remove(permission);
                user.permList = S.join(",", perms);
                save(user);
            }
        }

    }
}
