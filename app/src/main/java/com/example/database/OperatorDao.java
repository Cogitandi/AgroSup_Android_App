package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.entities.Operator;
import com.example.entities.Parcel;

import java.util.List;

@Dao
public interface OperatorDao {
    @Query("SELECT * FROM operator")
    List<Operator> getAll();

    @Query("SELECT * FROM operator WHERE yearPlanId = :yearPlanId")
    List<Operator> loadAllByYearPlan(int yearPlanId);

    @Query("DELETE FROM operator WHERE yearPlanId = :yearPlanId")
    void deleteOperatorsByYearPlanId(int yearPlanId);

    @Query("SELECT * FROM operator WHERE yearPlanId = :yearPlanId AND id = :id")
    Operator findOperatorById(int yearPlanId, int id);


    @Insert
    void insertAll(Operator... operator);

    @Delete
    void delete(Operator operator);
}
