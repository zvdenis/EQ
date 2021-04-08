package com.SuperClub.EQ.Menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperClub.EQ.R;
import com.SuperClub.EQ.network.RequestController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONObject;


public class CreateQueueActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private TextView startTimer;
    private TextView endTimer;
    private EditText title;
    private EditText description;
    private Button createButton;
    private PickerListener startListener;
    private PickerListener endListener;
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
        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(this);

        startListener = new PickerListener(this, startTimer);
        endListener = new PickerListener(this, endTimer);
        startTimer.setOnClickListener(startListener);
        endTimer.setOnClickListener(endListener);
        description = findViewById(R.id.description);
        title = findViewById(R.id.code_text);


    }


    @Override
    public void onClick(View v) {
        progressOverlay.setVisibility(View.VISIBLE);

        RequestController.getInstance(this).sendCreateQueueRequest(title.getText().toString(), description.getText().toString(), startListener.date, endListener.date, this, this);
    }

    @Override
    public void onResponse(JSONObject response) {
        progressOverlay.setVisibility(View.INVISIBLE);
        finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        Toast toast = Toast.makeText(this, "Something went wrong" + error.toString(), Toast.LENGTH_LONG);
        toast.show();
    }
}