package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Treatment {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "email")
    private int operatorId;
    private int fieldId;
    private int area;
    private int machineWidth;
    private String machineName;
    private String date;
    private String start;
    private String end;
    private String description;
}
