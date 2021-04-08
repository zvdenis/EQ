package com.SuperClub.EQ.Application;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.sip.SipSession;

import com.SuperClub.EQ.Data.QueueInfo;

import java.util.ArrayList;


public class Application {

    public ArrayList<QueueInfo> myQueues = new ArrayList();

    private String userData = "userData";
    private String tokenKey = "token";
    private String passwordKey = "password";
    private String emailKey = "email";
    private Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private ArrayList<EventListener> listeners = new ArrayList<>();
    public QueueInfo passedQueue;


    public Application(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(userData, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public String getToken() {
        return prefs.getString(tokenKey, null);
    }

    public void setToken(String token) {
        editor.putString(tokenKey, token);
        editor.commit();
    }

    public String getPassword() {
        return prefs.getString(passwordKey, null);
    }

    public void setPassword(String password) {
        editor.putString(passwordKey, password);
        editor.commit();
    }

    public String getEmail() {
        return prefs.getString(emailKey, null);
    }

    public void setEmail(String email) {
        editor.putString(emailKey, email);
        editor.commit();
    }

    public void setMyQueues(ArrayList<QueueInfo> queues) {
        myQueues = queues;
        queuesChanged();
    }

    public void removeQueue(String id) {
        for (QueueInfo queue : myQueues) {
            if (queue.id.equals(id)) {
                myQueues.remove(queue);
                break;
            }
        }
        queuesChanged();
    }

    public void queuesChanged() {
        for (EventListener listener : listeners) {
            listener.event();
        }
    }


    public void addMyQueuesListener(EventListener listener) {
        listeners.add(listener);
    }


    static Application instance;

    public static Application getInstance(Context context) {
        if (instance == null) {
            instance = new Application(context);
        }
        return instance;
    }
}
