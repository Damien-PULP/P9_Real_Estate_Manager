/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */
package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PointOfInterest {

    @PrimaryKey (autoGenerate = true) public long id;
    private String name;
    private long idProperty;

    //Need empty constructor
    public PointOfInterest() {
    }
    public PointOfInterest(String name, long idProperty) {
        this.name= name;
        this.idProperty = idProperty;
    }

    //GETTER
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public long getIdProperty() {
        return idProperty;
    }

    //SETTER
    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    } 
    public void setIdProperty(long idProperty) {
        this.idProperty = idProperty;
    }
}
