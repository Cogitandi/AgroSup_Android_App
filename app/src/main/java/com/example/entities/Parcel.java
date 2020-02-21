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
    private Integer cultivatedArea;

    @ColumnInfo(name = "fuelApplication")
    private boolean fuelApplication;

    @ColumnInfo(name = "ArimrOperator")
    private Operator arimrOperator;

    @ColumnInfo(name = "yearPlan")
    private YearPlan yearPlan;

    public YearPlan getYearPlan() {
        return yearPlan;
    }

    public void setYearPlan(YearPlan yearPlan) {
        this.yearPlan = yearPlan;
    }

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

    public Integer getCultivatedArea() {
        return cultivatedArea;
    }

    public void setCultivatedArea(Integer cultivatedArea) {
        this.cultivatedArea = cultivatedArea;
    }

    public boolean isFuelApplication() {
        return fuelApplication;
    }

    public void setFuelApplication(boolean fuelApplication) {
        this.fuelApplication = fuelApplication;
    }

    public Operator getArimrOperator() {
        return arimrOperator;
    }

    public void setArimrOperator(Operator arimrOperator) {
        this.arimrOperator = arimrOperator;
    }
}
