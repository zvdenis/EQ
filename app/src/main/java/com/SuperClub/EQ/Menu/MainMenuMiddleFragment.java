package com.SuperClub.EQ.Menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.SuperClub.EQ.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuMiddleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuMiddleFragment extends Fragment {

    FloatingActionButton createQueueButton;
    Button searchButton;
    Button qrButton;
    EditText codeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu, container, false);
        searchButton = root.findViewById(R.id.search_button);
        codeText = root.findViewById(R.id.code_text);
        qrButton = root.findViewById(R.id.qr_button);
        createQueueButton = root.findViewById(R.id.create_queue);
        createQueueButton.setOnClickListener(this::createQueueButtonClick);

        searchButton.setOnClickListener(this::searchButtonClick);
        qrButton.setOnClickListener(this::qrButtonClick);

        return root;
    }

    private void createQueueButtonClick(View view) {
        Intent intent = new Intent(getActivity(), CreateQueueActivity.class);
        startActivity(intent);
    }

    public void searchButtonClick(View view) {

    }

    public void qrButtonClick(View view) {

    }

}