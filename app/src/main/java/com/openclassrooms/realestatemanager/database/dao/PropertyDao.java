/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE idAgent = :userId")
    LiveData<List<Property>> getProperty(long userId);

    @Insert
    long insertProperty (Property property);

    @Update
    int updateProperty(Property property);

    @Query("DELETE FROM Property WHERE id = :idProperty")
    int deleteProperty(long idProperty);
}
