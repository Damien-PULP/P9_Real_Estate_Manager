/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PropertyObj {
    @Embedded
    public Property property;
    @Relation(
            entity = Address.class,
            parentColumn = "id",
            entityColumn = "idProperty"
    )
    public Address address;
    @Relation(
            entity = Photo.class,
            parentColumn = "id",
            entityColumn = "idProperty"
    )
    public List<Photo> photos;
    @Relation(
            entity = PointOfInterest.class,
            parentColumn = "id",
            entityColumn = "idProperty"
    )
    public List<PointOfInterest> pointOfInterests;

    //GETTER
    public Property getProperty() {
        return property;
    }
    public Address getAddress() {
        return address;
    }
    public List<Photo> getPhotos() {
        return photos;
    }
    public List<PointOfInterest> getPointOfInterests() {
        return pointOfInterests;
    }
}
