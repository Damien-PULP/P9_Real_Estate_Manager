/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.AddressDao;
import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.database.dao.PointOfInterestDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.utils.ConverterRoom;

@Database(entities = {User.class, Property.class, Address.class, Photo.class, PointOfInterest.class}, version = 5, exportSchema = false)
@TypeConverters({ConverterRoom.class})
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    // SINGLETON
    private static volatile RealEstateManagerDatabase INSTANCE;
    // DAO
    public abstract UserDao userDao();
    public abstract PropertyDao propertyDao ();
    public abstract AddressDao addressDao ();
    public abstract PhotoDao photoDao ();
    public abstract PointOfInterestDao pointOfInterestDao();
    // INSTANCE
    public static RealEstateManagerDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (RealEstateManagerDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateManagerDatabase.class, "RealEstateManagerDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                /* TODO Suppress When account is here */
                /*ContentValues contentValues = new ContentValues();
                contentValues.put("id", "1");
                contentValues.put("firstName", "firstName");
                contentValues.put("email", "email");
                contentValues.put("phoneNumber", "000000000");
                contentValues.put("password", "password");

                db.insert("User", OnConflictStrategy.IGNORE, contentValues);*/
            }
        };
    }
}
