package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public Operator(int id, String firstName, String surname, int yearPlanId) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.yearPlanId = yearPlanId;
    }

    public int getYearPlanId() {
        return yearPlanId;
    }

    public void setYearPlanId(int yearPlanId) {
        this.yearPlanId = yearPlanId;
    }
}
