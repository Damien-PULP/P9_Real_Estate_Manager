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

import com.openclassrooms.realestatemanager.model.PointOfInterest;

import java.util.List;

@Dao
public interface PointOfInterestDao {

    // RAW QUERY FOR ADVANCED SEARCHING
    @RawQuery(observedEntities = PointOfInterest.class)
    LiveData<List<PointOfInterest>> searchPointOfInterestWithFilter(SupportSQLiteQuery query);

    // GET ALL POINT OF A PROPERTY
    @Query("SELECT * FROM PointOfInterest WHERE id = :idProperty")
    LiveData<List<PointOfInterest>> getPointsOfInterest(long idProperty);

    // GET ALL POINTS
    @Query("SELECT DISTINCT * FROM PointOfInterest")
    LiveData<List<PointOfInterest>> getAllPointsOfInterest();

    // INSERT
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    long insertPointOfInterest (PointOfInterest pointOfInterest);

    // UPDATE
    @Update
    int updatePointOfInterest(PointOfInterest pointOfInterest);

    // DELETE ALL POINTS OF A PROPERTY
    @Query("DELETE FROM PointOfInterest WHERE idProperty = :idProperty")
    int deletePointsOfInterestOfProperty(long idProperty);
}
