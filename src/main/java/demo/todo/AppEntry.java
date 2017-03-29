package demo.todo;

import act.Act;
import demo.todo.controller.AuthenticatedController;
import org.osgl.mvc.annotation.GetAction;

import static act.controller.Controller.Util.render;

public class AppEntry extends AuthenticatedController {

    @GetAction("/")
    public void home() {
        render(me);
    }

    public static void main(String[] args) throws Exception {
        Act.start("Yet Another Todo List");
    }

}
