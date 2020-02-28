package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.entities.Parcel;

import java.util.List;

@Dao
public interface ParcelDao {
    @Query("SELECT * FROM parcel")
    List<Parcel> getAll();

    @Query("SELECT * FROM parcel WHERE yearPlanId = :yearPlanId")
    List<Parcel> loadAllByYearPlan(int yearPlanId);

    @Query("SELECT fieldId FROM parcel WHERE yearPlanId = :yearPlanId AND parcelNumber = :number")
    int findFieldIdByParcelNumber(int yearPlanId, String number);

    @Query("DELETE FROM parcel WHERE yearPlanId = :yearPlanId")
    void deleteParcelsByYearPlanId(int yearPlanId);

    @Insert
    void insertAll(Parcel... parcel);

    @Delete
    void delete(Parcel parcel);
}
