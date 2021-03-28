/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM Photo WHERE idProperty = :idProperty")
    LiveData<List<Photo>> getPhotos(long idProperty);

    @Insert
    long insertPhoto (Photo photo);

    @Update
    int updatePhoto(Photo photo);

    @Query("DELETE FROM Photo WHERE id = :id")
    int deletePhoto(long id);

    @Query("DELETE FROM Photo WHERE idProperty = :idProperty")
    int deletePhotosOfAProperty(long idProperty);
}
