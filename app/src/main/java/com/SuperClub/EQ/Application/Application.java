package com.SuperClub.EQ.Application;

import android.content.Context;
import android.content.SharedPreferences;

public class Application {

    private String userData = "userData";
    private String tokenKey = "token";
    private String passwordKey = "password";
    private String emailKey = "email";
    private Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public Application(Context context) {
        this.context = context;
        editor = context.getSharedPreferences(userData, Context.MODE_PRIVATE).edit();
        prefs = context.getSharedPreferences(userData, Context.MODE_PRIVATE);
    }

    public String getToken() {
        return prefs.getString(tokenKey, null);
    }

    public void setToken(String token) {
        editor.putString(tokenKey, token);
    }

    public String getPassword() {
        return prefs.getString(passwordKey, null);
    }

    public void setPassword(String password) {
        editor.putString(passwordKey, password);
    }

    public String getEmail() {
        return prefs.getString(emailKey, null);
    }

    public void setEmail(String email) {
        editor.putString(emailKey, email);
    }


    static Application instance;

    public static Application getInstance(Context context) {
        if (instance == null) {
            instance = new Application(context);
        }
        return instance;
    }
}
