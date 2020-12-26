package com.example.movieticketproject.HomeDashborad;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieticketproject.HomeActivity;
import com.example.movieticketproject.Models.Cinema;
import com.example.movieticketproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class LocationFragment extends Fragment {
    Spinner cinemaList ;
    TextView btn_select;
    GoogleMap map = null;
    ArrayList<Cinema> cinemas ;
    DatabaseReference reference = null;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            try {
                map = googleMap;
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                Cinema cinema = HomeFragment.targetCinema;
                LatLng currentLocation = new LatLng(cinema.getLatitude(),cinema.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,50));
                map.animateCamera(CameraUpdateFactory.zoomIn());
                map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle));

                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
                reference = FirebaseDatabase.getInstance().getReference().child("Cinema");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot data :snapshot.getChildren())
                            {
                                Cinema cinema = data.getValue(Cinema.class);
                                map.addMarker(new MarkerOptions().position(new LatLng(cinema.getLatitude(),cinema.getLongitude())).title(cinema.getName()));
                            }

                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Cancelled Pülling", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);
            }

           // LatLng sydney = new LatLng(-34, 151);

            //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_location, container, false);
        cinemaList = v.findViewById(R.id.sp_cinema_list);
        btn_select = v.findViewById(R.id.tv_cinemaselect);
        btn_select.setEnabled(false);
        cinemas = new ArrayList<>();
        Cinema empty = new Cinema();
        empty.setName("Select Cinema");
        cinemas.add(empty);
        reference = FirebaseDatabase.getInstance().getReference().child("Cinema");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot data :snapshot.getChildren())
                    {
                        Cinema cinema = data.getValue(Cinema.class);
                        cinemas.add(cinema);
                    }
                    ArrayAdapter<Cinema> arrayAdapter = new ArrayAdapter<Cinema>(getActivity(), R.layout.custom_spinner,cinemas);
                    cinemaList.setAdapter(arrayAdapter);
                    cinemaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position !=0)
                            {
                                btn_select.setEnabled(true);
                                Cinema c = cinemas.get(position);
                                LatLng latLng = new LatLng(c.getLatitude(),c.getLongitude());
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                            }
                            else
                            {
                                btn_select.setEnabled(false);
                            }

                            //map.animateCamera(CameraUpdateFactory.zoomIn());
                            //map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
                else
                {
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Cancelled Pülling", Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction= getActivity().getSupportFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putSerializable("MapCinema",(Cinema)cinemaList.getSelectedItem());
                HomeActivity.homeFragment.setArguments(b);
                fragmentTransaction.replace(R.id.home_main_fragment,HomeActivity.homeFragment).commit();
                HomeActivity.navBar.setItemActiveIndex(0);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}