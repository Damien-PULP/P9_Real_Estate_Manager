/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */

package com.openclassrooms.realestatemanager.database.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.model.User;

public class UserDataRepository {

    //DAO
    private final UserDao userDao;

    public UserDataRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    //GET USER
    public LiveData<User> getUser (String firstName, String secondName, String password) {
        return this.userDao.getUser(firstName, secondName, password);
    }
    //GET USER
    public LiveData<User> getUserSimplify () {
        return this.userDao.getUserSimplify();
    }
    //CREATE
    public void createUser (User user){
        this.userDao.createUser(user);
    }
    //UPDATE
    public void updateUser (User user){
        this.userDao.updateUser(user);
    }
    //DELETE
    public void deleteUser (String firstName, String secondName, String password){
        this.userDao.deleteUser(firstName,secondName,password);
    }

}
