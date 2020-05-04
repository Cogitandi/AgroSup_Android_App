package com.example.entities;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.apiModels.ParcelApi;

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

    public Parcel(int id, String parcelNumber, int cultivatedArea, boolean fuelApplication, int arimrOperatorId, int yearPlanId, int fieldId) {
        this.id = id;
        this.parcelNumber = parcelNumber;
        this.cultivatedArea = cultivatedArea;
        this.fuelApplication = fuelApplication;
        this.arimrOperatorId = arimrOperatorId;
        this.yearPlanId = yearPlanId;
        this.fieldId = fieldId;
    }

    public Parcel(ParcelApi parcelApi) {
        this.id = parcelApi.getId();
        this.parcelNumber = parcelApi.getParcelNumber();
        this.cultivatedArea = parcelApi.getCultivatedArea();
        this.fuelApplication = parcelApi.isFuelApplication();
        this.yearPlanId = new Integer(parcelApi.getYearPlan().substring(16));
        this.fieldId = new Integer(parcelApi.getField().substring(12));
        setArimrOperatorId(parcelApi.getArimrOperator());
        Log.d("test", parcelApi.getYearPlan() + "," + yearPlanId);
        Log.d("test", parcelApi.getField() + "," + fieldId);
    }

    public void setArimrOperatorId(String arimrOperatorId) {
        if (arimrOperatorId != null) {
            this.arimrOperatorId = new Integer(arimrOperatorId.substring(15));
        } else {
            this.arimrOperatorId = 0;
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
}
