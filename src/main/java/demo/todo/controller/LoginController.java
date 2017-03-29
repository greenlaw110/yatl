package demo.todo.controller;

import act.Act;
import act.app.ActionContext;
import act.controller.Controller;
import demo.todo.mail.PostOffice;
import demo.todo.model.User;
import org.osgl.aaa.NoAuthentication;
import org.osgl.http.H;
import org.osgl.inject.annotation.Provided;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.util.Token;

import javax.inject.Inject;

/**
 * Responsible for Login/Logout/Sign up
 */
@NoAuthentication
public class LoginController extends Controller.Util {

    @Inject
    private User.Dao userDao;

    @GetAction("/login")
    public void loginForm() {
    }

    @PostAction("/login")
    public void login(String email, String password, H.Flash flash, ActionContext context) {
        User user = userDao.authenticate(email, password);
        if (null == user) {
            flash.error("cannot find user by email and password combination");
            redirect("/login");
        }
        context.login(email);
    }

    @GetAction("/sign_up")
    public void signUpForm() {
    }

    @PostAction("/sign_up")
    public void signUp(User user, ActionContext context, @Provided PostOffice postOffice) {
        if (userDao.exists(user.email)) {
            context.flash().error("User already exists");
            redirect("/sign_up");
        }
        user.activated = false;
        userDao.save(user);
        postOffice.sendWelcomeLetter(user);
        redirect("/sign_up_ok");
    }

    @GetAction("/sign_up_ok")
    public void signUpConfirm() {
    }

    @GetAction("/activate")
    public void activate(String tk, ActionContext context) {
        Token token = Act.crypto().parseToken(tk);
        notFoundIfNot(token.isValid());
        User user = userDao.findByEmail(token.id());
        notFoundIfNull(user);
        context.session("tk", tk);
        render(user);
    }

    @PostAction("/activate")
    public void completeActivation(String password, ActionContext context) {
        String tk = context.session("tk");
        notFoundIfNull(tk);
        Token token = Act.crypto().parseToken(tk);
        notFoundIfNot(token.isValid());
        User user = userDao.findByEmail(token.id());
        token.consume();
        user.setPassword(password);
        user.activated = true;
        userDao.save(user);
        context.login(user.email);
        redirect("/");
    }

}
