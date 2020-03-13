package com.example.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.entities.Treatment;

import java.util.List;
@Dao
public interface TreatmentDao {

    @Query("SELECT * FROM Treatment")
    List<Treatment> getAll();

    @Query("SELECT * FROM Treatment where id = (:id)")
    Treatment getTreatment (int id);

    @Insert
    void insertAll(Treatment... treatment);

    @Delete
    void delete(Treatment treatment);
}
