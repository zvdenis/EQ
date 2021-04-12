package com.SuperClub.EQ.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Application.FieldValidator;
import com.SuperClub.EQ.Application.ValidationResult;
import com.SuperClub.EQ.R;
import com.SuperClub.EQ.network.RequestController;
import com.android.volley.VolleyError;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class AccountInfoActivity extends AppCompatActivity {

    EditText emailText;
    EditText nameText;
    NumberPicker numberPicker;
    Button saveButton;

    private ProgressBar progressBar;
    private View progressOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        emailText = findViewById(R.id.email_field_text);
        saveButton = findViewById(R.id.save_button);
        nameText = findViewById(R.id.name_field_text);
        numberPicker = findViewById(R.id.notify_number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(15);
        saveButton.setOnClickListener(this::saveClick);


        progressBar = findViewById(R.id.spin_kit);
        Sprite sprite = new Wave();
        progressBar.setIndeterminateDrawable(sprite);
        progressBar.setVisibility(View.VISIBLE);
        progressOverlay = findViewById(R.id.loading_layout);
        progressOverlay.setVisibility(View.INVISIBLE);

        updateText();

    }

    void updateText() {
        emailText.setText(Application.getInstance(this).getEmail());
        nameText.setText(Application.getInstance(this).getName());
        numberPicker.setValue(Application.getInstance(this).getNotificationNumber());
    }


    public void saveClick(View view) {
        String newName = nameText.getText().toString();
        String newEmail = emailText.getText().toString();

        if (!validFields()) {
            return;
        }


        progressOverlay.setVisibility(View.VISIBLE);
        RequestController.getInstance(this).sendUpdateUserRequest(newEmail, newName, null, this::updateListener, this::updateError);


    }

    private boolean validFields() {

        ValidationResult result;

        result = FieldValidator.validateEmail(emailText.getText().toString());
        if (result.type == ValidationResult.ResultType.ERROR) {
            Toasty.warning(this, result.message).show();
            return false;
        }

        result = FieldValidator.validateName(nameText.getText().toString());
        if (result.type == ValidationResult.ResultType.ERROR) {
            Toasty.warning(this, result.message).show();
            return false;
        }


        return true;
    }


    void updateListener(JSONObject result) {
        progressOverlay.setVisibility(View.INVISIBLE);
        int newNumber = numberPicker.getValue();
        String newName = nameText.getText().toString();
        String newEmail = emailText.getText().toString();

        Application.getInstance(this).setNotificationNumber(newNumber);
        Application.getInstance(this).setName(newName);
        Application.getInstance(this).setEmail(newEmail);


        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

        Toasty.success(this, "Changed").show();
    }

    void updateError(VolleyError error) {
        progressOverlay.setVisibility(View.INVISIBLE);
        Toasty.error(this, "Failed update").show();
    }
}