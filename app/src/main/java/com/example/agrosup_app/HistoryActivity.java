package com.example.agrosup_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.database.AppDatabase;

public class HistoryActivity extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initialize();
    }

    private void initialize() {
        db = AppDatabase.getInstance(getApplicationContext());
    }

}
