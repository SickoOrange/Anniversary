package com.berber.orange.memories.login.command;

import com.berber.orange.memories.login.service.BaseSignInCallBack;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public interface BaseSignInMethodInterface {

    void login(String email, String password, BaseSignInCallBack callBack);

    void login();

    void logout();

    void signUp();

}
