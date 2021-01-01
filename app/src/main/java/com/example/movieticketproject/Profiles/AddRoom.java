package com.example.movieticketproject.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.movieticketproject.Models.Cinema;
import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.Models.Room;
import com.example.movieticketproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddRoom extends AppCompatActivity {

    DatabaseReference reference = null;
    Spinner Spinner_addroom;
    Button btn_addroom;
    ArrayList<Cinema> cinemas;
    ImageView ic_back_addfilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
        Spinner_addroom = findViewById(R.id.Spinner_addroom);
        btn_addroom = findViewById(R.id.btn_addroom);
        ic_back_addfilm = findViewById(R.id.ic_back_addfilm);
        reference = FirebaseDatabase.getInstance().getReference();
        cinemas = new ArrayList<>();
        showDataSpinner();

        ic_back_addfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRoom.this.finish();
            }
        });
    }

    private void showDataSpinner() {
        Cinema x = new Cinema();
        x.setName("Select Cinema");
        cinemas.add(x);
        reference.child("Cinema").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //cinemas.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    cinemas.add(item.getValue(Cinema.class));
                    System.out.println(cinemas);
                }

                ArrayAdapter<Cinema> cinemaArrayAdapter = new ArrayAdapter<>(AddRoom.this,R.layout.custom_spinner,cinemas);
                Spinner_addroom.setAdapter(cinemaArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_addroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Spinner_addroom.getSelectedItemPosition() != 0)
                {
                    btn_addroom.setEnabled(true);
                Cinema cinema = (Cinema)Spinner_addroom.getSelectedItem();
                Query query = reference.child("Room").orderByChild("idCinema").equalTo(cinema.getId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long count = 0;
                        if(snapshot.exists())
                        {
                            count = snapshot.getChildrenCount();
                            String roomname = "R"+String.valueOf(count+1); // 10
                            String id =cinema.getId()+roomname;
                            Room room = new Room();
                            room.setId(id);
                            room.setNumber(roomname);
                            room.setIdCinema(cinema.getId());
                            reference.child("Room").push().setValue(room);
                            Toast.makeText(AddRoom.this, "Room "+room.getNumber()+" Has Been Added to "+cinema.getName(), Toast.LENGTH_SHORT).show();
                        }else
                        {
                            String roomname = "R"+String.valueOf(count+1); // 10
                            String id =cinema.getId()+roomname;
                            Room room = new Room();
                            room.setId(id);
                            room.setNumber(roomname);
                            room.setIdCinema(cinema.getId());
                            reference.child("Room").push().setValue(room);
                            Toast.makeText(AddRoom.this, "Room "+room.getNumber()+" Has Been Added to "+cinema.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                }
                else
                {
                    btn_addroom.setEnabled(false);
                }
            }
        });
    }
}