package com.example.movieticketproject.Profiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.example.movieticketproject.Adapters.FilmRecyclerAdpater;
import com.example.movieticketproject.Decorators.CustomDecorator;
import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.R;
import com.example.movieticketproject.Services.SearchMovies;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddFilm extends AppCompatActivity {

    ImageView ic_back_addfilm;
    TextInputEditText tinp_searchfilm;
    static RecyclerView rv_listefilm_addfilm;
    ArrayList<Film> films;
    Context context;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);

        ic_back_addfilm = findViewById(R.id.ic_back_addfilm);
        tinp_searchfilm = findViewById(R.id.tinp_searchfilm);
        rv_listefilm_addfilm = findViewById(R.id.rv_listefilm_addfilm);
        CustomDecorator marginDec = new CustomDecorator(25, 25, 25, 0);
        rv_listefilm_addfilm.addItemDecoration(marginDec);
        films = new ArrayList<>();


        ic_back_addfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        tinp_searchfilm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchMovies searchMovies = new SearchMovies(AddFilm.this,  films, rv_listefilm_addfilm,s.toString());
                searchMovies.execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}