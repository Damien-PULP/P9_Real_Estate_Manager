/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.openclassrooms.realestatemanager.model.User;

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

    public LiveData<List<PropertyObj>> getProperty (long idUser){
        if (currentUser!= null) {
            return propertyDataSource.getPropertyWithAttributes(idUser);
        }else{
            return null;
        }
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
}
