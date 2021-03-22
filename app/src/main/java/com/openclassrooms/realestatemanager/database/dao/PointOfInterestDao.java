/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;

import java.util.List;

@Dao
public interface PointOfInterestDao {

    // Point of Interest
    @Query("SELECT * FROM PointOfInterest WHERE id = :idProperty")
    LiveData<List<PointOfInterest>> getPointsOfInterest(long idProperty);

    @Insert
    long insertPointOfInterest (PointOfInterest pointOfInterest);

    @Update
    int updatePointOfInterest(PointOfInterest pointOfInterest);

    @Query("DELETE FROM PointOfInterest WHERE id = :id")
    int deletePointOfInterest(long id);
}
