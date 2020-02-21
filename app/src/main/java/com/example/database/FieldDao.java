package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.entities.Field;
import com.example.entities.Operator;
import com.example.entities.YearPlan;

import java.util.List;

@Dao
public interface FieldDao {
    @Query("SELECT * FROM field")
    List<Field> getAll();

    @Query("SELECT * FROM field WHERE yearPlan = :yearPlan")
    List<Field> loadAllByYearPlan(YearPlan yearPlan);

    @Insert
    void insertAll(Field... field);

    @Delete
    void delete(Field field);
}
