package com.SuperClub.EQ.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperClub.EQ.Admin.PeopleInQueueActivity;
import com.SuperClub.EQ.Admin.ShowQRActivity;
import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Application.Configs;
import com.SuperClub.EQ.Application.Hasher;
import com.SuperClub.EQ.Data.QueueInfo;
import com.SuperClub.EQ.Data.UserInfo;
import com.SuperClub.EQ.R;
import com.SuperClub.EQ.network.RequestController;
import com.android.volley.VolleyError;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class ManageQueueActivity extends AppCompatActivity {


    public static final String noMoreUsers = "No more users in queue or queue not started";
    public static final String actionNotAllowrd = "Action not allowed for you";
    public static final String cantCreateOfflineUser = "Cant create anon";

    LinearLayout peopleQueueLayout;
    QueueInfo queueInfo;
    TextView title;
    TextView description;
    TextView number;
    Application instance;
    Button nextButton;
    Button offlineButton;
    ImageButton qrButton;
    TextView codeText;
    boolean isMyQueue;

    private ProgressBar progressBar;
    private View progressOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = Application.getInstance(this);
        queueInfo = instance.passedQueue;
        setContentView(R.layout.activity_manage_queue);
        title = findViewById(R.id.title_text);
        description = findViewById(R.id.description);
        nextButton = findViewById(R.id.next_button);
        offlineButton = findViewById(R.id.offline_user);
        number = findViewById(R.id.number);
        qrButton = findViewById(R.id.qr_button);
        codeText = findViewById(R.id.code_text);
        peopleQueueLayout = findViewById(R.id.people_in_queue_layout);
        qrButton.setOnClickListener(this::qrButtonPress);
        nextButton.setOnClickListener(this::nextButtonPress);
        offlineButton.setOnClickListener(this::offlineButtonPress);
        peopleQueueLayout.setOnClickListener(this::peoplePress);
        updateText();


        progressBar = findViewById(R.id.spin_kit);
        Sprite sprite = new Wave();
        progressBar.setIndeterminateDrawable(sprite);
        progressBar.setVisibility(View.VISIBLE);
        progressOverlay = findViewById(R.id.loading_layout);
        progressOverlay.setVisibility(View.INVISIBLE);
        Application.getInstance(this).addMyQueuesListener(this::updateText);
    }


    public void updateText() {
        QueueInfo realQueue = null;
        for (QueueInfo queue : instance.adminQueues) {
            if (queue.id.equals(queueInfo.id)) {
                realQueue = queue;
            }
        }
        description.setText(realQueue.description);
        title.setText(realQueue.title);
        codeText.setText(Hasher.getQueueCode(Integer.parseInt(realQueue.id)).toUpperCase());
        number.setText(String.valueOf(queueInfo.getLiveUserCount()));
    }


    public void qrButtonPress(View view) {
        Application.getInstance(this).passedQueue = queueInfo;
        Intent intent = new Intent(this, ShowQRActivity.class);
        intent.putExtra("title", queueInfo.title);
        intent.putExtra(Configs.qrTextExtraName, queueInfo.code);
        startActivity(intent);
    }

    public void nextButtonPress(View view) {
        progressOverlay.setVisibility(View.VISIBLE);
        RequestController.getInstance(this).sendNextUserRequest(queueInfo.id, this::onResponseNext, this::onErrorResponseNext);
    }


    public void onResponseNext(JSONObject response) {
        queueInfo.currentUser += 1;
        RequestController.getInstance(this).updateMyQueues();
        updateText();
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.success(this, "Next called").show();
        RequestController.getInstance(this).updateMyQueues();
    }


    public void onErrorResponseNext(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        try {
            String body = new String(error.networkResponse.data, "UTF-8");
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.get("message").equals(noMoreUsers)) {
                noMoreUsersInQueue();
            } else {
                Toasty.error(this, "Network error").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void noMoreUsersInQueue() {
        Toasty.warning(this, noMoreUsers).show();
    }


    public void offlineButtonPress(View view) {
        progressOverlay.setVisibility(View.VISIBLE);
        RequestController.getInstance(this).sendOfflineUserRequest(queueInfo.id, this::onResponseOffline, this::onErrorResponseOffline);
    }


    public void onResponseOffline(JSONObject response) {
        queueInfo.currentUser -= 1;
        RequestController.getInstance(this).updateMyQueues();
        updateText();
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.success(this, "Offline user added").show();
    }


    public void onErrorResponseOffline(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        try {
            String body = new String(error.networkResponse.data, "UTF-8");
            JSONObject jsonObject = new JSONObject(body);
            switch ((String) jsonObject.get("message")) {
                case noMoreUsers:

                case cantCreateOfflineUser:
                default:
                    Toasty.error(this, "Failed to add offline user").show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(this, "Unknown Error").show();
        }
    }


    public void peoplePress(View view) {
        RequestController.getInstance(this).sendPeopleInQueuesRequest(queueInfo.id, this::onResponsePeople, this::onErrorResponsePeople);

    }

    public void onResponsePeople(JSONArray response) {
        Type itemListType = new TypeToken<List<UserInfo>>() {
        }.getType();
        ArrayList<UserInfo> users = new Gson().fromJson(response.toString(), itemListType);
        Application.getInstance(this).passedUsers = users;
        progressOverlay.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, PeopleInQueueActivity.class);
        startActivity(intent);
    }


    public void onErrorResponsePeople(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        try {
            String body = new String(error.networkResponse.data, "UTF-8");
            JSONObject jsonObject = new JSONObject(body);
            Toasty.error(this, "Unknown Error").show();
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(this, "Unknown Error").show();
        }
    }
}