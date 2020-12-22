package com.example.movieticketproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.example.movieticketproject.Authentication.LoginFragment;
import com.example.movieticketproject.Authentication.SignupFragment;
import com.example.movieticketproject.HomeDashborad.HomeFragment;

public class HomeActivity extends AppCompatActivity {
    public static HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
        FragmentManager fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().add(R.id.home_main_fragment,homeFragment).commit();
    }
}