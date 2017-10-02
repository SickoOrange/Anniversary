package com.berber.orange.memories.login.command;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public interface BaseSignInMethodInterface {

    void login(String email, String password);

    void login();

    void logout();

    void signUp();

}
