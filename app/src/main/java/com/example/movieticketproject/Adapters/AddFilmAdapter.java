package com.example.movieticketproject.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddFilmAdapter extends RecyclerView.Adapter<AddFilmAdapter.ViewHolder> {

    Context context;
    ArrayList<Film> films ;

    public AddFilmAdapter(Context context, ArrayList<Film> films) {
        this.context = context;
        this.films = films;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        CardView view = (CardView)inflater.inflate(R.layout.addfilm_cardview, parent,false);
        return new AddFilmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film film = films.get(position);
        Picasso.with(context).load(films.get(position).getThumbnail()).resize(150,250).centerCrop().into(holder.film_thumbnail_addfilm);
        holder.addbutton_addfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bach nab3thou intent feha el film
            }
        });
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView film_thumbnail_addfilm,addbutton_addfilm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            film_thumbnail_addfilm = itemView.findViewById(R.id.film_thumbnail);
            film_thumbnail_addfilm.setColorFilter(Color.argb(1,0,0,0));
            addbutton_addfilm = itemView.findViewById(R.id.infobutton);
        }
    }
}
