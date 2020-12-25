package com.example.movieticketproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieticketproject.Adapters.ImageViewerAdapter;
import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.Models.Retrofit.ImageAPI.Backdrop;
import com.example.movieticketproject.Models.Retrofit.ImageAPI.ImageModel;
import com.example.movieticketproject.Models.Retrofit.ImageAPI.Poster;
import com.example.movieticketproject.Models.Retrofit.ImageAPI.TMDBImage;
import com.example.movieticketproject.Models.Retrofit.Movie.MovieAPI;
import com.example.movieticketproject.Services.ApiInterface;
import com.example.movieticketproject.Services.Credentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {
    TextView TVName,TVRelease,TVRuntime,TVPrice,TVOverview,TVDatetime;
    ImageView backButton,thumbnail;
    static ViewPager2 images;
    Button btn_buyticket;
    String idMovie;
    Film RetFilm;
    MovieAPI movie;
    DatabaseReference reference = null;
    static ArrayList<ImageModel> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Bundle b= getIntent().getExtras();
        idMovie = b.getString("idMovie");
        imageList = new ArrayList<ImageModel>();

        TVName = findViewById(R.id.tv_name_details);
        TVRelease = findViewById(R.id.tv_releasedate_details);
        TVRuntime = findViewById(R.id.tv_runtime_details);
        TVPrice = findViewById(R.id.tv_price_details);
        TVOverview = findViewById(R.id.tv_overview_details);
        TVDatetime = findViewById(R.id.tv_datetime_details);

        backButton = findViewById(R.id.ic_back_details);
        thumbnail = findViewById(R.id.iv_thumbnail_detail);

        images = findViewById(R.id.vp_images_details);

        btn_buyticket = findViewById(R.id.btn_buyticket_details);
        movie = getMoviesDetails(idMovie);




        //imageList = movie.getImages();
        ImageViewerAdapter imageViewerAdapter = new ImageViewerAdapter(this,imageList);
        images.setAdapter(imageViewerAdapter);
        images.setClipToPadding(false);
        images.setClipChildren(false);
        images.setOffscreenPageLimit(3);
        images.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r+ 0.15f);
            }
        });
        images.setPageTransformer(compositePageTransformer);






        /*
        *
        *
        *   Actions
        *
        *
         */

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_buyticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public MovieAPI getMoviesDetails(String idMovie)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<MovieAPI> filmCall = apiInterface.getMovieDetails(idMovie,Credentials.API_KEY);
        filmCall.enqueue(new Callback<MovieAPI>() {
            @Override
            public void onResponse(Call<MovieAPI> call, Response<MovieAPI> response) {
                if(response.code() == 200)
                {
                    System.out.println("ResponseValid 200");
                    movie = response.body();
                    System.out.println("ID:"+String.valueOf(movie.getId()));

                    reference = FirebaseDatabase.getInstance().getReference().child("Films");
                    Query query = reference.orderByChild("id").equalTo(idMovie);
                    System.out.println(query.toString());
                    query.addValueEventListener(new ValueEventListener() {
                        Film film;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            System.out.println("3");
                            if(snapshot.exists())
                            {
                                DataSnapshot data = snapshot.getChildren().iterator().next();
                                film = data.getValue(Film.class);
                                RetFilm = film;

                                ArrayList<ImageModel> imageModels = new ArrayList<>();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(Credentials.BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                                Call<TMDBImage> tmdbImageCall = apiInterface.getMovieImages(idMovie,Credentials.API_KEY);
                                tmdbImageCall.enqueue(new Callback<TMDBImage>() {
                                    @Override
                                    public void onResponse(Call<TMDBImage> call, Response<TMDBImage> response) {
                                        if(response.code() == 200)
                                        {
                                            TMDBImage imageslist = response.body();
                                            for(Backdrop backdrop:imageslist.getBackdrops())
                                            {
                                                String imgURL = Credentials.IMAGE_BASE_URL+backdrop.getFilePath();
                                                ImageModel imageModel = new ImageModel();
                                                imageModel.setImage(imgURL);
                                                imageModels.add(imageModel);
                                            }
                                            movie.setImages(imageModels);
                                            imageList=imageModels;
                                            System.out.println(imageList.size());
                                            ImageViewerAdapter imageViewerAdapter = new ImageViewerAdapter(MovieDetails.this,imageList);
                                            images.setAdapter(imageViewerAdapter);
                                            images.setClipToPadding(false);
                                            images.setClipChildren(false);
                                            images.setOffscreenPageLimit(3);
                                            images.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                                            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                                            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                                            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                                @Override
                                                public void transformPage(@NonNull View page, float position) {
                                                    float r = 1 - Math.abs(position);
                                                    page.setScaleY(0.85f + r+ 0.15f);
                                                }
                                            });
                                            images.setPageTransformer(compositePageTransformer);

                                        }
                                        else
                                        {
                                            Toast.makeText(MovieDetails.this, "Error Performing API Access", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<TMDBImage> call, Throwable t) {
                                        System.out.println("HERE FAILURE IMAGE");
                                    }
                                });
                                System.out.println("HERE END");






                                movie.setShowdatetime(film.getShowdatetime());
                                movie.setPrice(film.getPrice());
                                System.out.println("Movie:"+movie);
                                String imgPath = "https://image.tmdb.org/t/p/original"+movie.getPoster_path();
                                Picasso.with(MovieDetails.this).load(imgPath).into(thumbnail);
                                //.with(MovieDetails.this).load(Credentials.IMAGE_BASE_URL+movie.getPoster_path()).into(thumbnail);
                                TVName.setText(movie.getTitle());
                                TVRelease.setText(movie.getReleaseDate());
                                int hour = movie.getRuntime() / 60;
                                int minutes = movie.getRuntime() % 60;
                                TVRuntime.setText(String.valueOf(hour)+"h"+String.valueOf(minutes)+"m");
                                TVPrice.setText(movie.getPrice()+"DT");
                                TVOverview.setText(movie.getOverview());
                                TVDatetime.setText(movie.getShowdatetime());

                            }
                            else
                            {
                                System.out.println("HERE 2");
                                RetFilm = new Film();
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("HERE 3");
                        }
                    });
                }
                else
                {
                    Toast.makeText(MovieDetails.this, "Error Performing API Access"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieAPI> call, Throwable t) {
                System.out.println("ResponseValid failed");
            }
        });
        return movie;
    }

    public ArrayList<ImageModel> getMoviesImages(String idMovie)
    {
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<TMDBImage> tmdbImageCall = apiInterface.getMovieImages(idMovie,Credentials.API_KEY);
        tmdbImageCall.enqueue(new Callback<TMDBImage>() {
            @Override
            public void onResponse(Call<TMDBImage> call, Response<TMDBImage> response) {
                if(response.code() == 200)
                {
                    System.out.println("HERE IMAGE");
                    TMDBImage images = response.body();
                    for(Poster poster:images.getPosters())
                    {
                        String imgURL = Credentials.IMAGE_BASE_URL+poster.getFilePath();
                        ImageModel imageModel = new ImageModel();
                        imageModel.setImage(imgURL);
                        imageModels.add(imageModel);
                    }

                }
                else
                {
                    Toast.makeText(MovieDetails.this, "Error Performing API Access", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TMDBImage> call, Throwable t) {
                System.out.println("HERE FAILURE IMAGE");
            }
        });
        System.out.println("HERE END");
        return imageModels;
    }

    public void getMovie(String idMovie)
    {
        reference = FirebaseDatabase.getInstance().getReference().child("Films");
        Query query = reference.orderByChild("id").equalTo(idMovie);
        System.out.println(query.toString());
        query.addValueEventListener(new ValueEventListener() {
            Film film;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("3");
                if(snapshot.exists())
                {
                    System.out.println("HERE 1");
                    film = snapshot.getValue(Film.class);
                    RetFilm = film;
                }
                else
                {
                    System.out.println("HERE 2");
                    RetFilm = new Film();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("HERE 3");
            }
        });
        System.out.println("4");

        //return RetFilm;
    }
}