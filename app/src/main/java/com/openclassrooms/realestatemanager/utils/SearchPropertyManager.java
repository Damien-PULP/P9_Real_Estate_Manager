/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.utils;

import android.view.View;
import android.widget.ProgressBar;

import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.SearchPropertyModel;
import com.openclassrooms.realestatemanager.view.activity.MainActivity;
import com.openclassrooms.realestatemanager.view.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Complex search properties by filter
 */
public class SearchPropertyManager {

    private final MainActivity activity;
    private final MainViewModel mainViewModel;
    private final SearchPropertyManagerCallback callback;
    private final SearchPropertyModel searchPropertyModel;

    private final ProgressBar progressBar;

    private List<PropertyObj> listPropertyByFirstStep = new ArrayList<>();
    private List<PointOfInterest> listPointOfInterestSecondStep = new ArrayList<>();

    //CALLBACK OF THE SEARCH MANAGER
    public interface SearchPropertyManagerCallback {
        void onResearchIsCompleted(List<PropertyObj> propertyByFilter);
    }

    public SearchPropertyManager(MainActivity activity, MainViewModel mainViewModel, SearchPropertyManagerCallback callback, SearchPropertyModel searchPropertyModel, ProgressBar progressBar) {
        this.activity = activity;
        this.mainViewModel = mainViewModel;
        this.callback = callback;
        this.searchPropertyModel = searchPropertyModel;
        this.progressBar = progressBar;
    }

    //CALL THE SEARCH WITH A FILTER
    public void searchPropertiesByFilter() {
        progressBar.setVisibility(View.VISIBLE);
        this.mainViewModel.searchAPropertyWithFilter(searchPropertyModel).observe(activity, this::firstStepByGeneralField);
    }

    //FIRST STEP OF THE FILTER
    private void firstStepByGeneralField(List<PropertyObj> properties) {
        this.listPropertyByFirstStep = properties;
        this.mainViewModel.searchPointOfInterestByName(searchPropertyModel).observe(activity, this::secondStepByPointOfInterestFilter);
    }
    //SECOND STEP OF THE FILTER
    private void secondStepByPointOfInterestFilter(List<PointOfInterest> pointOfInterests) {
        this.listPointOfInterestSecondStep = pointOfInterests;
        this.callback.onResearchIsCompleted(getPropertyByMultipleFilter());
    }
    //FINALLY SORT
    private List<PropertyObj> getPropertyByMultipleFilter() {
        List<PropertyObj> propertiesSearched = new ArrayList<>();

        boolean byDate = false;
        boolean byPOI = false;
        boolean byCity = false;

        // FOR DATE
        Date dateMax = new Date();
        if (!searchPropertyModel.getLastSellDateProperty().equals("none")) {
            if (searchPropertyModel.getLastSellDateProperty().equals("byDay")) {
                dateMax = Utils.subtractTimeToDate(new Date(), 7, 0, 0);
            } else if (searchPropertyModel.getLastSellDateProperty().equals("byMonth")) {
                dateMax = Utils.subtractTimeToDate(new Date(), 0, 1, 0);
            } else {
                dateMax = Utils.subtractTimeToDate(new Date(), 0, 0, 1);
            }
            byDate = true;
        }
        //FOR CITY
        if (!searchPropertyModel.getCityProperty().equals("")) {
            byCity = true;
        }
        //FOR POI
        if (searchPropertyModel.getPointOfInterestProperty().size() > 0) {
            byPOI = true;
        }

        for (PropertyObj property : listPropertyByFirstStep) {
            if (!byCity || searchPropertyModel.getCityProperty().toLowerCase().equals(property.getAddress().getCity().toLowerCase())) {
                if (!byDate || property.getProperty().getDateEnter().getTime() >= dateMax.getTime()) {
                    if (!byPOI) {
                        propertiesSearched.add(property);
                    } else {
                        forBreak:
                        for (PointOfInterest point : property.getPointOfInterests()) {
                            for (PointOfInterest pointSearched : listPointOfInterestSecondStep) {
                                if (point.getName().toLowerCase().equals(pointSearched.getName().toLowerCase())) {
                                    propertiesSearched.add(property);
                                    break forBreak;
                                }
                            }
                        }
                    }
                }
            }
        }

        progressBar.setVisibility(View.GONE);
        return propertiesSearched;
    }

}
