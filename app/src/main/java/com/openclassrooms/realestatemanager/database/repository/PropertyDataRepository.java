/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.openclassrooms.realestatemanager.database.dao.AddressDao;
import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.database.dao.PointOfInterestDao;
import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.SearchPropertyModel;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class PropertyDataRepository {

    private final PropertyDao propertyDao;
    private final AddressDao addressDao;
    private final PhotoDao photoDao;
    private final PointOfInterestDao pointOfInterestDao;

    public PropertyDataRepository(PropertyDao propertyDao, AddressDao addressDao, PhotoDao photoDao, PointOfInterestDao pointOfInterestDao) {
        this.propertyDao = propertyDao;
        this.addressDao = addressDao;
        this.photoDao = photoDao;
        this.pointOfInterestDao = pointOfInterestDao;
    }

    // SEARCH
    public LiveData<List<PropertyObj>> searchPropertyWithFilter (SearchPropertyModel searchPropertyModel){
        return this.propertyDao.searchPropertyWithFilter(Utils.getQueriesByFilter(searchPropertyModel));
    }
    public LiveData<List<PointOfInterest>> getAllPointOfInterest(){
        return this.pointOfInterestDao.getAllPointsOfInterest();
    }
    public LiveData<List<PointOfInterest>> searchPointOfInterestWithFilter(SearchPropertyModel searchPropertyModel){
        return this.pointOfInterestDao.searchPointOfInterestWithFilter(Utils.getQueriesByFilterForPointOfInterest(searchPropertyModel));
    }
    // GET
    public LiveData<List<Property>> getProperties (long uId){ return this.propertyDao.getProperty(uId); }
    public LiveData<PropertyObj> getAPropertyObj (long idProperty){ return this.propertyDao.getAPropertyObj(idProperty); }
    public LiveData<Address> getAddress (long idProperty){ return this.addressDao.getAddress(idProperty); }
    public LiveData<List<Photo>> getPhotos (long idProperty){ return this.photoDao.getPhotos(idProperty);}
    public LiveData<Photo> getAPhoto (long idPhoto){ return this.photoDao.getAPhoto(idPhoto); }
    public LiveData<List<PointOfInterest>> getPointOfInterests (long idProperty) { return this.pointOfInterestDao.getPointsOfInterest(idProperty); }
    public LiveData<List<PropertyObj>> getPropertyWithAttributes (long idUser) { return this.propertyDao.getPropertyWithAllAttribute(idUser); }
    /*LiveData<List<PropertyAttribute>> getPropertiesWithAttributes (Property property, User user){
        return new PropertyAttribute(property, getPointOfInterests(property.getId()), getPhotos(property.getId()), getAddress(property.getId()), user)
    }*/

    // CREATE
    public long createProperty (Property property){
        return this.propertyDao.insertProperty(property);
    }
    public void createAddress (Address address){
        this.addressDao.insertAddress(address);
    }
    public long createPhoto (Photo photo){
        return this.photoDao.insertPhoto(photo);
    }
    public void createPointOfInterest(PointOfInterest pointOfInterest){
        this.pointOfInterestDao.insertPointOfInterest(pointOfInterest);
    }
    // UPDATE
    public void updateStateProperty(long id, String state){ this.propertyDao.updateStateProperty(id,state); }
    public void updateProperty (Property property){
        this.propertyDao.updateProperty(property);
    }
    public void updateAddress (Address address){
        this.addressDao.updateAddress(address);
    }
    public void updatePhoto (Photo photo){
        this.photoDao.updatePhoto(photo);
    }
    public void updatePointOfInterest (PointOfInterest pointOfInterest){
        this.pointOfInterestDao.updatePointOfInterest(pointOfInterest);
    }
    // DELETE
    public void deleteProperty (long idProperty){
        this.propertyDao.deleteProperty(idProperty);
    }
    public void deleteAddress (long idProperty){
        this.addressDao.deleteAddress(idProperty);
    }
    public void deletePhotosOfProperty (long idProperty){
        this.photoDao.deletePhotosOfAProperty(idProperty);
    }
    public void deletePointOfInterestOfProperty (long idProperty){
        this.pointOfInterestDao.deletePointsOfInterestOfProperty(idProperty);
    }

}
