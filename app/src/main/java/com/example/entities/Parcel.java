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

    @ColumnInfo(name = "fieldId")
    private int fieldId;

    public Parcel(int id, String parcelNumber, int cultivatedArea, boolean fuelApplication, int yearPlanId) {
        this.id = id;
        this.parcelNumber = parcelNumber;
        this.cultivatedArea = cultivatedArea;
        this.fuelApplication = fuelApplication;
        this.yearPlanId = yearPlanId;

    }

    public void setArimrOperatorId(String arimrOperatorId) {
        if (arimrOperatorId != null) {
            this.arimrOperatorId = new Integer(arimrOperatorId.substring(15));
        } else {
            this.arimrOperatorId = 0;
        }
    }

    public void setFieldId(String fieldId) {
        if (fieldId != null) {
            this.fieldId = new Integer(fieldId.substring(12));
        } else {
            this.fieldId = 0;
        }
    }

    public int getId() {
        return id;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public int getCultivatedArea() {
        return cultivatedArea;
    }

    public boolean isFuelApplication() {
        return fuelApplication;
    }

    public int getArimrOperatorId() {
        return arimrOperatorId;
    }

    public int getYearPlanId() {
        return yearPlanId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public void setCultivatedArea(int cultivatedArea) {
        this.cultivatedArea = cultivatedArea;
    }

    public void setFuelApplication(boolean fuelApplication) {
        this.fuelApplication = fuelApplication;
    }
    public void setYearPlanId(int yearPlanId) {
        this.yearPlanId = yearPlanId;
    }

    public void setArimrOperatorId(int arimrOperatorId) {
        this.arimrOperatorId = arimrOperatorId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }
}
