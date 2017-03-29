package demo.todo.controller;

import act.aaa.LoginUser;
import demo.todo.model.User;

public abstract class AuthenticatedController {
    @LoginUser
    protected User me;
}
