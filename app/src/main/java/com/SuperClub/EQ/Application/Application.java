package com.SuperClub.EQ.Application;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.SuperClub.EQ.Data.QueueInfo;
import com.SuperClub.EQ.Data.UserInfo;
import com.SuperClub.EQ.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Application {

    private static final String CHANNEL_ID = "channel1";
    public ArrayList<QueueInfo> myQueues = new ArrayList();
    public ArrayList<QueueInfo> adminQueues = new ArrayList();

    private String userData = "userData";
    private String tokenKey = "token";
    private String passwordKey = "password";
    private String emailKey = "email";
    private String nameKey = "userName";
    private String numberKey = "number";
    private Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private Set<EventListener> listeners = new HashSet<>();
    private int interval = 1000 * 10;
    private final HashSet<Integer> queuesReceivedNotification = new HashSet<>();
    public QueueInfo passedQueue;
    public ArrayList<UserInfo> passedUsers;

    private String notificationTitle = "Your time soon";


    public Application(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(userData, Context.MODE_PRIVATE);
        editor = prefs.edit();

        createNotificationChannel();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
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

    public String getName() {
        return prefs.getString(nameKey, null);
    }

    public void setName(String value) {
        editor.putString(nameKey, value);
        editor.commit();
    }


    public int getNotificationNumber() {
        return prefs.getInt(numberKey, 3);
    }

    public void setNotificationNumber(int value) {
        editor.putInt(numberKey, value);
        editor.commit();
    }

    public void setMyQueues(ArrayList<QueueInfo> queues) {
        myQueues = queues;
        queuesChanged();
    }

    public void setAdminQueues(ArrayList<QueueInfo> queues) {
        adminQueues = queues;
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

    public void removeAdminQueue(String id) {
        for (QueueInfo queue : adminQueues) {
            if (queue.id.equals(id)) {
                adminQueues.remove(queue);
                break;
            }
        }
        queuesChanged();
    }

    public void queuesChanged() {
        for (EventListener listener : listeners) {
            listener.event();
        }

        for (QueueInfo queue : myQueues) {
            if (queue.usersBeforeMe <= getNotificationNumber()) {
                notifyAboutNumber(queue);
            }
        }
    }

    public void notifyAboutNumber(QueueInfo queue) {
        if (!queuesReceivedNotification.contains(Integer.parseInt(queue.id))) {
            showNotification(notificationTitle, "In queue " + queue.title + " only " + queue.usersBeforeMe + " people before you");
            queuesReceivedNotification.add(Integer.parseInt(queue.id));
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


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    public void nullify() {
        instance = null;
    }
}
