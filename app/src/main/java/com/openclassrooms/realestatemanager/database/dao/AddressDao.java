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

import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Property;

import java.util.List;

@Dao
public interface AddressDao {

    @Query("SELECT * FROM Address WHERE idProperty = :idProperty")
    LiveData<Address> getAddress(long idProperty);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddress (Address address);

    @Update
    int updateAddress(Address address);

    @Query("DELETE FROM Address WHERE idProperty = :idProperty")
    int deleteAddress(long idProperty);
}
