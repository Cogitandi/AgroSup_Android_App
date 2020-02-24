package com.example.agrosup_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.database.AppDatabase;
import com.example.database.FieldWithParcels;
import com.example.entities.Operator;
import com.example.entities.User;

import java.util.List;

public class FieldsActivity extends AppCompatActivity {

    Handler handler;
    AppDatabase db;
    User user;
    List<Operator> operators;
    List<FieldWithParcels> fields;
    ListView fieldsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields);
        initialize();
    }

    private void initialize() {
        db = AppDatabase.getInstance(getApplicationContext());
        fieldsLV = findViewById(R.id.fields_fieldsLV);
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                messageAction(msg);
            }
        };
        fetchFromDb();
    }

    private void messageAction(Message msg) {
        if (msg.what == 1) {
            createFieldsList();
        }

    }

    private void createFieldsList() {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.fieldslv_row, fields);
        fieldsLV.setAdapter(adapter);
    }

    public void sendMessage(int code) {
        Message msg = new Message();
        msg.what = code;
        handler.sendMessage(msg);
    }

    private void fetchFromDb() {
        Thread thread = new Thread(() -> {
            user = db.userDao().findLogged(true);
            operators = db.operatorDao().loadAllByYearPlan(user.getSelectedYearPlanId());
            fields = db.yearPlanDao().fieldsWithParcels(user.getSelectedYearPlanId());
            sendMessage(1);
        });
        thread.start();

    }


}
