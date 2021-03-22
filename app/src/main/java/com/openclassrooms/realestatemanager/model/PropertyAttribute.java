/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PropertyAttribute {

    public Property property;
    public List<PointOfInterest> pointsOfInterest;
    public List<Photo> photos;
    public Address address;
    public User agent;

    public PropertyAttribute() {
    }

    public PropertyAttribute(Property property, List<PointOfInterest> pointsOfInterest, List<Photo> photos, Address address, User agent) {
        this.property = property;
        this.pointsOfInterest = pointsOfInterest;
        this.photos = photos;
        this.address = address;
        this.agent = agent;
    }

    // GETTER
    public Property getProperty() {
        return property;
    }
    public List<PointOfInterest> getPointsOfInterest() {
        return pointsOfInterest;
    }
    public List<Photo> getPhotos() {
        return photos;
    }
    public Address getAddress() {
        return address;
    }
    public User getAgent() {
        return agent;
    }

    //SETTER
    public void setProperty(Property property) {
        this.property = property;
    }
    public void setPointsOfInterest(List<PointOfInterest> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public void setAgent(User agent) {
        this.agent = agent;
    }
}

