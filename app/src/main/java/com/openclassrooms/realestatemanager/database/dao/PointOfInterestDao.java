/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.PropertyObj;

import java.util.List;

@Dao
public interface PointOfInterestDao {

    /** To Search Point by Name */
    @RawQuery(observedEntities = PointOfInterest.class)
    LiveData<List<PointOfInterest>> searchPointOfInterestWithFilter(SupportSQLiteQuery query);

    // Point of Interest
    @Query("SELECT * FROM PointOfInterest WHERE id = :idProperty")
    LiveData<List<PointOfInterest>> getPointsOfInterest(long idProperty);

    // TODO Multiple Name
    @Query("SELECT DISTINCT * FROM PointOfInterest")
    LiveData<List<PointOfInterest>> getAllPointsOfInterest();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    long insertPointOfInterest (PointOfInterest pointOfInterest);

    @Update
    int updatePointOfInterest(PointOfInterest pointOfInterest);

    @Query("DELETE FROM PointOfInterest WHERE id = :id")
    int deletePointOfInterest(long id);

    @Query("DELETE FROM PointOfInterest WHERE idProperty = :idProperty")
    int deletePointsOfInterestOfProperty(long idProperty);
}
