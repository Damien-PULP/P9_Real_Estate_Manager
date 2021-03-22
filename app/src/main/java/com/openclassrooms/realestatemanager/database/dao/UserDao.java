/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createUser(User user);

    @Query("SELECT * FROM User WHERE firstName = :firstName AND secondName = :secondName AND password = :password")
    LiveData<User> getUser(String firstName, String secondName, String password);

    @Update
    int updateUser(User user);

    @Query("DELETE FROM User WHERE firstName = :firstName AND secondName = :secondName AND password = :password")
    int deleteUser(String firstName, String secondName, String password);
}
