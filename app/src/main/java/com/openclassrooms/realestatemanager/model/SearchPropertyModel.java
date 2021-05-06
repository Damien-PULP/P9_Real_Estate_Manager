/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.model;

import java.util.List;

public class SearchPropertyModel {

    private boolean isSoldProperty;
    private String typeProperty;
    private int minSurfaceProperty;
    private int maxSurfaceProperty;
    private int minPrisProperty;
    private int maxPrisProperty;
    private List<String> pointOfInterestProperty;
    private String lastSellDateProperty;
    private String cityProperty;

    public SearchPropertyModel(boolean isSoldProperty, String typeProperty, int minSurfaceProperty, int maxSurfaceProperty, int minPrisProperty, int maxPrisProperty, List<String> pointOfInterestProperty, String lastSellDateProperty, String cityProperty) {
        this.isSoldProperty = isSoldProperty;
        this.typeProperty = typeProperty;
        this.minSurfaceProperty = minSurfaceProperty;
        this.maxSurfaceProperty = maxSurfaceProperty;
        this.minPrisProperty = minPrisProperty;
        this.maxPrisProperty = maxPrisProperty;
        this.pointOfInterestProperty = pointOfInterestProperty;
        this.lastSellDateProperty = lastSellDateProperty;
        this.cityProperty = cityProperty;
    }

    //GETTER
    public boolean isSoldProperty() {
        return isSoldProperty;
    }
    public String getTypeProperty() {
        return typeProperty;
    }
    public int getMinSurfaceProperty() {
        return minSurfaceProperty;
    }
    public int getMaxSurfaceProperty() {
        return maxSurfaceProperty;
    }
    public int getMinPrisProperty() {
        return minPrisProperty;
    }
    public int getMaxPrisProperty() {
        return maxPrisProperty;
    }
    public List<String> getPointOfInterestProperty() {
        return pointOfInterestProperty;
    }
    public String getLastSellDateProperty() {
        return lastSellDateProperty;
    }
    public String getCityProperty() {
        return cityProperty;
    }
    //SETTER
    public void setSoldProperty(boolean soldProperty) {
        isSoldProperty = soldProperty;
    }
    public void setTypeProperty(String typeProperty) {
        this.typeProperty = typeProperty;
    }
    public void setMinSurfaceProperty(int minSurfaceProperty) {
        this.minSurfaceProperty = minSurfaceProperty;
    }
    public void setMaxSurfaceProperty(int maxSurfaceProperty) {
        this.maxSurfaceProperty = maxSurfaceProperty;
    }
    public void setMinPrisProperty(int minPrisProperty) {
        this.minPrisProperty = minPrisProperty;
    }
    public void setMaxPrisProperty(int maxPrisProperty) {
        this.maxPrisProperty = maxPrisProperty;
    }
    public void setPointOfInterestProperty(List<String> pointOfInterestProperty) {
        this.pointOfInterestProperty = pointOfInterestProperty;
    }
    public void setLastSellDateProperty(String lastSellDateProperty) {
        this.lastSellDateProperty = lastSellDateProperty;
    }
    public void setCityProperty(String cityProperty) {
        this.cityProperty = cityProperty;
    }

}
