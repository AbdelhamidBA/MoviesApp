package com.example.movieticketproject.Profiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.movieticketproject.MainActivity;
import com.example.movieticketproject.R;

public class AdminProfile extends AppCompatActivity {

    CardView cv_adminprof1,cv_adminprof2,cv_adminprof3,cv_adminprof4;
    Context context;
    ImageView ic_back_addfilm;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);

        cv_adminprof1 = findViewById(R.id.cv_adminprof1);
        cv_adminprof2 = findViewById(R.id.cv_adminprof2);
        cv_adminprof3 = findViewById(R.id.cv_adminprof3);
        cv_adminprof4 = findViewById(R.id.cv_adminprof4);
        ic_back_addfilm = findViewById(R.id.ic_back_addfilm);

        pref = getSharedPreferences("current_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        ic_back_addfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Intent intent = new Intent(AdminProfile.this, MainActivity.class);
                startActivity(intent);
                AdminProfile.this.finish();
            }
        });


        cv_adminprof1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfile.this, AddFilm.class);
                startActivity(intent);
            }
        });

        cv_adminprof2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfile.this, AddCinema.class);
                startActivity(intent);
            }
        });

        cv_adminprof3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfile.this, AddRoom.class);
                startActivity(intent);
            }
        });

        cv_adminprof4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfile.this, Statistics.class);
                startActivity(intent);
            }
        });
    }
}