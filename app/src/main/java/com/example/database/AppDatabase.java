package com.example.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.entities.Field;
import com.example.entities.Operator;
import com.example.entities.Parcel;
import com.example.entities.Treatment;
import com.example.entities.User;
import com.example.entities.YearPlan;

@Database(entities = {User.class, YearPlan.class, Operator.class, Field.class, Parcel.class, Treatment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract YearPlanDao yearPlanDao();
    public abstract OperatorDao operatorDao();
    public abstract FieldDao fieldDao();
    public abstract ParcelDao parcelDao();
    public abstract TreatmentDao treatmentDao();

    /** The only instance */
    private static AppDatabase sInstance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "agroSupDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }
}