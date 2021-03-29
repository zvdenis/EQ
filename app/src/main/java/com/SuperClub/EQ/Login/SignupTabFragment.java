package com.SuperClub.EQ.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.SuperClub.EQ.R;
import com.SuperClub.EQ.network.RequestController;
import com.github.ybq.android.spinkit.style.Wave;

public class SignupTabFragment extends Fragment {

    private EditText login;
    private EditText password;
    private EditText confirmPassword;
    private EditText name;
    private Button signupButton;
    private ProgressBar progressBar;
    private View progressOverlay;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);

        login = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        confirmPassword = root.findViewById(R.id.confirm_password);
        name = root.findViewById(R.id.name);
        signupButton = root.findViewById(R.id.signup_button);
        progressOverlay = getActivity().findViewById(R.id.loading_layout);

        progressBar = getActivity().findViewById(R.id.spin_kit);
        progressBar.setIndeterminateDrawable(new Wave());
        progressBar.setVisibility(View.VISIBLE);
        progressOverlay.setVisibility(View.INVISIBLE);


        signupButton.setOnClickListener(this::signupClicked);
        return root;
    }


    public void signupClicked(View view) {

        if (!validFields()) {
            return;
        }
        progressOverlay.setVisibility(View.VISIBLE);
        sendSignupRequest();
    }

    private boolean validFields() {
        return true;
    }

    private void sendSignupRequest() {
        RequestController.getInstance(getContext()).sendRegisterRequest(login.getText().toString(), password.getText().toString(), name.getText().toString(),
                response -> {
                    System.out.println(response);
                    progressOverlay.setVisibility(View.INVISIBLE);
                },
                error -> {
                    System.out.println(error.networkResponse);
                    progressOverlay.setVisibility(View.INVISIBLE);
                });
    }
}
