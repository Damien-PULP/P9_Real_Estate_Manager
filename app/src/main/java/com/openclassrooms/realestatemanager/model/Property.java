package com.openclassrooms.realestatemanager.model;

import androidx.annotation.NonNull;

import java.util.Date;

public class Property {

    @NonNull private String type;
    @NonNull private float pris;
    @NonNull private int nbRoom;
    @NonNull private String description;
    @NonNull private String state;
    @NonNull private Date dateEnter;
    private Date dateSold;

    public Property() {
    }
    public Property(@NonNull String type, float pris, int nbRoom, @NonNull String description, @NonNull String state, @NonNull Date dateEnter, Date dateSold) {
        this.type = type;
        this.pris = pris;
        this.nbRoom = nbRoom;
        this.description = description;
        this.state = state;
        this.dateEnter = dateEnter;
        this.dateSold = dateSold;
    }

    //GETTER
    @NonNull
    public String getType() {
        return type;
    }
    public float getPris() {
        return pris;
    }
    public int getNbRoom() {
        return nbRoom;
    }
    @NonNull
    public String getDescription() {
        return description;
    }
    @NonNull
    public String getState() {
        return state;
    }
    @NonNull
    public Date getDateEnter() {
        return dateEnter;
    }
    public Date getDateSold() {
        return dateSold;
    }

    //SETTER
    public void setType(@NonNull String type) {
        this.type = type;
    }
    public void setPris(float pris) {
        this.pris = pris;
    }
    public void setNbRoom(int nbRoom) {
        this.nbRoom = nbRoom;
    }
    public void setDescription(@NonNull String description) {
        this.description = description;
    }
    public void setState(@NonNull String state) {
        this.state = state;
    }
    public void setDateEnter(@NonNull Date dateEnter) {
        this.dateEnter = dateEnter;
    }
    public void setDateSold(Date dateSold) {
        this.dateSold = dateSold;
    }
}
