/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.model;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PropertyAttribute {

    public LiveData<Property> property;
    public LiveData<List<PointOfInterest>> pointsOfInterest;
    public LiveData<List<Photo>> photos;
    public LiveData<Address> address;
    public LiveData<User> agent;

    public PropertyAttribute() {
    }

    public PropertyAttribute(LiveData<Property> property, LiveData<List<PointOfInterest>> pointsOfInterest, LiveData<List<Photo>> photos, LiveData<Address> address, LiveData<User> agent) {
        this.property = property;
        this.pointsOfInterest = pointsOfInterest;
        this.photos = photos;
        this.address = address;
        this.agent = agent;
    }

    // GETTER
    public LiveData<Property> getProperty() {
        return property;
    }
    public LiveData<List<PointOfInterest>> getPointsOfInterest() {
        return pointsOfInterest;
    }
    public LiveData<List<Photo>> getPhotos() {
        return photos;
    }
    public LiveData<Address> getAddress() {
        return address;
    }
    public LiveData<User> getAgent() {
        return agent;
    }

    //SETTER
    public void setProperty(LiveData<Property> property) {
        this.property = property;
    }
    public void setPointsOfInterest(LiveData<List<PointOfInterest>> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }
    public void setPhotos(LiveData<List<Photo>> photos) {
        this.photos = photos;
    }
    public void setAddress(LiveData<Address> address) {
        this.address = address;
    }
    public void setAgent(LiveData<User> agent) {
        this.agent = agent;
    }

}

