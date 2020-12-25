package com.example.movieticketproject.Services;

import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.Models.Retrofit.ImageAPI.TMDBImage;
import com.example.movieticketproject.Models.Retrofit.Movie.MovieAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/{id}")
    Call<MovieAPI> getMovieDetails(@Path("id")String id, @Query("api_key")String apikey);
    @GET("movie/{id}/images")
    Call<TMDBImage> getMovieImages(@Path("id")String id, @Query("api_key")String apikey);
}
