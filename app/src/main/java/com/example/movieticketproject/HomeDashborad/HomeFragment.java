package com.example.movieticketproject.HomeDashborad;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieticketproject.Adapters.FilmRecyclerAdpater;
import com.example.movieticketproject.Decorators.CustomDecorator;
import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<Film> films;
    RecyclerView films_RV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        films_RV = v.findViewById(R.id.rv_films_home);
        films = new ArrayList<Film>();


        Film film = new Film();
        film.setId("1");
        film.setName("Joker");
        film.setThumbnail("https://images-na.ssl-images-amazon.com/images/I/71YrVzXizzL._AC_SL1200_.jpg");


        Film film2 = new Film();
        film2.setId("2");
        film2.setName("Frozen 2");
        film2.setThumbnail("https://images-na.ssl-images-amazon.com/images/I/91MpJvDr1WL.jpg");

        Film film3 = new Film();
        film3.setId("3");
        film3.setName("Joker");
        film3.setThumbnail("https://images-na.ssl-images-amazon.com/images/I/81f2QuPhhML._SL1500_.jpg");

        Film film4 = new Film();
        film4.setId("4");
        film4.setName("Joker");
        film4.setThumbnail("https://images-na.ssl-images-amazon.com/images/I/91VOg0YOlQL._SL1500_.jpg");

        films.add(film);
        films.add(film2);
        films.add(film3);
        films.add(film4);


        FilmRecyclerAdpater filmRecyclerAdpater = new FilmRecyclerAdpater(getActivity(),films);
        CustomDecorator marginDec = new CustomDecorator(25, 25, 25, 0);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        films_RV.setLayoutManager(manager);
        films_RV.addItemDecoration(marginDec);
        films_RV.setAdapter(filmRecyclerAdpater);

        return v;
    }



}