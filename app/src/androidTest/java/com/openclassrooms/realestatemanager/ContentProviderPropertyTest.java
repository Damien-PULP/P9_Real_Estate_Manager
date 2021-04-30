/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.provider.PropertyContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ContentProviderPropertyTest {

    private ContentResolver contentResolver;
    private long USER_ID;

    @Before
    public void setup (){
        contentResolver = ApplicationProvider.getApplicationContext().getContentResolver();
    }

    @Test
    public void testInsertionProperty (){
        //FOR USER
        contentResolver.insert(PropertyContentProvider.URI_USER, generateUser());
        final Cursor cursorUser = contentResolver.query(PropertyContentProvider.URI_USER, null, null, null,null);
        final int size = cursorUser.getColumnCount();
        final int index_id = cursorUser.getColumnIndex("id");
        final int index_firstName = cursorUser.getColumnIndex("firstName");

        if(!cursorUser.moveToFirst()){
            Log.e("ContentProviderTest", "No user in database");
        }else{
            USER_ID = cursorUser.getLong(index_id);
            final String firstName_user = cursorUser.getString(index_firstName);

            assertThat(firstName_user, is("testName"));
        }

        // FOR PROPERTY
        final Uri uriProperty = contentResolver.insert(PropertyContentProvider.URI_PROPERTY, generateProperty());
        final long idProperty = ContentUris.parseId(uriProperty);
        final Cursor cursorPropertyInserted = contentResolver.query(PropertyContentProvider.URI_PROPERTY_ID, null, String.valueOf(idProperty), null, null);

        final int size_property = cursorPropertyInserted.getColumnCount();
        final int index_property_type = cursorPropertyInserted.getColumnIndex("type");
        final int index_property_description = cursorPropertyInserted.getColumnIndex("description");

        if(!cursorPropertyInserted.moveToFirst()){
            Log.e("ContentProviderTest", "No property in database, size : " + size_property);
        }else {
            final String type_property = cursorPropertyInserted.getString(index_property_type);
            final String description_property = cursorPropertyInserted.getString(index_property_description);

            assertThat(type_property, is("HOUSE TEST"));
            assertThat(description_property, is("hello"));
            Log.d("ContentProviderTest", "The property is a " + type_property + " and the description is : " + description_property);
        }

        //FOR ADDRESS
        contentResolver.insert(PropertyContentProvider.URI_ADDRESS, generateAddress(idProperty));
        final Cursor cursorOfAddressOfProperty = contentResolver.query(PropertyContentProvider.URI_ADDRESS_ID, null, String.valueOf(idProperty), null, null);

        final int index_country = cursorOfAddressOfProperty.getColumnIndex("country");
        final int index_street = cursorOfAddressOfProperty.getColumnIndex("street");

        if(!cursorOfAddressOfProperty.moveToFirst()){
            Log.e("ContentProviderTest", "No address in database");
        }else{
            final String country = cursorOfAddressOfProperty.getString(index_country);
            final String street = cursorOfAddressOfProperty.getString(index_street);

            assertThat(country, is("Belgique"));
            assertThat(street, is("XXX"));
        }

    }

    // DUMMY DATA
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

        values.put("id", 1);
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
