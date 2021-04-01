/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */
package com.openclassrooms.realestatemanager.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "id",
        childColumns = "idProperty"))
public class Photo {

    @PrimaryKey(autoGenerate = true) @NonNull
    private long id;
    @NonNull private Bitmap bitmapPhoto;
    private String description;
    @NonNull private long idProperty;

    //Need empty constructor
    public Photo() {
    }
    public Photo(@NonNull Bitmap bitmapPhoto, String description, @NonNull long idProperty) {
        this.bitmapPhoto = bitmapPhoto;
        this.description = description;
        this.idProperty = idProperty;
    }

    //GETTER
    @NonNull
    public long getId() {
        return id;
    }
    @NonNull
    public Bitmap getBitmapPhoto() {
        return bitmapPhoto;
    }
    public String getDescription() {
        return description;
    }
    @NonNull
    public long getIdProperty() {
        return idProperty;
    }

    //SETTER
    public void setId(@NonNull long id) {
        this.id = id;
    }
    public void setBitmapPhoto(@NonNull Bitmap btmIcon) {
        this.bitmapPhoto = bitmapPhoto;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setIdProperty(@NonNull long idProperty) {
        this.idProperty = idProperty;
    }

}
