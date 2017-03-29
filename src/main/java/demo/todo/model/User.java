package demo.todo.model;

import act.Act;
import act.db.morphia.MorphiaDao;
import act.db.morphia.MorphiaModel;
import org.apache.bval.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.osgl.util.S;

@Entity("user")
public class User extends MorphiaModel<User> implements UserLinked {

    @NotEmpty
    public String email;

    @NotEmpty
    public String firstName;

    @NotEmpty
    public String lastName;

    public boolean activated;

    private String password;

    public String fullName() {
        return S.concat(lastName, " ", firstName);
    }

    public void setPassword(String password) {
        this.password = Act.crypto().passwordHash(password);
    }

    public boolean verifyPassword(char[] password) {
        return Act.crypto().verifyPassword(password, this.password);
    }

    @Override
    public String userId() {
        return email;
    }

    public static class Dao extends MorphiaDao<User> {

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

    }
}
