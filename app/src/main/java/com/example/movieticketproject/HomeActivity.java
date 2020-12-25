package com.example.movieticketproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.movieticketproject.Authentication.LoginFragment;
import com.example.movieticketproject.Authentication.SignupFragment;
import com.example.movieticketproject.HomeDashborad.HistoryFragment;
import com.example.movieticketproject.HomeDashborad.HomeFragment;
import com.example.movieticketproject.HomeDashborad.LocationFragment;
import com.example.movieticketproject.Models.Cinema;
import com.example.movieticketproject.Models.Film;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class HomeActivity extends AppCompatActivity {
    public static HomeFragment homeFragment;
    public static LocationFragment locationFragment;
    public static HistoryFragment historyFragment;
    public static Cinema targetCinema = null;
    public static ArrayList<Film> films = new ArrayList<Film>();
    private boolean permission_location = false;
    DatabaseReference reference = null;
    Fragment fragment = null;
    float closetDistance = 0;
    SmoothBottomBar navBar;
    private double myLat,myLong;
    /*

        Permission Check

     */

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            permission_location = true;
        }
    }

    /*

        OnCreate

     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navBar = findViewById(R.id.smoothBottomBar);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
        FragmentManager fragmentManager = getSupportFragmentManager();
        reference=FirebaseDatabase.getInstance().getReference().child("Films");
        Film f = new Film();
        f.setId("330457");
        f.setName("Frozen II");
        f.setPrice("100");
        f.setShowdatetime("21/01/2021 14:00");
        //reference.push().setValue(f);
        homeFragment = new HomeFragment();
        locationFragment = new LocationFragment();
        historyFragment = new HistoryFragment();
        navBar.setItemActiveIndex(0);
        fragmentManager.beginTransaction().add(R.id.home_main_fragment,homeFragment).commit();
        navBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch (i){
                    case 0:
                        fragmentManager.beginTransaction().replace(R.id.home_main_fragment,homeFragment).commit();
                        break;
                    case 1:
                        fragmentManager.beginTransaction().replace(R.id.home_main_fragment,historyFragment).commit();
                        break;
                    case 2:
                        fragmentManager.beginTransaction().replace(R.id.home_main_fragment,locationFragment).commit();
                        break;
                }
                return true;
            }
        });
    }

    /*

        Get Suitable Cinema

     */

    public void getSuitableCinema(){

        FusedLocationProviderClient mClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        mClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                {
                    myLat = location.getLatitude();
                    myLong = location.getLongitude();
                }
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("Cinema");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double cinLat=0,cinLong=0;
                Location myLocation = new Location("");
                myLocation.setLatitude(myLat);
                myLocation.setLongitude(myLong);
                for(DataSnapshot datas : snapshot.getChildren())
                {
                    if(closetDistance == 0)
                    {
                        cinLat = datas.child("latitude").getValue(double.class);
                        cinLong = datas.child("longitude").getValue(double.class);


                        Location cinemaLocation = new Location("");
                        cinemaLocation.setLatitude(cinLat);
                        cinemaLocation.setLongitude(cinLong);

                        closetDistance = myLocation.distanceTo(cinemaLocation);
                        targetCinema = datas.getValue(Cinema.class);
                    }

                    cinLat = datas.child("latitude").getValue(double.class);
                    cinLong = datas.child("longitude").getValue(double.class);

                    Location cinemaLocation = new Location("");
                    cinemaLocation.setLatitude(cinLat);
                    cinemaLocation.setLongitude(cinLong);

                    if(myLocation.distanceTo(cinemaLocation) < closetDistance)
                    {
                        closetDistance = myLocation.distanceTo(cinemaLocation);
                        targetCinema = datas.getValue(Cinema.class);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*

        Get Suitable Cinema

     */


    public void getListOfMovies()
    {
        films.clear();
        reference = FirebaseDatabase.getInstance().getReference().child("Film");
        Query query = reference.orderByChild("idCinema").equalTo(targetCinema.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    Film film = data.getValue(Film.class);
                    films.add(film);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*

        Permission Result

     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                permission_location = true;
            }
            else
            {
                permission_location = false;
            }
        }
    }
}