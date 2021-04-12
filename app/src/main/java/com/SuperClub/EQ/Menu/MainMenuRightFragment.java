package com.SuperClub.EQ.Menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.SuperClub.EQ.Admin.AdminQueues;
import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Login.LoginActivity;
import com.SuperClub.EQ.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuRightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuRightFragment extends Fragment {

    TextView nameText;
    TextView manageQueuesText;
    TextView accountText;
    Application instance;
    ViewGroup root;

    ConstraintLayout settings;
    ConstraintLayout logout;
    ConstraintLayout admin;
    ConstraintLayout profile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu_right, container, false);
        nameText = root.findViewById(R.id.user_name);
        manageQueuesText = root.findViewById(R.id.name_field_text);
        //accountText = root.findViewById(R.id.mail_text);
        instance = Application.getInstance(getContext());

        settings = root.findViewById(R.id.setting);
        logout = root.findViewById(R.id.logout);
        admin = root.findViewById(R.id.admin);
        //profile = root.findViewById(R.id.profile);


        settings.setOnClickListener(this::settingClick);
        logout.setOnClickListener(this::logoutClick);
        admin.setOnClickListener(this::adminClick);
        //profile.setOnClickListener(this::profileClick);

        updateText();
        return root;
    }

    public void updateText() {
        nameText.setText(instance.getName());
        //accountText.setText(instance.getEmail());
        manageQueuesText.setText(instance.adminQueues.size() + " " + getString(R.string.admin_queues_count));
    }

    public void settingClick(View v) {
        Intent intent = new Intent(getContext(), AccountInfoActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateText();
    }

    public void adminClick(View v) {
        Intent intent = new Intent(getContext(), AdminQueues.class);
        getContext().startActivity(intent);
    }

    public void profileClick(View v) {

    }

    public void logoutClick(View v) {
        Application.getInstance(getContext()).nullify();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getContext().startActivity(intent);

    }
}