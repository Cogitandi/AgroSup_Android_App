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

    @ColumnInfo(name = "yearPlan")
    private YearPlan yearPlan;

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

    public YearPlan getYearPlan() {
        return yearPlan;
    }

    public void setYearPlan(YearPlan yearPlan) {
        this.yearPlan = yearPlan;
    }
}
