package com.SuperClub.EQ.Menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperClub.EQ.Application.FieldValidator;
import com.SuperClub.EQ.Application.ValidationResult;
import com.SuperClub.EQ.R;
import com.SuperClub.EQ.network.RequestController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONObject;

import java.util.Date;

import es.dmoral.toasty.Toasty;


public class CreateQueueActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private TextView startTimer;
    private TextView endTimer;
    private TextView dateTimer;
    private EditText title;
    private EditText description;
    private Button createButton;
    private PickerListener startListener;
    private PickerListener endListener;
    private PickerListener dateListener;
    private View progressOverlay;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_queue);

        progressBar = findViewById(R.id.spin_kit);
        Sprite sprite = new Wave();
        progressBar.setIndeterminateDrawable(sprite);
        progressBar.setVisibility(View.VISIBLE);

        progressOverlay = findViewById(R.id.loading_layout);
        progressOverlay.setVisibility(View.INVISIBLE);

        startTimer = findViewById(R.id.timer1);
        endTimer = findViewById(R.id.timer2);
        dateTimer = findViewById(R.id.date);
        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(this);

        startListener = new PickerListener(this, startTimer, PickerListener.PickType.TIME);
        endListener = new PickerListener(this, endTimer, PickerListener.PickType.TIME);
        dateListener = new PickerListener(this, dateTimer, PickerListener.PickType.DATE);

        startTimer.setOnClickListener(startListener);
        endTimer.setOnClickListener(endListener);
        dateTimer.setOnClickListener(dateListener);
        description = findViewById(R.id.description);
        title = findViewById(R.id.title_text);


    }


    @Override
    public void onClick(View v) {


        long millisInDay = 60 * 60 * 24 * 1000;

        long dateOnly = (dateListener.date.getTime() / millisInDay) * millisInDay;
        long startTimeOnly = startListener.date.getTime() % millisInDay;
        long endTimeOnly = endListener.date.getTime() % millisInDay;

        Date startTime = new Date(dateOnly + startTimeOnly);
        Date endTime = new Date(dateOnly + endTimeOnly);

        ValidationResult result;

        result = FieldValidator.validateQueueTitle(title.getText().toString());
        if (result.type == ValidationResult.ResultType.ERROR) {
            Toasty.warning(this, result.message).show();
            return;
        }

        result = FieldValidator.validateQueueDescription(description.getText().toString());
        if (result.type == ValidationResult.ResultType.ERROR) {
            Toasty.warning(this, result.message).show();
            return;
        }

        result = FieldValidator.validateStartEnd(startTime, endTime);
        if (result.type == ValidationResult.ResultType.ERROR) {
            Toasty.warning(this, result.message).show();
            return;
        }


        progressOverlay.setVisibility(View.VISIBLE);
        RequestController.getInstance(this).sendCreateQueueRequest(title.getText().toString(), description.getText().toString(), startTime, endTime, this, this);

    }

    @Override
    public void onResponse(JSONObject response) {
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.success(this, "Queue created").show();
        RequestController.getInstance(this).updateMyQueues();
        finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.error(this, "Failed to create queue").show();
    }
}