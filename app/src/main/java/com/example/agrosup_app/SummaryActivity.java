package com.example.agrosup_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.database.AppDatabase;
import com.example.database.FieldWithParcels;
import com.example.entities.Operator;
import com.example.entities.User;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    Handler handler;
    AppDatabase db;
    User user;
    List<Operator> operators;
    List<FieldWithParcels> fields;
    ListView fieldsLV;

    // 1 - db executed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        initialize();

    }

    private void initialize() {
        db = AppDatabase.getInstance(getApplicationContext());
    }
}
