package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.entities.Field;

import java.util.List;

@Dao
public interface FieldDao {
    @Query("SELECT * FROM field")
    List<Field> getAll();

    @Query("SELECT * FROM field WHERE yearPlanId = :yearPlanId")
    List<Field> loadAllByYearPlan(int yearPlanId);

    @Query("DELETE FROM field WHERE yearPlanId = :yearPlanId")
    void deleteFieldsByYearPlanId(int yearPlanId);

    @Insert
    void insertAll(Field... field);

    @Delete
    void delete(Field field);
}
