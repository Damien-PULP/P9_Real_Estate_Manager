/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.marker.InfoMarkerView;
import com.openclassrooms.realestatemanager.view.viewmodel.Injection;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.view.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    //DATA
    private List<PropertyObj> propertyObjs = new ArrayList<>();

    private double userLatitude;
    private double userLongitude;

    private MainViewModel mainViewModel;

    private FusedLocationProviderClient fusedLocationClient;
    private LifecycleOwner activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        activity = getActivity();
        configureViewModel();
        getCurrentLocation();

        return root;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), viewModelFactory).get(MainViewModel.class);
        mainViewModel.init();
    }

    private void getCurrentLocation() {
        //GET LOCATION
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        if (mainViewModel.checkPermissionLocation((MainActivity) getActivity())) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                if (location != null) {
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();
                    getUser();
                }
            }).addOnFailureListener(getActivity(), Throwable::printStackTrace);
        }
    }

    private void getUser() {
        assert mainViewModel.getUser() != null;
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
        if(((MainActivity)activity).getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_detail) != null){
            //LAND VIEW
            //MAP Fragment
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_detail_location_maps_land);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }else{
            //MAP Fragment
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_detail_location_maps);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
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
    }
}
