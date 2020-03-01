package com.example.database;

import androidx.room.TypeConverter;

import com.example.entities.Machine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DatabaseConverters {

    @TypeConverter
    public Machine stringToMachine(String value) {
        Machine machine = new Gson().fromJson(value, Machine.class);
        return machine;
    }

    @TypeConverter
    public String machineToString(Machine machine) {
        Gson gson = new Gson();
        return gson.toJson(machine);
    }

    @TypeConverter
    public List<Machine> stringListToMachine(String value) {
        Type listType = new TypeToken<List<Machine>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String machineListToString(List<Machine> machineList) {
        Gson gson = new Gson();
        return gson.toJson(machineList);
    }
}
