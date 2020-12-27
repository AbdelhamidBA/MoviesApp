package com.example.movieticketproject.Models.Retrofit.Movie;

import com.example.movieticketproject.Models.Retrofit.ImageAPI.ImageModel;
import com.example.movieticketproject.Models.Retrofit.ImageAPI.TMDBImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieAPI implements Serializable
{
private String poster_path;
private String backdrop_path;
private Integer id;
private String original_title;
private String overview;
private String release_date;
private Integer runtime;
private String title;
private String price;
private String showdatetime;
private ArrayList<ImageModel> images;
private final static long serialVersionUID = -6438542834312631114L;

public String getBackdropPath() {
return backdrop_path;
}

public void setBackdropPath(String backdropPath) {
this.backdrop_path = backdropPath;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getOriginalTitle() {
return original_title;
}

public void setOriginalTitle(String originalTitle) {
this.original_title = originalTitle;
}

public String getOverview() {
return overview;
}

public void setOverview(String overview) {
this.overview = overview;
}

public String getReleaseDate() {
return release_date;
}

public void setReleaseDate(String releaseDate) {
this.release_date = releaseDate;
}

public Integer getRuntime() {
return runtime;
}

public void setRuntime(Integer runtime) {
this.runtime = runtime;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getPrice() {
        return price;
    }

public void setPrice(String price) {
        this.price = price;
    }

public String getShowdatetime() {
        return showdatetime;
    }

public void setShowdatetime(String showdatetime) {
        this.showdatetime = showdatetime;
    }

public ArrayList<ImageModel> getImages() {
        return images;
    }

public void setImages(ArrayList<ImageModel> images) {
        this.images = images;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public String toString() {
        return "MovieAPI{" +
                "backdropPath='" + backdrop_path + '\'' +
                ", id=" + id +
                ", originalTitle='" + original_title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + release_date + '\'' +
                ", runtime=" + runtime +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", showdatetime='" + showdatetime + '\'' +
                ", images=" + images +
                '}';
    }
}