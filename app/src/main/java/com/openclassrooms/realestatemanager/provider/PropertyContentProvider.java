/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.User;

public class PropertyContentProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.openclassrooms.realestatemanager.provider";

    public static final int CODE_PROPERTY = 1;
    public static final int CODE_PROPERTY_ID = 101;
    public static final int CODE_ADDRESS = 2;
    public static final int CODE_ADDRESS_ID = 201;
    public static final int CODE_USER = 3;
    public static final int CODE_USER_ID = 301;

    public static final String TABLE_NAME_PROPERTY = Property.class.getSimpleName();
    public static final String TABLE_NAME_ADDRESS = Address.class.getSimpleName();
    public static final String TABLE_NAME_USER = User.class.getSimpleName();
    public static final Uri URI_PROPERTY = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_NAME_PROPERTY);
    public static final Uri URI_ADDRESS = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_NAME_ADDRESS);
    public static final Uri URI_USER = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_NAME_USER);
    public static final Uri URI_PROPERTY_ID = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_NAME_PROPERTY + "/" + CODE_PROPERTY_ID);
    public static final Uri URI_ADDRESS_ID = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_NAME_ADDRESS + "/" + CODE_ADDRESS_ID);
    public static final Uri URI_USER_ID = Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_NAME_USER + "/" + CODE_USER_ID);


    // For search specific tables
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, Property.class.getSimpleName(), CODE_PROPERTY);
        uriMatcher.addURI(PROVIDER_NAME, Address.class.getSimpleName(), CODE_ADDRESS);
        uriMatcher.addURI(PROVIDER_NAME, User.class.getSimpleName(), CODE_USER);
        uriMatcher.addURI(PROVIDER_NAME, Property.class.getSimpleName() + "/*", CODE_PROPERTY_ID);
        uriMatcher.addURI(PROVIDER_NAME, Address.class.getSimpleName() + "/*", CODE_ADDRESS_ID);
        uriMatcher.addURI(PROVIDER_NAME, User.class.getSimpleName() + "/*", CODE_USER_ID);
    }

    @Override
    public boolean onCreate() { return true; }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] strings1, @Nullable String s1) {
        if(getContext() != null){

            int index = uriMatcher.match(uri);

            switch (index){
                case CODE_USER :
                    final Cursor cursorOfUser = RealEstateManagerDatabase.getInstance(getContext()).userDao().getCursorUser();
                    cursorOfUser.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorOfUser;
                case CODE_PROPERTY :
                    // get all property
                    long userId = 1;
                    final Cursor cursorOfProperty = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().getCursorProperty(userId);
                    cursorOfProperty.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorOfProperty;
                case CODE_ADDRESS :
                    final Cursor cursorOfAddress = RealEstateManagerDatabase.getInstance(getContext()).addressDao().getCursorOfAllAddress();
                    cursorOfAddress.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorOfAddress;
                case CODE_PROPERTY_ID :
                    // get all property
                    long propertyId = Long.parseLong(selection);
                    final Cursor cursorOfAPropertyById = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().getCursorPropertyById(propertyId);
                    cursorOfAPropertyById.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorOfAPropertyById;
                case CODE_ADDRESS_ID :
                    long propertyIdOfAddress = Long.parseLong(selection);
                    final Cursor cursorOfAddressOfAProperty = RealEstateManagerDatabase.getInstance(getContext()).addressDao().getCursorOfAddress(propertyIdOfAddress);
                    cursorOfAddressOfAProperty.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursorOfAddressOfAProperty;
                default:
                    break;
            }
        }
        throw new IllegalArgumentException("Not data found for the uri : " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case CODE_PROPERTY :
                return "vnd.android.cursor.dir/" + PROVIDER_NAME + "." + TABLE_NAME_PROPERTY;
            case CODE_ADDRESS :
                return "vnd.android.cursor.dir/" + PROVIDER_NAME + "." + TABLE_NAME_ADDRESS;
            case CODE_PROPERTY_ID :
                return "vnd.android.cursor.item/" + PROVIDER_NAME + "." + TABLE_NAME_PROPERTY;
            case CODE_ADDRESS_ID :
                return "vnd.android.cursor.item/" + PROVIDER_NAME + "." + TABLE_NAME_ADDRESS;
            default:
                throw new IllegalArgumentException("No good type " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if(getContext() != null){
            switch (uriMatcher.match(uri)){
                case CODE_USER :
                    final long idUser = RealEstateManagerDatabase.getInstance(getContext()).userDao().createUser(User.fromContentValues(contentValues));
                    if(idUser != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, idUser);
                    }
                case CODE_PROPERTY :
                    final long idProperty = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().insertProperty(Property.fromContentValues(contentValues));
                    if(idProperty != 0){
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, idProperty);
                    }
                case CODE_ADDRESS :
                    final long idAddress = RealEstateManagerDatabase.getInstance(getContext()).addressDao().insertAddress(Address.fromContentValues(contentValues));
                    if(idAddress != 0){
                        getContext().getContentResolver().notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, idAddress);
                    }
                default:
                   break;
            }
        }
        throw new IllegalArgumentException("Failed to insert data : " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null){
            switch (uriMatcher.match(uri)){
                case CODE_PROPERTY :
                    final int countProperty = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().deleteProperty(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countProperty;
                case CODE_ADDRESS :
                    final int countAddress = RealEstateManagerDatabase.getInstance(getContext()).addressDao().deleteAddress(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countAddress;
                default:
                    break;
            }
        }
        throw new IllegalArgumentException("Failed to delete data " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if(getContext() != null){
            switch (uriMatcher.match(uri)){
                case CODE_PROPERTY :
                    final int countProperty = RealEstateManagerDatabase.getInstance(getContext()).propertyDao().updateProperty(Property.fromContentValues(contentValues));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countProperty;
                case CODE_ADDRESS :
                    final int countAddress = RealEstateManagerDatabase.getInstance(getContext()).addressDao().updateAddress(Address.fromContentValues(contentValues));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return countAddress;
                default:
                    break;
            }
        }
        throw new IllegalArgumentException("Failed to update date " + uri);
    }
}
