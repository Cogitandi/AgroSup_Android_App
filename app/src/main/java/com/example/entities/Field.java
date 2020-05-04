package com.example.entities;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.apiModels.FieldApi;

@Entity
public class Field {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "yearPlanId")
    private int yearPlanId;

    @ColumnInfo(name = "plantName")
    private String plantName;

    @ColumnInfo(name = "plantVariety")
    private String plantVariety;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearPlanId() {
        return yearPlanId;
    }

    public void setYearPlanId(int yearPlanId) {
        this.yearPlanId = yearPlanId;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantVariety() {
        return plantVariety;
    }

    public void setPlantVariety(String plantVariety) {
        this.plantVariety = plantVariety;
    }

    public Field(FieldApi fieldApi) {
        this.id = fieldApi.getId();
        this.name = fieldApi.getName();
        this.plantName = fieldApi.getPlant();
        this.plantVariety = fieldApi.getPlantVariety();
        this.yearPlanId = new Integer(fieldApi.getYearPlan().substring(16));
        Log.d("test", fieldApi.getYearPlan() + "," + yearPlanId);
    }

    public Field(int id, String name, int yearPlanId, String plantName, String plantVariety) {
        this.id = id;
        this.name = name;
        this.yearPlanId = yearPlanId;
        this.plantName = plantName;
        this.plantVariety = plantVariety;
    }
}
