package com.example.movieticketproject.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.movieticketproject.Authentication.SignupFragment;
import com.example.movieticketproject.Models.Cinema;
import com.example.movieticketproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCinema extends AppCompatActivity {

    TextInputEditText edlat_addcin,edlon_addcin,edad_addcin,edname_addcin;
    Button btn_addcinema;
    String name,addresse,longitude,latitude;
    DatabaseReference reference = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cinema);


        edlat_addcin = findViewById(R.id.edlat_addcin);
        edlon_addcin = findViewById(R.id.edlon_addcin);
        edad_addcin = findViewById(R.id.edadd_addcin);
        edname_addcin = findViewById(R.id.edname_addcin);
        btn_addcinema = findViewById(R.id.btn_addcinema);
        reference = FirebaseDatabase.getInstance().getReference().child("Cinema");


        btn_addcinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = edname_addcin.getText().toString();
                addresse = edad_addcin.getText().toString();
                longitude = edlon_addcin.getText().toString();
                latitude = edlat_addcin.getText().toString();

                if (name.equals("")){
                    edname_addcin.setError("Enter a valid Fullname");
                } else if (addresse.equals("")){
                    edad_addcin.setError("Enter a valid Fullname");
                }else if (longitude.equals("")){
                    edlon_addcin.setError("Enter a valid Fullname");
                }else if (latitude.equals("")){
                    edlat_addcin.setError("Enter a valid Fullname");
                }else {
                    name = "Cinema " + name;
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long count = 0;
                            if (snapshot.exists()){
                                count = snapshot.getChildrenCount() + 1 + 100000;
                                Cinema cinema = new Cinema();
                                cinema.setId(String.valueOf(count));
                                cinema.setName(name);
                                cinema.setAddress(addresse);
                                cinema.setLatitude(Double.parseDouble(latitude));
                                cinema.setLongitude(Double.parseDouble(longitude));

                                reference.push().setValue(cinema);
                                Toast.makeText(AddCinema.this, "cinema Successfully added", Toast.LENGTH_SHORT).show();
                                AddCinema.this.finish();


                            }else
                            {
                                count = 100001;
                                Cinema cinema = new Cinema();
                                cinema.setId(String.valueOf(count));
                                cinema.setName(name);
                                cinema.setAddress(addresse);
                                cinema.setLatitude(Double.parseDouble(latitude));
                                cinema.setLongitude(Double.parseDouble(longitude));

                                reference.push().setValue(cinema);
                                Toast.makeText(AddCinema.this, "cinema Successfully added", Toast.LENGTH_SHORT).show();
                                AddCinema.this.finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
        });

    }
}