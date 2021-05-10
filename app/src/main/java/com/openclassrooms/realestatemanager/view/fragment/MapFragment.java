/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.utils.GPSTrackerManager;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.marker.InfoMarkerView;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, LocationListener {

    //DATA
    private List<PropertyObj> propertyObjs = new ArrayList<>();

    private double userLatitude;
    private double userLongitude;

    private MainViewModel mainViewModel;

    //private FusedLocationProviderClient fusedLocationClient;
    private LifecycleOwner activity;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        activity = getActivity();
        configureViewModel();
        getLocation();
        //getCurrentLocation();

        return root;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory((MainActivity) activity);
        this.mainViewModel = new ViewModelProvider(Objects.requireNonNull((MainActivity) activity), viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
    }

    private void getLocation() {

        //boolean stateApi = checkPlayServices();
        //Log.d("MainActivity", "the status of Google Play Service : " + stateApi);
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((MainActivity) activity);
        if (ActivityCompat.checkSelfPermission((MainActivity) activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((MainActivity) activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if(location != null){
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
                getUser();
            }else{
                Log.e("MainActivity", "the location is null");
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }
    /**private void getCurrentLocation() {

        /*GPSTrackerManager tracker = new GPSTrackerManager((MainActivity) activity);

        if(tracker.isPossibleGetLocation()){
            Location location = tracker.getCurrentLocation();
            if(location != null) {
                Log.d("MapFragment", "latitude" + location.getLatitude() + "longitude" + location.getLongitude());
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
                getUser();
            }
        }else{
            Log.e("MapFragment", "error get Location");
        }



        LocationManager locationManager = (LocationManager) ((MainActivity) activity).getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission((MainActivity) activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((MainActivity) activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int locationRequestCode = 100;
            ActivityCompat.requestPermissions((MainActivity) activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        //GET LOCATION
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull((MainActivity)activity));
        if (mainViewModel.checkPermissionLocation((MainActivity)activity)) {
            if (ActivityCompat.checkSelfPermission((MainActivity)activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((MainActivity)activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener((MainActivity)activity, location -> {
                if (location != null) {
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();

                }else{
                    getCurrentLocation();
                }
            }).addOnFailureListener(e -> {
                Log.e("MapFragment", "error map : ");
                Log.e("MapFragment", e.getLocalizedMessage());
            });
        }
    }*/

    private void getUser() {
        mainViewModel.getUser().observe(Objects.requireNonNull(activity), this::getProperty);
    }

    private void getProperty(User user) {
        if (user != null)
            mainViewModel.getProperty(user.getId()).observe(Objects.requireNonNull(activity), this::updateProperty);
    }
    private void updateProperty(List<PropertyObj> propertyObjs) {
        this.propertyObjs = propertyObjs;
        configureMap();
    }

    private void configureMap() {
        //MAP Fragment
        if(activity != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map_support_map_fragment);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        for (PropertyObj propertyObj : propertyObjs) {
            LatLng locationProperty = new LatLng(propertyObj.getAddress().getLatLocation(), propertyObj.getAddress().getLongLocation());
            MarkerOptions markerOptions = new MarkerOptions().position(locationProperty).title(String.valueOf(propertyObj.getProperty().getId()));
            //marker.
            Marker marker = googleMap.addMarker(markerOptions);
            InfoMarkerView adapterMarker = new InfoMarkerView(getActivity(), propertyObjs);
            googleMap.setInfoWindowAdapter(adapterMarker);
            googleMap.setOnInfoWindowClickListener(this);
        }
        LatLng locationUser = new LatLng(userLatitude, userLongitude);
        googleMap.addMarker(new MarkerOptions().position(locationUser).title("User").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationUser, 10));

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        mainViewModel.setCurrentIndexPropertyDetail(Long.parseLong(marker.getTitle()));
        if(getActivity() != null) ((MainActivity) getActivity()).switchFragment(1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions((MainActivity) activity, perms)) {
            Toast.makeText((MainActivity) activity, "Permission already granted", Toast.LENGTH_SHORT).show();
            getLocation();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        userLatitude = location.getLatitude();
                        userLongitude = location.getLongitude();
                        getUser();
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }*/


    @Override
    public void onLocationChanged(@NonNull Location location) {
        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();
        getUser();
    }
    @Override
    public void onStatusChanged(String var1, int var2, Bundle var3){
        Log.d("MapFragment", "here status");
    }
    @Override
    public void onProviderEnabled(String var1){
        Log.d("MapFragment", "here provider enabled ");
    }
    @Override
    public void onProviderDisabled(String var1){
        Log.d("MapFragment", "here provider disabled");
    }
}
