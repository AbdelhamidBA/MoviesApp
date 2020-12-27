package com.example.movieticketproject.HomeDashborad;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieticketproject.Adapters.FilmRecyclerAdpater;
import com.example.movieticketproject.Decorators.CustomDecorator;
import com.example.movieticketproject.HomeActivity;
import com.example.movieticketproject.Models.Cinema;
import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.Models.Retrofit.ImageAPI.ImageModel;
import com.example.movieticketproject.R;
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

public class HomeFragment extends Fragment {
    TextView cinemaName;
    ArrayList<Film> films;
    RecyclerView films_RV;
    ImageView iconPosition;
    TextView emptyView;
    private double myLat, myLong;
    float closetDistance = 0;
    DatabaseReference reference = null;
    public static Cinema targetCinema = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        films_RV = v.findViewById(R.id.rv_films_home);
        films = new ArrayList<Film>();
        cinemaName = v.findViewById(R.id.tv_theater_name);
        iconPosition = v.findViewById(R.id.changelocIV);
        emptyView = v.findViewById(R.id.hf_emptyview);
        Bundle b = this.getArguments();

        if (b != null) {
            System.out.println("Bundle Exection");
            Cinema cinMap = (Cinema) b.get("MapCinema");
            targetCinema = cinMap;
            cinemaName.setText(targetCinema.getName());
            films.clear();
            reference = FirebaseDatabase.getInstance().getReference().child("Films");
            Query query = reference.orderByChild("idCinema").equalTo(targetCinema.getId());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Film film = data.getValue(Film.class);
                        films.add(film);
                    }
                    System.out.println("Size:" + films.size());
                    FilmRecyclerAdpater filmRecyclerAdpater = new FilmRecyclerAdpater(getActivity(), films);
                    CustomDecorator marginDec = new CustomDecorator(25, 25, 25, 0);
                    GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
                    films_RV.setLayoutManager(manager);
                    films_RV.addItemDecoration(marginDec);
                    films_RV.setAdapter(filmRecyclerAdpater);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            System.out.println("Location Exection");
            FusedLocationProviderClient mClient = LocationServices.getFusedLocationProviderClient(getActivity());
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            }
            mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        myLat = location.getLatitude();
                        myLong = location.getLongitude();
                        reference = FirebaseDatabase.getInstance().getReference().child("Cinema");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                double cinLat = 0, cinLong = 0;
                                Location myLocation = new Location("");
                                myLocation.setLatitude(myLat);
                                myLocation.setLongitude(myLong);
                                for (DataSnapshot datas : snapshot.getChildren()) {
                                    if (closetDistance == 0) {
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

                                    System.out.println("ID:" + datas.child("id").getValue(String.class) + "Distance:" + myLocation.distanceTo(cinemaLocation));
                                    if (myLocation.distanceTo(cinemaLocation) < closetDistance) {
                                        closetDistance = myLocation.distanceTo(cinemaLocation);
                                        targetCinema = datas.getValue(Cinema.class);
                                    }

                                }
                                cinemaName.setText(targetCinema.getName());
                                System.out.println("TargetID:" + targetCinema.getId());
                                films.clear();
                                reference = FirebaseDatabase.getInstance().getReference().child("Films");
                                Query query = reference.orderByChild("idCinema").equalTo(targetCinema.getId());
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                        {
                                            emptyView.setVisibility(View.GONE);
                                            for (DataSnapshot data : snapshot.getChildren()) {
                                                Film film = data.getValue(Film.class);
                                                films.add(film);
                                            }
                                            System.out.println("Size:" + films.size());
                                            FilmRecyclerAdpater filmRecyclerAdpater = new FilmRecyclerAdpater(getActivity(), films);
                                            CustomDecorator marginDec = new CustomDecorator(25, 25, 25, 0);
                                            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
                                            films_RV.setLayoutManager(manager);
                                            films_RV.addItemDecoration(marginDec);
                                            films_RV.setAdapter(filmRecyclerAdpater);
                                        }
                                        else
                                        {
                                            films = new ArrayList<>();
                                            FilmRecyclerAdpater filmRecyclerAdpater = new FilmRecyclerAdpater(getActivity(), films);
                                            films_RV.setAdapter(filmRecyclerAdpater);
                                            emptyView.setVisibility(View.VISIBLE);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                    }
                }
            });


        }

        iconPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closetDistance=0;
                FusedLocationProviderClient mClient = LocationServices.getFusedLocationProviderClient(getActivity());
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                }
                mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            myLat = location.getLatitude();
                            myLong = location.getLongitude();
                            reference = FirebaseDatabase.getInstance().getReference().child("Cinema");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    double cinLat = 0, cinLong = 0;
                                    Location myLocation = new Location("");
                                    myLocation.setLatitude(myLat);
                                    myLocation.setLongitude(myLong);
                                    System.out.println("Latitude:"+myLat);
                                    System.out.println("Longitude"+myLong);
                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                        if (closetDistance == 0) {
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

                                        System.out.println("ID:" + datas.child("id").getValue(String.class) + "Distance:" + myLocation.distanceTo(cinemaLocation));
                                        if (myLocation.distanceTo(cinemaLocation) < closetDistance) {
                                            closetDistance = myLocation.distanceTo(cinemaLocation);
                                            targetCinema = datas.getValue(Cinema.class);
                                        }

                                    }
                                    cinemaName.setText(targetCinema.getName());
                                    System.out.println("TargetID:" + targetCinema.getId());
                                    films.clear();
                                    reference = FirebaseDatabase.getInstance().getReference().child("Films");
                                    Query query = reference.orderByChild("idCinema").equalTo(targetCinema.getId());
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                emptyView.setVisibility(View.GONE);
                                                for (DataSnapshot data : snapshot.getChildren()) {
                                                    Film film = data.getValue(Film.class);
                                                    films.add(film);
                                                }
                                                System.out.println("Size:" + films.size());
                                                FilmRecyclerAdpater filmRecyclerAdpater = new FilmRecyclerAdpater(getActivity(), films);
                                                CustomDecorator marginDec = new CustomDecorator(25, 25, 25, 0);
                                                GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
                                                films_RV.setLayoutManager(manager);
                                                films_RV.addItemDecoration(marginDec);
                                                films_RV.setAdapter(filmRecyclerAdpater);

                                            }
                                            else
                                            {
                                                films = new ArrayList<>();
                                                FilmRecyclerAdpater filmRecyclerAdpater = new FilmRecyclerAdpater(getActivity(), films);
                                                films_RV.setAdapter(filmRecyclerAdpater);
                                                emptyView.setVisibility(View.VISIBLE);
                                            }
                                            Toast.makeText(getActivity(), "Closest Cinema to your position is Selected", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });

            }

        });
        return v;
    }
}