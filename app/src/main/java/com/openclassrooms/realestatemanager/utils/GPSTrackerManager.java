/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;

public class GPSTrackerManager implements LocationListener {

    private boolean isPossibleGetLocation = false;

    private boolean statusGPS;
    private boolean statusNetwork;

    private Location currentLocation;

    private LocationManager locationManager;

    private final Context context;

    public GPSTrackerManager(Context context) {
        this.context = context;
        getCurrentLocation();
    }

    public Location getCurrentLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            statusGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            statusNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!statusNetwork && !statusGPS) {
                Log.e("GPSTrackerManager", "The GPS or internet is no activate");
                return null;
            } else {
                isPossibleGetLocation = true;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((MainActivity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
                if(statusNetwork) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, this);
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if(statusGPS){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return currentLocation;
    }

    /*public Location getCurrentFusedLocation (){
        FusedLocationProviderClient  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation().addOn
    }*/

    public boolean isPossibleGetLocation(){
        return isPossibleGetLocation;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
