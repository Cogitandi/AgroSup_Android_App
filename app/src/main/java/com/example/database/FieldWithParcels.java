package com.example.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.entities.Field;
import com.example.entities.Parcel;

import java.util.List;

public class FieldWithParcels {
    @Embedded
    public Field field;
    @Relation(
            parentColumn = "id",
            entityColumn = "fieldId"
    )
    public List<Parcel> parcels;

    public float getFieldArea() {
        float area = 0;
        for(Parcel item: parcels) {
            area+=item.getCultivatedArea();
        }
        return area/100;
    }
    @NonNull
    @Override
    public String toString() {
        return field.getName()+"\t\t\t\t ["+getFieldArea()+" ha]";
    }

    public String getPlant() {
        return field.getPlantName();
    }
}
