/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    // GET ALL PHOTO OF A PROPERTY
    @Query("SELECT * FROM Photo WHERE idProperty = :idProperty")
    LiveData<List<Photo>> getPhotos(long idProperty);

    // GET A PHOTO BY ID
    @Query("SELECT * FROM Photo WHERE id = :id")
    LiveData<Photo> getAPhoto(long id);

    // INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPhoto (Photo photo);

    // UPDATE
    @Update
    int updatePhoto(Photo photo);

    // DELETE ALL PHOTO OF A PROPERTY
    @Query("DELETE FROM Photo WHERE idProperty = :idProperty")
    int deletePhotosOfAProperty(long idProperty);
}
