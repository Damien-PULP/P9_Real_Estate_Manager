/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.database.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    // TEST DATABASE
    private RealEstateManagerDatabase database;

    //USER DATA
    private static long uId = 1;
    private static final User user = new User("Damien", "De Lombaert", "email@gmail.com", "0404560844", "pathIcon", "password");

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb () {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        uId = user.getId();
    }

    @After
    public void closeDb () {
        database.close();
    }

    @Test
    public void insertAndGetUser () throws InterruptedException {
        this.database.userDao().createUser(user);
        User userDb = LiveDataTestUtil.getValue(this.database.userDao().getUser(user.getFirstName(), user.getSecondName(), user.getPassword()));
        assertEquals(userDb.getFirstName(), user.getFirstName());
    }

    @Test
    public void updateAndGetUser () throws InterruptedException {
        this.database.userDao().createUser(user);
        User userUpdated = user;
        userUpdated.setFirstName("Paul");
        this.database.userDao().updateUser(userUpdated);
        User userDb = LiveDataTestUtil.getValue(this.database.userDao().getUser(userUpdated.getFirstName(), userUpdated.getSecondName(), userUpdated.getPassword()));
        assertEquals(userDb.getFirstName(), user.getFirstName());
    }

    @Test
    public void deleteAndGetUser () throws InterruptedException {
        this.database.userDao().createUser(user);
        this.database.userDao().deleteUser(user.getFirstName(), user.getSecondName(), user.getPassword());
        User userDb = LiveDataTestUtil.getValue(this.database.userDao().getUser(user.getFirstName(), user.getSecondName(), user.getPassword()));
        assertNull(userDb);
    }

}
