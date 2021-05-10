/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerViewPhotosList extends RecyclerView.Adapter<AdapterRecyclerViewPhotosList.ViewHolderPhoto> {

    private List<Photo> photos = new ArrayList<>();

    private Context context;
    private final boolean isLittleView;
    private final MainViewModel mainViewModel;

    public AdapterRecyclerViewPhotosList(boolean isLittleView, MainViewModel mainViewModel) {
        this.isLittleView = isLittleView;
        this.mainViewModel = mainViewModel;
    }

    @NonNull
    @Override
    public ViewHolderPhoto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);
        View view = inflate.inflate(R.layout.item_photo, parent, false);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        final float scale = context.getResources().getDisplayMetrics().density;

        if(isLittleView) {
            int pixelsLt = (int) (100 * scale + 0.5f);
            params.width = pixelsLt;
            params.height = pixelsLt;
        }else{
            params.height = (int) (350 * scale + 0.5f);
        }
        view.setLayoutParams(params);

        return new AdapterRecyclerViewPhotosList.ViewHolderPhoto(view,  this);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderPhoto holder, int position) {
        holder.bind(photos.get(position), isLittleView, context);
    }
    @Override
    public int getItemCount() {
        return photos.size();
    }

    //call to change data
    public void updateData (List<Photo> photos){
        this.photos = photos;
        notifyDataSetChanged();
    }
    public void removeAPhoto(Photo photo){
        mainViewModel.removeAPhotoOfTheProperty(photo);
        this.photos.remove(photo);
        notifyDataSetChanged();
    }
    public void addAPhoto (Photo photo){
        this.photos.add(photo);
        notifyDataSetChanged();
    }

    //VIEW HOLDER of this adapter
    static class ViewHolderPhoto extends RecyclerView.ViewHolder {

        private final ImageView picture;
        private final TextView description;
        private final AdapterRecyclerViewPhotosList adapter;

        public ViewHolderPhoto(@NonNull View itemView, AdapterRecyclerViewPhotosList adapter) {
            super(itemView);
            this.picture = itemView.findViewById(R.id.item_photo_picture);
            this.description = itemView.findViewById(R.id.item_photo_description);
            this.adapter = adapter;
        }

        public void bind(Photo photo, boolean isLittleView, Context context) {
            description.setText(photo.getDescription());
            picture.setImageBitmap(photo.getBitmapPhoto());
            if (!isLittleView) {
                final float scale = context.getResources().getDisplayMetrics().density;
                int pixelsPadding = (int) (15 * scale + 0.5f);
                description.setPadding(pixelsPadding, pixelsPadding, pixelsPadding, pixelsPadding);
                description.setTextSize(30);
            }else{
                itemView.setOnLongClickListener(v -> {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.msg_sure_remove_picture);
                    builder.setPositiveButton(R.string.positive_response,(dialogInterface, i) -> adapter.removeAPhoto(photo));
                    builder.setNegativeButton(R.string.negative_response, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                });
            }
        }
    }
}
