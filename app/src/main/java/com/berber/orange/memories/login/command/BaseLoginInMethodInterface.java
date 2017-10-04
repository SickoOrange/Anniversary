package com.berber.orange.memories.login.command;

import com.berber.orange.memories.login.service.BaseLoginInCallBack;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public interface BaseLoginInMethodInterface {

    void login(String email, String password, BaseLoginInCallBack callBack);

    void login();

    void logout();

    void signUp();

}
