package com.example.movieticketproject.Models;

public class Film {
    private String id;
    private String name;
    private String description;
    private String releaseDate;
    private String runtime;
    private String thumbnail;
    private String price;
    private String showdatetime;
    private String idCinema;
    private String idRoom;


    public Film() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getShowdatetime() {
        return showdatetime;
    }

    public void setShowdatetime(String showdatetime) {
        this.showdatetime = showdatetime;
    }

    public String getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(String idCinema) {
        this.idCinema = idCinema;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", runtime='" + runtime + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", price='" + price + '\'' +
                ", showdatetime='" + showdatetime + '\'' +
                ", idCinema='" + idCinema + '\'' +
                ", idRoom='" + idRoom + '\'' +
                '}';
    }
}
