package com.example.entities;

import android.widget.Toast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.agrosup_app.R;
import com.example.api.GetData;
import com.example.api.RetrofitClient;
import com.example.apiModels.TransformationApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public Field(int id, String name, int yearPlanId, String plantName) {
        this.id = id;
        this.name = name;
        this.yearPlanId = yearPlanId;
        this.plantName = plantName;
    }

}
