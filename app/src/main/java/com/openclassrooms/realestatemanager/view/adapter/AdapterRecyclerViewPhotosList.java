/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerViewPhotosList extends RecyclerView.Adapter<AdapterRecyclerViewPhotosList.ViewHolderPhoto> {

    private List<Photo> photos = new ArrayList<>();

    private Context context;

    @Override
    public ViewHolderPhoto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.item_photo, parent, false);
        return new AdapterRecyclerViewPhotosList.ViewHolderPhoto(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPhoto holder, int position) {
        holder.bind(photos.get(position));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void updateData (List<Photo> photos){
        this.photos = photos;
        notifyDataSetChanged();
    }

    static class ViewHolderPhoto extends RecyclerView.ViewHolder {

        private ImageView picture;
        private TextView description;

        public ViewHolderPhoto(@NonNull View itemView) {
            super(itemView);
            this.picture = itemView.findViewById(R.id.item_photo_picture);
            this.description = itemView.findViewById(R.id.item_photo_description);
        }

        public void bind(Photo photo){
            description.setText(photo.getDescription());
            picture.setImageBitmap(photo.getBitmapPhoto());
        }
    }
}
