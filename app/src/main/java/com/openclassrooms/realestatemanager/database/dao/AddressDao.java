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

import com.openclassrooms.realestatemanager.model.Address;

@Dao
public interface AddressDao {


    // FOR CONTENT PROVIDER
    @Query("SELECT * FROM Address WHERE idProperty = :idProperty")
    Cursor getCursorOfAddress(long idProperty);

    @Query("SELECT * FROM Address")
    Cursor getCursorOfAllAddress();

    //GET
    @Query("SELECT * FROM Address WHERE idProperty = :idProperty")
    LiveData<Address> getAddress(long idProperty);

    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddress (Address address);

    //UPDATE
    @Update
    int updateAddress(Address address);

    //DELETE
    @Query("DELETE FROM Address WHERE idProperty = :idProperty")
    int deleteAddress(long idProperty);
}
