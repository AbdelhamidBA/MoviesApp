package com.example.movieticketproject.Models;

public class Ticket {
    private String id;
    private String idFilm;
    private int sold;

    public Ticket() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdFilm(String idFilm) {
        this.idFilm = idFilm;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getIdFilm() {
        return idFilm;
    }

    public int getSold() {
        return sold;
    }
}
