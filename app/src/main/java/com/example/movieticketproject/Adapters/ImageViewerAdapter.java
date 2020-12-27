package com.example.movieticketproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketproject.Models.Retrofit.ImageAPI.ImageModel;
import com.example.movieticketproject.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageViewerAdapter extends RecyclerView.Adapter<ImageViewerHolder>{
    ArrayList<ImageModel> imageList;
    Context context;

    public ImageViewerAdapter(Context context,ArrayList<ImageModel> imageList)
    {
        this.context = context;
        this.imageList =  imageList;
    }


    @NonNull
    @Override
    public ImageViewerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ImageViewerHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.custom_image_details,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewerHolder holder, int position) {
        Picasso.with(context).load(imageList.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}

class ImageViewerHolder extends RecyclerView.ViewHolder{
    RoundedImageView imageView;
    public ImageViewerHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageslide);
    }
}
