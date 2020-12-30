package com.example.movieticketproject.Profiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.movieticketproject.Authentication.SignupFragment;
import com.example.movieticketproject.Models.Cinema;
import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.Models.Room;
import com.example.movieticketproject.Models.Ticket;
import com.example.movieticketproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FilmForm extends AppCompatActivity {

    EditText ed_filmName_addfilm,ed_filmprice_addfilm,ed_datepicker_addfilm,ed_timepicker_addfilm;
    Spinner spin_cinema_addfilm,spin_room_addfilm;
    Button btn_add_addfilm;
    DatabaseReference reference = null;
    Calendar cdate,ctime;
    DatePickerDialog dp;
    TimePickerDialog tp;
    int tmin,theures;
    String filmprice,filmdate,filmtime,filmcinema,filmroom;
    ArrayList<Cinema> cinemas;
    ArrayList<Room> room;
    Film film;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmform);

        Bundle b = getIntent().getExtras();
        film = (Film) b.get("film");

        ed_filmName_addfilm = findViewById(R.id.ed_filmName_addfilm);
        ed_filmprice_addfilm = findViewById(R.id.ed_filmprice_addfilm);
        ed_datepicker_addfilm = findViewById(R.id.ed_datepicker_addfilm);
        ed_timepicker_addfilm = findViewById(R.id.ed_timepicker_addfilm);
        spin_cinema_addfilm = findViewById(R.id.spin_cinema_addfilm);
        btn_add_addfilm = findViewById(R.id.btn_add_addfilm);
        spin_room_addfilm = findViewById(R.id.spin_room_addfilm);
        reference = FirebaseDatabase.getInstance().getReference();
        cinemas = new ArrayList<>();
        room = new ArrayList<>();
        tmin = 00;
        theures =00;
        showDataSpinner();

        ed_filmName_addfilm.setText(film.getName());



        ed_datepicker_addfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdate = Calendar.getInstance();
                int day = cdate.get(Calendar.DAY_OF_MONTH);
                int month = cdate.get(Calendar.MONTH);
                int year = cdate.get(Calendar.YEAR);
                dp = new DatePickerDialog(FilmForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int Myear, int Mmonth, int Mday) {
                        ed_datepicker_addfilm.setText(Mday + "/" + (Mmonth+1) + "/" + Myear);
                    }
                }, day, month, year);
                dp.show();
            }
        });




        ed_timepicker_addfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tp = new TimePickerDialog(FilmForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tmin = minute;
                        theures = hourOfDay;
                        ctime = Calendar.getInstance();
                        ctime.set(0,0,0,theures,tmin);
                        ed_timepicker_addfilm.setText(String.valueOf(theures)+":"+String.valueOf(tmin));

                    }
                },12,0,true);
                tp.updateTime(theures,tmin);
                tp.show();
            }

        });

        btn_add_addfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filmprice = ed_filmprice_addfilm.getText().toString();
                filmdate = ed_datepicker_addfilm.getText().toString();
                filmtime = ed_timepicker_addfilm.getText().toString();
                String x = filmdate + " "+filmtime;
                Cinema c = (Cinema)spin_cinema_addfilm.getSelectedItem();
                filmcinema = c.getId();
                Room r = (Room) spin_room_addfilm.getSelectedItem();
                filmroom = r.getId();

                film.setPrice(filmprice);
                film.setShowdatetime(x);
                film.setIdCinema(filmcinema);
                film.setIdRoom(filmroom);

                reference.child("Films").push().setValue(film);

                for (int i=0;i<=29;i++){
                    Ticket ticket = new Ticket();
                    String id = "T"+film.getId()+"R"+(i+1);
                    ticket.setId(id);
                    ticket.setIdFilm(film.getId());
                    ticket.setSold(0);
                    reference.child("Ticket").push().setValue(ticket);
                }
            }
        });


    }

    private void showDataSpinner() {
       reference.child("Cinema").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot item: snapshot.getChildren()){
                   cinemas.add(item.getValue(Cinema.class));
                   System.out.println(cinemas);
               }

               ArrayAdapter<Cinema> cinemaArrayAdapter = new ArrayAdapter<>(FilmForm.this,R.layout.custom_spinner,cinemas);
               spin_cinema_addfilm.setAdapter(cinemaArrayAdapter);
               spin_cinema_addfilm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       Cinema cinema = (Cinema)cinemas.get(position);
                       Query query = reference.child("Room").orderByChild("idCinema").equalTo(cinema.getId());
                       query.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if (snapshot.exists()){
                                   for (DataSnapshot data : snapshot.getChildren()){
                                       room.add(data.getValue(Room.class));
                                   }
                                   ArrayAdapter<Room> roomArrayAdapter = new ArrayAdapter<>(FilmForm.this,R.layout.custom_spinner,room);
                                   spin_room_addfilm.setAdapter(roomArrayAdapter);
                               }else{
                                   ArrayAdapter<Room> roomArrayAdapter = new ArrayAdapter<>(FilmForm.this,R.layout.custom_spinner,new ArrayList<>());
                                   spin_room_addfilm.setAdapter(roomArrayAdapter);
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
                   }


                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {

                   }
               });


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }


}