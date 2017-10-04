package com.berber.orange.memories.loginservice.command;

import com.berber.orange.memories.loginservice.service.BaseLoginInCallBack;

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
