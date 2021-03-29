package com.SuperClub.EQ.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.SuperClub.EQ.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_navigation);

        chipNavigationBar = findViewById(R.id.bottom_navigation_menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MainMenuMiddleFragment()).commit();
        chipNavigationBar.setItemSelected(R.id.bottom_nav_2, true);
        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_1:
                        fragment = new MainMenuLeftFragment();
                        break;
                    case R.id.bottom_nav_2:
                        fragment = new MainMenuMiddleFragment();
                        break;
                    case R.id.bottom_nav_3:
                        fragment = new MainMenuRightFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            }
        });
    }
}