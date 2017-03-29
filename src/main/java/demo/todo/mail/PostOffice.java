package demo.todo.mail;

import act.Act;
import act.mail.Mailer;
import demo.todo.model.User;
import org.osgl.util.Token;

import javax.inject.Singleton;

import static act.mail.Mailer.Util.send;
import static act.mail.Mailer.Util.subject;
import static act.mail.Mailer.Util.to;

@Mailer
@Singleton
public class PostOffice {

    public void sendWelcomeLetter(User user) {
        to(user.email);
        subject("Welcome to Yet Another Todo List!");
        String tk = Act.crypto().generateToken(Token.Life.ONE_DAY, user.email);
        send(user, tk);
    }

}
