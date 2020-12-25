package com.example.movieticketproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.Models.Retrofit.Movie.MovieAPI;
import com.example.movieticketproject.MovieDetails;
import com.example.movieticketproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilmRecyclerAdpater extends RecyclerView.Adapter<FilmRecyclerAdpater.ViewHolder> {

    Context context;
    ArrayList<Film> films ;

    public FilmRecyclerAdpater(Context context,ArrayList<Film> films)
    {
        this.context = context;
        this.films = films;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        CardView view = (CardView)inflater.inflate(R.layout.film_cardview, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("Position:"+position);
        Picasso.with(context).load(films.get(position).getThumbnail()).resize(150,250).centerCrop().into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail,infoButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.film_thumbnail);
            thumbnail.setColorFilter(Color.argb(1,0,0,0));
            infoButton = itemView.findViewById(R.id.infobutton);

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetails.class);
                    intent.putExtra("idMovie","475557");
                    context.startActivity(intent);
                }
            });


        }
}


}
