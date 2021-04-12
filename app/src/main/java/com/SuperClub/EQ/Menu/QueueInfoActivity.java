package com.SuperClub.EQ.Menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Data.QueueInfo;
import com.SuperClub.EQ.R;
import com.SuperClub.EQ.network.RequestController;
import com.android.volley.VolleyError;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;


public class QueueInfoActivity extends AppCompatActivity {

    QueueInfo queueInfo;
    TextView title;
    TextView description;
    TextView number;
    TextView numberTitle;
    Application instance;
    Button button;
    boolean isMyQueue;

    private ProgressBar progressBar;
    private View progressOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = Application.getInstance(this);
        queueInfo = instance.passedQueue;
        setContentView(R.layout.activity_queue_info);
        String id = getIntent().getStringExtra("QueueID");
        title = findViewById(R.id.title_text);
        description = findViewById(R.id.description);
        number = findViewById(R.id.number);
        button = findViewById(R.id.button);
        numberTitle = findViewById(R.id.number_title);
        updateText();


        progressBar = findViewById(R.id.spin_kit);
        Sprite sprite = new Wave();
        progressBar.setIndeterminateDrawable(sprite);
        progressBar.setVisibility(View.VISIBLE);
        progressOverlay = findViewById(R.id.loading_layout);
        progressOverlay.setVisibility(View.INVISIBLE);
    }


    public void updateText() {
        QueueInfo realQueue = null;
        isMyQueue = false;
        for (QueueInfo queue : instance.myQueues) {
            if (queue.id.equals(queueInfo.id)) {
                isMyQueue = true;
                realQueue = queue;
            }
        }
        description.setText(queueInfo.description);
        title.setText(queueInfo.title);
        if (isMyQueue) {
            button.setOnClickListener(this::leave);
            button.setBackgroundResource(R.drawable.button_red_bg2);
            button.setText(R.string.leave);
            numberTitle.setText(R.string.your_number);
            number.setText(String.valueOf(realQueue.usersBeforeMe + 1));
        } else {
            button.setOnClickListener(this::subscribe);
            button.setBackgroundResource(R.drawable.button_bg2);
            button.setText(R.string.join);
            numberTitle.setText(R.string.available_q_number);
            number.setText(String.valueOf(queueInfo.usersBeforeMe + 1));
        }

    }


    public void subscribe(View v) {
        progressOverlay.setVisibility(View.VISIBLE);
        RequestController.getInstance(this).sendRegisterForQueueRequest(queueInfo.id, this::onResponseSubscribe, this::onErrorResponseSubscribe);
    }


    public void onResponseSubscribe(JSONObject response) {
        QueueInfo queue = new Gson().fromJson(response.toString(), QueueInfo.class);
        queueInfo = queue;
        instance.myQueues.add(queue);
        instance.queuesChanged();
        updateText();
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.success(this, "You joined queue").show();
    }


    public void onErrorResponseSubscribe(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.error(this, "Failed to leave queue").show();
    }


    public void leave(View v) {
        progressOverlay.setVisibility(View.VISIBLE);
        RequestController.getInstance(this).sendLeaveQueueRequest(queueInfo.id, this::onResponseLeave, this::onErrorResponseLeave);
    }


    public void onResponseLeave(JSONObject response) {
        instance.removeQueue(queueInfo.id);
        instance.queuesChanged();
        updateText();
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.success(this, "You left the queue").show();
    }


    public void onErrorResponseLeave(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.error(this, "Failed to leave queue").show();
    }
}