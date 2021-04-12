package com.SuperClub.EQ.Menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuLeftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuLeftFragment extends Fragment {

    private static boolean b = true;
    RecyclerView recyclerView;
    MyQueuesRecyclerAdapter adapter;
    LinearLayout emptyListImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu_left, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        emptyListImage = root.findViewById(R.id.empty_list_image);
        updateEmptyListImage();
        adapter = new MyQueuesRecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(b) {
            Application.getInstance(getContext()).addMyQueuesListener(this::updateEmptyListImage);
            b = false;
        }
        return root;
    }

    private void updateEmptyListImage() {
        if (Application.getInstance(getContext()).myQueues.size() == 0) {
            emptyListImage.setVisibility(View.VISIBLE);
        } else {
            emptyListImage.setVisibility(View.INVISIBLE);
        }
    }
}