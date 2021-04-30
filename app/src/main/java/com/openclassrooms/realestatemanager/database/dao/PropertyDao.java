/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyObj;

import java.util.List;

@Dao
public interface PropertyDao {

    /** To complex search in data base
     * use SearchPropertyModel.class */
    @RawQuery(observedEntities = PropertyObj.class)
    LiveData<List<PropertyObj>> searchPropertyWithFilter(SupportSQLiteQuery query);

    @Query("SELECT * FROM Property WHERE idAgent = :userId")
    LiveData<List<PropertyObj>> getPropertyWithAllAttribute (long userId);

    //use for the content provider
    @Query("SELECT * FROM Property WHERE idAgent = :userId")
    Cursor getCursorProperty (long userId);

    @Query("SELECT * FROM Property WHERE id = :id")
    Cursor getCursorPropertyById (long id);

    @Query("SELECT * FROM Property WHERE idAgent = :userId")
    LiveData<List<Property>> getProperty(long userId);

    @Query("SELECT * FROM Property WHERE id = :idProperty")
    LiveData<PropertyObj> getAPropertyObj(long idProperty);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProperty (Property property);

    @Update
    int updateProperty(Property property);

    @Query("UPDATE Property SET state = :state WHERE id = :id")
    int updateStateProperty(long id, String state);

    @Query("DELETE FROM Property WHERE id = :idProperty")
    int deleteProperty(long idProperty);
}
