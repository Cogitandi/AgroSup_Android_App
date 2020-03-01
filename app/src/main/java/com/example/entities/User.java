package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.database.DatabaseConverters;

import java.util.List;

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

    @ColumnInfo(name = "amountImpuls")
    private int amountImpuls;

    @ColumnInfo(name = "selectedYearPlan")
    private int selectedYearPlanId;

    @ColumnInfo(name = "selectedOperator")
    private int selectedOperatorId;

    @TypeConverters(DatabaseConverters.class)
    private Machine selectedMachine;

    @TypeConverters(DatabaseConverters.class)
    private List<Machine> machineList;

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

    public int getAmountImpuls() {
        return amountImpuls;
    }

    public void setAmountImpuls(int amountImpuls) {
        this.amountImpuls = amountImpuls;
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

    public Machine getSelectedMachine() {
        return selectedMachine;
    }

    public void setSelectedMachine(Machine selectedMachine) {
        this.selectedMachine = selectedMachine;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}