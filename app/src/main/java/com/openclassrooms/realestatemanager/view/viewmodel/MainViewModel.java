/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.database.repository.UserDataRepository;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.SearchPropertyModel;
import com.openclassrooms.realestatemanager.model.User;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {

    private UserDataRepository userDataSource;
    private PropertyDataRepository propertyDataSource;
    private Executor executor;

    @Nullable
    private LiveData<User> currentUser;

    //Temporary data
    private List<Photo> photosOfAProperty = new ArrayList<>();

    private MutableLiveData<Long> currentIdPropertyDetail = new MutableLiveData<>();
    private int locationRequestCode = 100;


    public MainViewModel(UserDataRepository userDataSource, PropertyDataRepository propertyDataSource, Executor executor) {
        this.userDataSource = userDataSource;
        this.propertyDataSource = propertyDataSource;
        this.executor = executor;
    }

    public void init (){
        if(currentUser == null){
            currentUser = userDataSource.getUserSimplify();
        }
    }

    public LiveData<User> getUser(){
        return currentUser;
    }
    public void insertUser (@NonNull String firstName, @NonNull String secondName, @NonNull String email, @NonNull String phoneNumber, @NonNull String icon, @NonNull String password){
        executor.execute(()-> {
            User user = new User(firstName, secondName, email, phoneNumber, icon, password);
            userDataSource.createUser(user);
            init();
        });
    }

    public void insertProperty (Property property, Address address, List<Photo> photos, List<PointOfInterest> pointOfInterests){
        executor.execute(()-> {
            if(currentUser != null) {

                long id = propertyDataSource.createProperty(property);
                address.setIdProperty(id);
                propertyDataSource.createAddress(address);
                for(Photo photo : photos){
                    photo.setIdProperty(id);
                    propertyDataSource.createPhoto(photo);
                }

                for(PointOfInterest pointOfInterest : pointOfInterests){
                    pointOfInterest.setIdProperty(id);
                    propertyDataSource.createPointOfInterest(pointOfInterest);
                }

            }
        });
    }
    public void updateStateOfProperty(long id, String state){
        propertyDataSource.updateStateProperty(id, state);
    }
    public LiveData<List<PropertyObj>> getProperty (long idUser){
        if (currentUser!= null) {
            return propertyDataSource.getPropertyWithAttributes(idUser);
        }else{
            return null;
        }
    }

    public LiveData<Photo> getAPhoto (long id){
        return propertyDataSource.getAPhoto(id);
    }

    public LiveData<PropertyObj> getAPropertyObj (long idProperty){
        return propertyDataSource.getAPropertyObj(idProperty);
    }

    public void setCurrentIndexPropertyDetail (long index){
        currentIdPropertyDetail.setValue(index);
    }
    public Long getCurrentIndexPropertyDetail (){
        return currentIdPropertyDetail.getValue();
    }

    // To Search a Property in Database
    public LiveData<List<PropertyObj>> searchAPropertyWithFilter (SearchPropertyModel searchPropertyModel){
        return this.propertyDataSource.searchPropertyWithFilter(searchPropertyModel);
    }
    public LiveData<List<PointOfInterest>> getAllPointOfInterest (){
        return this.propertyDataSource.getAllPointOfInterest();
    }
    public LiveData<List<PointOfInterest>> searchPointOfInterestByName (SearchPropertyModel searchPropertyModel){ return this.propertyDataSource.searchPointOfInterestWithFilter(searchPropertyModel);}
    // For AddFragment - Photos manager
    public void addPhotoOfTheProperty (Photo photo){
        photosOfAProperty.add(photo);
    }
    public void clearThePhotoListOfTheProperty (){
        photosOfAProperty.clear();
    }
    public void removeAPhotoOfTheProperty (Photo photo){
        photosOfAProperty.remove(photo);
    }
    public List<Photo> getPhotosOfTheProperty (){
        return photosOfAProperty;
    }
    // CHECK PERMISSION LOCATION
    public boolean checkPermissionLocation (MainActivity activity){
        // Check location permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request the permission
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
            return false;
        } else {
            // already granted
            return true;
        }
    }

    public void updateProperty(Property propertyUpdated, Address addressOfProperty, List<Photo> photosOfTheProperty, List<PointOfInterest> pointsOfInterest) {
        executor.execute(()-> {
            if(currentUser != null) {

                propertyDataSource.updateProperty(propertyUpdated);
                addressOfProperty.setIdProperty(propertyUpdated.getId());
                propertyDataSource.createAddress(addressOfProperty);
                propertyDataSource.deletePhotosOfProperty(propertyUpdated.getId());
                for(Photo photo : photosOfTheProperty){
                    photo.setIdProperty(propertyUpdated.getId());
                    propertyDataSource.createPhoto(photo);
                }

                propertyDataSource.deletePointOfInterestOfProperty(propertyUpdated.getId());
                for(PointOfInterest pointOfInterest : pointsOfInterest){
                    pointOfInterest.setIdProperty(propertyUpdated.getId());
                    propertyDataSource.createPointOfInterest(pointOfInterest);
                }

            }
        });
    }
}
