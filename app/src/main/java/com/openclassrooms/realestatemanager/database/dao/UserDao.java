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
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.User;

@Dao
public interface UserDao {

    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createUser(User user);

    //GET
    @Query("SELECT * FROM User WHERE firstName = :firstName AND secondName = :secondName AND password = :password")
    LiveData<User> getUser(String firstName, String secondName, String password);

    //FOR CONTENT PROVIDER
    @Query("SELECT * FROM User")
    Cursor getCursorUser ();

    //GET USER WITHOUT PASSWORD
    @Query("SELECT * FROM User")
    LiveData<User> getUserSimplify();

    //UPDATE
    @Update
    int updateUser(User user);

    @Query("DELETE FROM User WHERE firstName = :firstName AND secondName = :secondName AND password = :password")
    int deleteUser(String firstName, String secondName, String password);
}
