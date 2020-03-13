package com.example.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class Treatment {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int operatorId;
    private int fieldId;
    private int area;
    private float combustedFuel;
    private int machineWidth;
    private String machineName;
    private String startDate;
    private String endDate;
    private String description;

    public Treatment(User user, Machine machine, Field field) {
        String currentTime = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(new Date());
        this.operatorId = user.getSelectedOperatorId();
        this.fieldId = field.getId();
        this.machineWidth = machine.getWidth();
        this.machineName = machine.getName();
        this.startDate = currentTime;
    }

    public Treatment(int id, int operatorId, int fieldId, int area, float combustedFuel, int machineWidth, String machineName, String startDate, String endDate, String description) {
        this.id = id;
        this.operatorId = operatorId;
        this.fieldId = fieldId;
        this.area = area;
        this.combustedFuel = combustedFuel;
        this.machineWidth = machineWidth;
        this.machineName = machineName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public float getCombustedFuel() {
        return combustedFuel;
    }

    public void setCombustedFuel(float combustedFuel) {
        this.combustedFuel = combustedFuel;
    }

    public int getMachineWidth() {
        return machineWidth;
    }

    public void setMachineWidth(int machineWidth) {
        this.machineWidth = machineWidth;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate() {
        String currentTime = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(new Date());
        this.endDate = currentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
