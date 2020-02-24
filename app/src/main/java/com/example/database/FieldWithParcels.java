package com.example.database;

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

    @NonNull
    @Override
    public String toString() {
        return field.getName();
    }
}
