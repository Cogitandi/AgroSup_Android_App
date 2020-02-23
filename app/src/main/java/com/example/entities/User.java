package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "logged")
    private boolean logged;

    @ColumnInfo(name = "sensorDistance")
    private int sensorDistance;

    @ColumnInfo(name = "selectedYearPlan")
    private int selectedYearPlanId;

    @ColumnInfo(name = "selectedOperator")
    private int selectedOperatorId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public int getSensorDistance() {
        return sensorDistance;
    }

    public void setSensorDistance(int sensorDistance) {
        this.sensorDistance = sensorDistance;
    }

    public int getSelectedYearPlanId() {
        return selectedYearPlanId;
    }

    public void setSelectedYearPlanId(int selectedYearPlanId) {
        this.selectedYearPlanId = selectedYearPlanId;
    }

    public int getSelectedOperatorId() {
        return selectedOperatorId;
    }

    public void setSelectedOperatorId(int selectedOperatorId) {
        this.selectedOperatorId = selectedOperatorId;
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}