package com.SuperClub.EQ.Menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SuperClub.EQ.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuLeftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuLeftFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu_left, container, false);
    }
}