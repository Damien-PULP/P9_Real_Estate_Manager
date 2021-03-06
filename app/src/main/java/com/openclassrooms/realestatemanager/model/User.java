/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */
package com.openclassrooms.realestatemanager.model;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class User {

    @PrimaryKey
    private long id;
    private String firstName;
    private String secondName;
    private String email;
    private String phoneNumber;
    private String icon;
    private String password;

    //Need empty constructor
    public User() {
    }
    public User( @NonNull String firstName, @NonNull String secondName, @NonNull String email, @NonNull String phoneNumber, @NonNull String icon, @NonNull String password) {
        this.id = 1;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.icon = icon;
        this.password = password;
    }

    //GETTER
    public long getId() {
        return id;
    }
    @NonNull
    public String getFirstName() {
        return firstName;
    }
    @NonNull
    public String getEmail() {
        return email;
    }
    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }
    @NonNull
    public String getPassword() {
        return password;
    }
    @NonNull
    public String getSecondName() {
        return secondName;
    }
    public String getIcon() {
        return icon;
    }

    //SETTER
    public void setId(long id) {
        this.id = id;
    }
    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }
    public void setEmail(@NonNull String email) {
        this.email = email;
    }
    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setPassword(@NonNull String password) {
        this.password = password;
    }
    public void setSecondName(@NonNull String secondName) {
        this.secondName = secondName;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    //UTILS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == (user.id) &&
                firstName.equals(user.firstName) &&
                secondName.equals(user.secondName)&&
                email.equals(user.email) &&
                phoneNumber.equals(user.phoneNumber) &&
                icon.equals(user.icon) &&
                password.equals(user.password);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, email, phoneNumber, password);
    }

    //CONTENT PROVIDER
    public static User fromContentValues(ContentValues values){
        User user = new User();
        if(values.containsKey("id")) user.setId(values.getAsLong("id"));
        if(values.containsKey("firstName")) user.setFirstName(values.getAsString("firstName"));
        if(values.containsKey("secondName")) user.setSecondName(values.getAsString("secondName"));
        if(values.containsKey("email")) user.setEmail(values.getAsString("email"));
        if(values.containsKey("phoneNumber")) user.setPhoneNumber(values.getAsString("phoneNumber"));
        if(values.containsKey("icon")) user.setIcon("icon");
        if(values.containsKey("password")) user.setPassword(values.getAsString("password"));

        return user;
    }

}
