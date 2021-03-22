/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */
package com.openclassrooms.realestatemanager.model;

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
    @NonNull private String pathIcon;
    private String description;
    @NonNull private long idProperty;

    //Need empty constructor
    public Photo() {
    }
    public Photo(@NonNull String pathIcon, String description, @NonNull long idProperty) {
        this.pathIcon = pathIcon;
        this.description = description;
        this.idProperty = idProperty;
    }

    //GETTER
    @NonNull
    public long getId() {
        return id;
    }
    @NonNull
    public String getPathIcon() {
        return pathIcon;
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
    public void setPathIcon(@NonNull String pathIcon) {
        this.pathIcon = pathIcon;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setIdProperty(@NonNull long idProperty) {
        this.idProperty = idProperty;
    }

}
