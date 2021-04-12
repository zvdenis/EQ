package com.SuperClub.EQ.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Menu.MainActivity;
import com.SuperClub.EQ.R;
import com.SuperClub.EQ.network.RequestController;
import com.android.volley.Response;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Wave;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import es.dmoral.toasty.Toasty;

public class LoginTabFragment extends Fragment {


    private EditText login;
    private EditText password;
    private Button loginButton;
    private TextView forgotPasswordTextView;
    private ProgressBar progressBar;
    private View progressOverlay;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);
        login = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        loginButton = root.findViewById(R.id.login_button);
        forgotPasswordTextView = root.findViewById(R.id.forgot_password);
        progressBar = getActivity().findViewById(R.id.spin_kit);
        Sprite sprite = new Wave();
        progressBar.setIndeterminateDrawable(sprite);
        progressBar.setVisibility(View.VISIBLE);
        progressOverlay = getActivity().findViewById(R.id.loading_layout);
        progressOverlay.setVisibility(View.INVISIBLE);

        login.setTranslationX(300);
        password.setTranslationX(300);

        login.setAlpha(0);
        password.setAlpha(0f);

        login.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        loginButton.setOnClickListener(this::loginClicked);
        forgotPasswordTextView.setOnClickListener(this::forgotPasswordClicked);


        return root;
    }

    public void loginClicked(View view) {

        if (!validFields()) {
            return;
        }
        progressOverlay.setVisibility(View.VISIBLE);
        sendLoginRequest();


    }

    private boolean validFields() {
        return true;
    }

    private void sendLoginRequest() {
        RequestController.getInstance(getContext()).sendLoginRequest(login.getText().toString(), password.getText().toString(),
                this::loginResponse,
                error -> {
                    progressOverlay.setVisibility(View.INVISIBLE);
                    String body = null;
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                        JSONObject jsonObject = new JSONObject(body);
                        Toasty.warning(getContext(), jsonObject.getString("message")).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toasty.error(getContext(), "Connection error").show();
                    }
                });
    }

    private void loginResponse(JSONObject response) {
        Application.getInstance(getContext()).setPassword(password.getText().toString());
        try {
            Application.getInstance(getContext()).setEmail(response.get("email").toString());
            Application.getInstance(getContext()).setToken(response.get("token").toString());
            Application.getInstance(getContext()).setName(response.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressOverlay.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);


        RequestController.getInstance(getContext()).updateMyQueues();
    }

    public void forgotPasswordClicked(View view) {

    }
}
