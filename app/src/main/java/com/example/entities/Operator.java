package com.example.entities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.apiModels.OperatorApi;

@Entity
public class Operator {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "firstName")
    private String firstName;

    @ColumnInfo(name = "surname")
    private String surname;

    @ColumnInfo(name = "yearPlanId")
    private int yearPlanId;

    @NonNull
    @Override
    public String toString() {
        return firstName+" "+surname;
    }

    public Operator(int id, String firstName, String surname, int yearPlanId) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.yearPlanId = yearPlanId;
    }
    public Operator(OperatorApi operatorApi) {
        this.id = operatorApi.getId();
        this.firstName = operatorApi.getFirstName();
        this.surname = operatorApi.getSurname();
        this.yearPlanId = new Integer(operatorApi.getYearPlan().substring(16));
        Log.d("test", operatorApi.getYearPlan() + "," + yearPlanId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getYearPlanId() {
        return yearPlanId;
    }

    public void setYearPlanId(int yearPlanId) {
        this.yearPlanId = yearPlanId;
    }
}
