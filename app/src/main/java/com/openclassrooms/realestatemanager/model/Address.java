/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.model;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "id", childColumns = "idProperty"))
public class Address {

    @PrimaryKey (autoGenerate = true) private long id;
    private String country;
    private String city;
    private String postalCode;
    private String street;
    private Integer numberStreet;
    private long idProperty;
    private double latLocation;
    private double longLocation;

    //Need empty constructor
    public Address() {
    }
    public Address(String country, String city, String postalCode, String street, Integer numberStreet, double latLocation, double longLocation, long idProperty) {
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.numberStreet = numberStreet;
        this.latLocation = latLocation;
        this.longLocation = longLocation;
        this.idProperty = idProperty;
    }

    //GETTER
    public long getId() {
        return id;
    }
    public String getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getStreet() {
        return street;
    }
    public Integer getNumberStreet() {
        return numberStreet;
    }
    public long getIdProperty() {
        return idProperty;
    }
    public double getLatLocation() {
        return latLocation;
    }
    public double getLongLocation() {
        return longLocation;
    }

    //SETTER
    public void setId(long id) {
        this.id = id;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public void setNumberStreet(Integer numberStreet) {
        this.numberStreet = numberStreet;
    }
    public void setIdProperty(long idProperty) {
        this.idProperty = idProperty;
    }
    public void setLatLocation(double latLocation) {
        this.latLocation = latLocation;
    }
    public void setLongLocation(double longLocation) {
        this.longLocation = longLocation;
    }

    @NonNull
    @Override
    public String toString() {
        return country + ", " + city + ", " + postalCode + ", " + street + " " + numberStreet;
    }

    public static Address fromContentValues (ContentValues values){
        Address address = new Address();
        if(values.containsKey("country")) address.setCountry(values.getAsString("country"));
        if(values.containsKey("city")) address.setCity(values.getAsString("city"));
        if(values.containsKey("postalCode")) address.setPostalCode(values.getAsString("postalCode"));
        if(values.containsKey("street")) address.setStreet(values.getAsString("street"));
        if(values.containsKey("numberStreet")) address.setNumberStreet(values.getAsInteger("numberStreet"));
        if(values.containsKey("idProperty")) address.setIdProperty(values.getAsLong("idProperty"));
        if(values.containsKey("latLocation")) address.setLatLocation(values.getAsLong("latLocation"));
        if(values.containsKey("longLocation")) address.setLongLocation(values.getAsLong("longLocation"));
        return address;
    }
}
