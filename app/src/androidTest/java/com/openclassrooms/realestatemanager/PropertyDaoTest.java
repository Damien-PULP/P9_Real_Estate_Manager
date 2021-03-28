/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.Utils.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.database.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.PointOfInterest;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyAttribute;
import com.openclassrooms.realestatemanager.model.PropertyObj;
import com.openclassrooms.realestatemanager.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PropertyDaoTest {

    // TEST DATABASE
    private RealEstateManagerDatabase database;

    //USER DATA
    private static long uId = 1;
    private static User user = new User("Damien", "De Lombaert", "email@gmail.com", "0404560844", "pathIcon", "password");
    // PROPERTY DATA
    private static Address address = new Address("United State", "New-York", "10000", "IdontKnow", 66, 0);
    private static Photo photo1 = new Photo("/user/picture/photo1.jpg", "Bed room 1", 0);
    private static Photo photo2 = new Photo("/user/picture/photo2.jpg", "WC", 0);
    private static PointOfInterest pointOfInterest = new PointOfInterest("school", 0);
    private static Property property = new Property("House", 1700000f, 12, "Amazing house !!", "Not sell", new Date(), new Date(), uId);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb () throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb () throws Exception {
        database.close();
    }

    @Test
    public void InsertProperty () throws InterruptedException {
        this.database.userDao().createUser(user);
        property.setIdAgent(user.getId());

        this.database.propertyDao().insertProperty(property);

        List<Property> propertiesDb = LiveDataTestUtil.getValue(database.propertyDao().getProperty(user.getId()));
        assertTrue(propertiesDb.get(0).getDescription().equals(property.getDescription()) && propertiesDb.get(0).getPris() == property.getPris());
    }

    @Test
    public void InsertPropertyAndAddress () throws InterruptedException {
        this.database.userDao().createUser(user);
        property.setIdAgent(user.getId());

        this.database.propertyDao().insertProperty(property);

        List<Property> propertiesDb = LiveDataTestUtil.getValue(database.propertyDao().getProperty(user.getId()));

        Address addressOfProperty = address;
        addressOfProperty.setIdProperty(propertiesDb.get(0).getId());
        this.database.addressDao().insertAddress(addressOfProperty);

        Address addressDb = LiveDataTestUtil.getValue(database.addressDao().getAddress(propertiesDb.get(0).getId()));
        assertTrue(propertiesDb.get(0).getDescription().equals(property.getDescription())
                && propertiesDb.get(0).getPris() == property.getPris()
                && addressDb.getNumberStreet().equals(addressOfProperty.getNumberStreet())
                && addressDb.getStreet().equals(addressOfProperty.getStreet()));
    }

    @Test
    public void InsertPropertyAndPhotos () throws InterruptedException {
        this.database.userDao().createUser(user);
        property.setIdAgent(user.getId());

        this.database.propertyDao().insertProperty(property);

        List<Property> propertiesDb = LiveDataTestUtil.getValue(database.propertyDao().getProperty(user.getId()));

        Photo photo1OfProperty = photo1;
        Photo photo2OfProperty = photo2;

        photo1OfProperty.setIdProperty(propertiesDb.get(0).getId());
        photo2OfProperty.setIdProperty(propertiesDb.get(0).getId());

        this.database.photoDao().insertPhoto(photo1OfProperty);
        this.database.photoDao().insertPhoto(photo2OfProperty);

        List<Photo> photos = LiveDataTestUtil.getValue(database.photoDao().getPhotos(propertiesDb.get(0).getId()));

        assertTrue(propertiesDb.get(0).getDescription().equals(property.getDescription())
                && propertiesDb.get(0).getPris() == property.getPris()
                && photos.get(0).getPathIcon().equals(photo1OfProperty.getPathIcon())
                && photos.get(1).getPathIcon().equals(photo2OfProperty.getPathIcon()));
    }

    @Test
    public void InsertPropertyAndPointOfInterest () throws InterruptedException {
        this.database.userDao().createUser(user);
        property.setIdAgent(user.getId());

        this.database.propertyDao().insertProperty(property);

        List<Property> propertiesDb = LiveDataTestUtil.getValue(database.propertyDao().getProperty(user.getId()));

        PointOfInterest pointOfProperty = pointOfInterest;
        pointOfProperty.setIdProperty(propertiesDb.get(0).getId());

        this.database.pointOfInterestDao().insertPointOfInterest(pointOfProperty);

        List<PointOfInterest> pointInDB = LiveDataTestUtil.getValue(database.pointOfInterestDao().getPointsOfInterest(propertiesDb.get(0).getId()));

        assertTrue(propertiesDb.get(0).getDescription().equals(property.getDescription())
                && propertiesDb.get(0).getPris() == property.getPris()
                && pointInDB.get(0).getName().equals(pointOfProperty.getName()));
    }

    @Test
    public void GetPropertyWithAllAttributes () throws InterruptedException {
        this.database.userDao().createUser(user);
        property.setIdAgent(user.getId());

        this.database.propertyDao().insertProperty(property);

        List<Property> propertiesDb = LiveDataTestUtil.getValue(database.propertyDao().getProperty(user.getId()));

        PointOfInterest pointOfProperty = pointOfInterest;
        pointOfProperty.setIdProperty(propertiesDb.get(0).getId());
        this.database.pointOfInterestDao().insertPointOfInterest(pointOfProperty);

        Address addressOfProperty = address;
        addressOfProperty.setIdProperty(propertiesDb.get(0).getId());
        this.database.addressDao().insertAddress(addressOfProperty);

        Photo photo1OfProperty = photo1;
        Photo photo2OfProperty = photo2;
        photo1OfProperty.setIdProperty(propertiesDb.get(0).getId());
        photo2OfProperty.setIdProperty(propertiesDb.get(0).getId());
        this.database.photoDao().insertPhoto(photo1OfProperty);
        this.database.photoDao().insertPhoto(photo2OfProperty);

        List<PropertyObj> propertyObjs = LiveDataTestUtil.getValue(database.propertyDao().getPropertyWithAllAttribute(user.getId()));

        assertNotNull(propertyObjs);

    }

}
