package com.example.movieticketproject.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketproject.Adapters.FilmRecyclerAdpater;
import com.example.movieticketproject.Models.Film;
import com.example.movieticketproject.Profiles.AddFilm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchMovies extends AsyncTask {

    String query;
    static JSONObject jObj = null;
    static String json = "";
    ArrayList<Film> films;
    Context context;
    RecyclerView recyclerView;

    public SearchMovies(Context context, ArrayList<Film> films, RecyclerView recyclerView, String query)
    {
        this.context= context;
        this.films = films;
        this.recyclerView=recyclerView;
        this.query = query;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String urltoquery = "search/movie?api_key="+Credentials.API_KEY+"&query="+query+"";
        URL url;
        HttpURLConnection conn = null;
        try{


            url = new URL(Credentials.BASE_URL+urltoquery);
            conn = (HttpURLConnection)url.openConnection();

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "iso-8859-1"), 8);


                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                json = sb.toString();

            } catch (UnsupportedEncodingException e) {
                Log.e("UnsupportedEncoding", "UnsupportedEncodingException " + e.getMessage());
            } catch (IOException e) {
                Log.d("IoException Error", "Error converting result " +e.getMessage());
            }finally {
                if(conn != null)
                {
                    conn.disconnect();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jObj = new JSONObject(json);
            JSONArray array = jObj.getJSONArray("results");
            for(int i=0;i<array.length();i++)
            {
                JSONObject line = array.getJSONObject(i);
                String name = line.getString("title");
                String id = String.valueOf(line.getInt("id"));
                String releasedata = line.getString("release_date");
                String thumbnail = Credentials.IMAGE_BASE_URL+line.getString("poster_path");
                String description = line.getString("overview");
                Film film = new Film();
                film.setName(name);
                film.setThumbnail(thumbnail);
                film.setReleaseDate(releasedata);
                film.setId(id);
                film.setDescription(description);
                films.add(film);
            }

        } catch (JSONException e) {

            Log.e("JSON Parser", "Error parsing data " + conn.toString());

        }

        // return JSON String
        return jObj;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        FilmRecyclerAdpater filmRecyclerAdpater = new FilmRecyclerAdpater(context,films);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(filmRecyclerAdpater);

    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }
}
