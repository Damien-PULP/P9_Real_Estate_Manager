/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.viewmodel;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.database.repository.UserDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static UserDataRepository provideUserDataSource(Context context ){
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new UserDataRepository(database.userDao());
    }
    public static PropertyDataRepository providePropertyDataSource(Context context ){
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new PropertyDataRepository(database.propertyDao(), database.addressDao(), database.photoDao(), database.pointOfInterestDao());
    }
    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }
    public static ViewModelFactory provideViewModelFactory(Context context){
        UserDataRepository dataSourceUser = provideUserDataSource(context);
        PropertyDataRepository dataSourceProperty = providePropertyDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceUser, dataSourceProperty, executor);
    }

}
