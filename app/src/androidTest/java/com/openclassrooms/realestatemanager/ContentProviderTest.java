/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.provider.AddressContentProvider;
import com.openclassrooms.realestatemanager.provider.PropertyContentProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    // FOR DATA
    private ContentResolver mContentResolver;
    RealEstateManagerDatabase db;

    // DATA SET FOR TEST
    private long USER_ID = 1;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
         db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), RealEstateManagerDatabase.class)
                .build();
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();
        USER_ID = db.userDao().createUser(new User("TestFirstName", "TestSecondName", "email", "000000000", "icon", "xxxx"));
    }

    @After
    public void closeDb() throws IOException {
        /** WARNING THIS SUPPRESS ALL TABLES */
        db.clearAllTables();
        db.close();
    }

    @Test
    public void getPropertyWhenNoPropertyInserted() {
        mContentResolver.insert(PropertyContentProvider.URI_USER, generateUser());
        final Cursor cursorUser = mContentResolver.query(PropertyContentProvider.URI_USER, null, null, null,null);
        USER_ID = cursorUser.getLong(cursorUser.getColumnIndex("id"));

        final Cursor cursorProperty = mContentResolver.query(PropertyContentProvider.URI_PROPERTY, null, null, null, null);

        final Cursor cursorAddress = mContentResolver.query(PropertyContentProvider.URI_ADDRESS, null, null, null, null);

        assertThat(cursorUser, notNullValue());
        assertThat(cursorUser.getCount(), is(1));
        assertThat(cursorUser.getString(cursorUser.getColumnIndexOrThrow("email")), is("email"));
        assertThat(cursorProperty, notNullValue());
        assertThat(cursorProperty.getCount(), is(0));
        assertThat(cursorAddress, notNullValue());
        assertThat(cursorAddress.getCount(), is(0));
        cursorProperty.close();
        cursorAddress.close();
        cursorUser.close();
    }

    @Test
    public void insertAndGetPropertyWithAddress() {

        final Uri uriProperty = mContentResolver.insert(PropertyContentProvider.URI_PROPERTY, generateProperty());
        final Cursor cursorPropertyInserted = mContentResolver.query(PropertyContentProvider.URI_PROPERTY, null, null, null, null);

        final long idProperty = cursorPropertyInserted.getColumnIndex("id");

        final Uri userUriAddress = mContentResolver.insert(PropertyContentProvider.URI_ADDRESS, generateAddress(idProperty));

        // TEST
        final Cursor cursorAddress = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ADDRESS, idProperty), null, null, null, null);
        //FOR PROPERTY
        assertThat(cursorPropertyInserted, notNullValue());
        assertThat(cursorPropertyInserted.getCount(), is(1));
        assertThat(cursorPropertyInserted.moveToFirst(), is(true));
        assertThat(cursorPropertyInserted.getString(cursorPropertyInserted.getColumnIndexOrThrow("type")), is("HOUSE TEST"));
        //FOR ADDRESS
        assertThat(cursorAddress, notNullValue());
        assertThat(cursorAddress.getCount(), is(1));
        assertThat(cursorAddress.moveToFirst(), is(true));
        assertThat(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow("country")), is("Belgique"));
    }

    // ---

    private ContentValues generateProperty(){
        final ContentValues values = new ContentValues();

        values.put("type", "HOUSE TEST");
        values.put("pris", 10000f);
        values.put("nbRoom", 10);
        values.put("area", 100);
        values.put("description", "hello");
        values.put("state", "NOT_SELL");
        values.put("dateEnter", (new Date()).toString());
        values.put("dateSold", (new Date()).toString());
        values.put("idAgent", USER_ID);

        return values;
    }

    private ContentValues generateUser(){
        final ContentValues values = new ContentValues();

        values.put("firstName", "testName");
        values.put("secondName", "testSecondName");
        values.put("email", "emailTest");
        values.put("phoneNumber", "000004");
        values.put("icon", "pathIcon");
        values.put("password","password");

        return values;
    }

    private ContentValues generateAddress(long idProperty){
        final ContentValues values = new ContentValues();

        values.put("country", "Belgique");
        values.put("city", "Namur");
        values.put("postalCode", "5000");
        values.put("street", "XXX");
        values.put("numberStreet", 10);
        values.put("idProperty", idProperty);
        values.put("latLocation", 0);
        values.put("longLocation", 0);

        return values;
    }


}