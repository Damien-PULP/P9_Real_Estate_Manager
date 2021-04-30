/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Property;

public class AddressContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    public static final String TABLE_NAME_ADDRESS = Property.class.getSimpleName();
    public static final Uri URI_ADDRESS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME_ADDRESS);

    @Override
    public boolean onCreate() { return true; }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        if(getContext() != null){
            long userId = ContentUris.parseId(uri);
            final Cursor cursor = RealEstateManagerDatabase.getInstance(getContext()).addressDao().getCursorOfAddress(userId);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        throw new IllegalArgumentException("Not data found for the uri : " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.address/" + AUTHORITY + "." + TABLE_NAME_ADDRESS;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if(getContext() != null){
            final long id = RealEstateManagerDatabase.getInstance(getContext()).addressDao().insertAddress(Address.fromContentValues(contentValues));
            if(id != 0){
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }
        throw new IllegalArgumentException("Failed to insert data : " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null){
            final int count = RealEstateManagerDatabase.getInstance(getContext()).addressDao().deleteAddress(ContentUris.parseId(uri));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to delete data " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if(getContext() != null){
            final int count = RealEstateManagerDatabase.getInstance(getContext()).addressDao().updateAddress(Address.fromContentValues(contentValues));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update date " + uri);
    }
}
