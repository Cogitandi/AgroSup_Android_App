package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.entities.Field;
import com.example.entities.Parcel;
import com.example.entities.YearPlan;

import java.util.List;

@Dao
public interface ParcelDao {
    @Query("SELECT * FROM parcel")
    List<Parcel> getAll();

    @Query("SELECT * FROM parcel WHERE yearPlan = :yearPlan")
    List<Parcel> loadAllByYearPlan(YearPlan yearPlan);

    @Insert
    void insertAll(Parcel... parcel);

    @Delete
    void delete(Parcel parcel);
}
