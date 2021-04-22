/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocation {

    public interface GeoLocationService {
        void onSuccessGetLocation (double latitude, double longitude, String address);
        void onFailureGetLocation ();
    }
    public static void getLocationOfAddress (String locationAddress, Context context, ProgressBar progressBar, View viewParent, GeoLocationService callback){
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                double latitude = 0;
                double longitude = 0;

                try{
                    AppCompatActivity activity = (AppCompatActivity) context;
                    activity.runOnUiThread(() -> {

                        progressBar.setVisibility(View.VISIBLE);
                        viewParent.setVisibility(View.INVISIBLE);

                    });

                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if(addressList != null && addressList.size() > 0){
                        Address address = (Address) addressList.get(0);
                        latitude = address.getLatitude();
                        longitude = address.getLongitude();

                    }

                } catch (IOException e){
                    e.printStackTrace();
                    callback.onFailureGetLocation();
                } finally {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    activity.runOnUiThread(() -> {

                        progressBar.setVisibility(View.GONE);
                        viewParent.setVisibility(View.VISIBLE);

                    });

                    if(latitude != 0 & longitude != 0) {
                        callback.onSuccessGetLocation(latitude, longitude, locationAddress);
                    }else{
                        callback.onFailureGetLocation();
                    }
                }
            }
        };
        thread.start();
    }
}
