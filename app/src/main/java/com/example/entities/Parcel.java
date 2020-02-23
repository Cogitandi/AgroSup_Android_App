package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Parcel {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "parcelNumber")
    private String parcelNumber;

    @ColumnInfo(name = "cultivatedArea")
    private int cultivatedArea;

    @ColumnInfo(name = "fuelApplication")
    private boolean fuelApplication;

    @ColumnInfo(name = "arimrOperatorId")
    private int arimrOperatorId;

    @ColumnInfo(name = "yearPlanId")
    private int yearPlanId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public int getCultivatedArea() {
        return cultivatedArea;
    }

    public void setCultivatedArea(int cultivatedArea) {
        this.cultivatedArea = cultivatedArea;
    }

    public boolean isFuelApplication() {
        return fuelApplication;
    }

    public void setFuelApplication(boolean fuelApplication) {
        this.fuelApplication = fuelApplication;
    }

    public int getArimrOperatorId() {
        return arimrOperatorId;
    }

    public void setArimrOperatorId(int arimrOperatorId) {
        this.arimrOperatorId = arimrOperatorId;
    }

    public int getYearPlanId() {
        return yearPlanId;
    }

    public void setYearPlanId(int yearPlanId) {
        this.yearPlanId = yearPlanId;
    }

    public Parcel(int id, String parcelNumber, int cultivatedArea, boolean fuelApplication, int arimrOperatorId, int yearPlanId) {
        this.id = id;
        this.parcelNumber = parcelNumber;
        this.cultivatedArea = cultivatedArea;
        this.fuelApplication = fuelApplication;
        this.arimrOperatorId = arimrOperatorId;
        this.yearPlanId = yearPlanId;
    }
}
