package com.atula.doanapplication.config;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static String TK_DANGNHAP_ADMIN = "123456789";
    public static String MK_DANGNHAP_ADMIN = "adminCNTT";

    private static Config instance;
    public static Config getInstance(){
        if(instance == null){
            synchronized (Config.class){
                instance = new Config();
            }
        }
        return instance;
    }
    private Config() {
    }

    private static String SAVE_LOGIN = "SAVE_LOGIN";
    public void setSaveLogin(Context context, boolean isSave,String mssv,String pass, boolean isSV){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ISSAVE",isSave);
        editor.putBoolean("ISSV",isSV);
        editor.putString("MSSV",mssv);
        editor.putString("PASS",pass);
        editor.apply();
    }
    public HashMap<String, String> getSaveLogin(Context context){
        HashMap<String, String> map = new HashMap();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_LOGIN,Context.MODE_PRIVATE);
        map.put("ISSAVE",sharedPreferences.getBoolean("ISSAVE",false)+"");
        map.put("ISSV",sharedPreferences.getBoolean("ISSV",false)+"");
        map.put("MSSV",sharedPreferences.getString("MSSV",""));
        map.put("PASS",sharedPreferences.getString("PASS",""));
        return  map;
    }
}
