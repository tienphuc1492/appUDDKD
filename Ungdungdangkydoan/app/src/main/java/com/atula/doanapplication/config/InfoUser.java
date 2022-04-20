package com.atula.doanapplication.config;

import com.atula.doanapplication.model.CUser;

public class InfoUser {
    private static InfoUser instance;
    public static InfoUser getInstance() {
        if (instance == null) {
            synchronized (InfoUser.class) {
                instance = new InfoUser();
            }
        }
        return instance;
    }

    public InfoUser() {
    }
    CUser cUser;

    public CUser getcUser() {
        return cUser;
    }

    public void setcUser(CUser cUser) {
        this.cUser = cUser;
    }

}
