/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.view.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.database.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.database.repository.UserDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final UserDataRepository userDataRepository;
    private final PropertyDataRepository propertyDataRepository;
    private final Executor executor;

    public ViewModelFactory(UserDataRepository userDataRepository, PropertyDataRepository propertyDataRepository, Executor executor) {
        this.userDataRepository = userDataRepository;
        this.propertyDataRepository = propertyDataRepository;
        this.executor = executor;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(userDataRepository, propertyDataRepository, executor);
        }
        throw new IllegalArgumentException("Not just ViewModel class");
    }
}
