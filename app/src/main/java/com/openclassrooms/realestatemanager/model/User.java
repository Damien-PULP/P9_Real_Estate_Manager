/*
 * Copyright (c) 2021. This code has been written by Damien De Lombaert
 */
package com.openclassrooms.realestatemanager.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Objects;

@Entity
public class User {

    @PrimaryKey
    @NonNull private long id;
    @NonNull private String firstName;
    @NonNull private String secondName;
    @NonNull private String email;
    @NonNull  private String phoneNumber;
    private String icon;
    @NonNull private String password;

    //Need empty constructor
    public User() {
    }
    public User( @NonNull String firstName, @NonNull String secondName, @NonNull String email, @NonNull String phoneNumber, @NonNull String icon, @NonNull String password) {
        this.id = Utils.getRandomLong();
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.icon = icon;
        this.password = password;
    }

    //GETTER
    @NonNull
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
    public void setId(@NonNull long id) {
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

}
