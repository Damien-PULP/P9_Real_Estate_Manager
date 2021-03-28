/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.database.repository.UserDataRepository;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.User;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {

    private UserDataRepository userDataSource;
    private PropertyDataRepository propertyDataSource;
    private Executor executor;

    @Nullable
    private LiveData<User> currentUser;

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

    public void insertProperty (String type, float pris, int nbRoom, String description, long idUser){
        executor.execute(()-> {
            if(currentUser != null) {
                Property property = new Property(type, pris, nbRoom, description, "NOT_SELL", new Date(), null, idUser);

                propertyDataSource.createProperty(property);
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
}
