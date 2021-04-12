package com.SuperClub.EQ.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Data.UserInfo;
import com.SuperClub.EQ.Menu.MyQueuesRecyclerAdapter;
import com.SuperClub.EQ.R;

import java.util.ArrayList;

public class PeopleInQueueActivity extends AppCompatActivity {

    private static boolean b = true;
    RecyclerView recyclerView;
    PeopleInQueueRecyclerAdapter adapter;
    LinearLayout emptyListImage;
    ArrayList<UserInfo> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_in_queue);
        users = Application.getInstance(this).passedUsers;

        recyclerView = findViewById(R.id.recycler_view);
        emptyListImage = findViewById(R.id.empty_list_image);
        updateEmptyListImage();
        adapter = new PeopleInQueueRecyclerAdapter(this, users);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (b) {
            Application.getInstance(this).addMyQueuesListener(this::updateEmptyListImage);
            b = false;
        }
    }

    private void updateEmptyListImage() {
        if (users.size() == 0) {
            emptyListImage.setVisibility(View.VISIBLE);
        } else {
            emptyListImage.setVisibility(View.INVISIBLE);
        }
    }
}