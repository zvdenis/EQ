package com.SuperClub.EQ.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.R;

public class AdminQueues extends AppCompatActivity {

    private static boolean b = true;
    RecyclerView recyclerView;
    AdminQueuesRecyclerAdapter adapter;
    LinearLayout emptyListImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_queues);
        recyclerView = findViewById(R.id.recycler_view);
        emptyListImage = findViewById(R.id.empty_list_image);
        updateEmptyListImage();
        adapter = new AdminQueuesRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (b) {
            Application.getInstance(this).addMyQueuesListener(this::updateEmptyListImage);
            b = false;
        }
    }

    private void updateEmptyListImage() {
        if (Application.getInstance(this).adminQueues.size() == 0) {
            emptyListImage.setVisibility(View.VISIBLE);
        } else {
            emptyListImage.setVisibility(View.INVISIBLE);
        }
    }
}