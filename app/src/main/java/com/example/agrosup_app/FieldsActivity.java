package com.example.agrosup_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.database.AppDatabase;
import com.example.database.FieldWithParcels;
import com.example.entities.Operator;
import com.example.entities.Parcel;
import com.example.entities.User;
import com.example.fieldsList.ExpandableListAdapter;
import com.example.fieldsList.FieldsGroup;

import java.util.List;

public class FieldsActivity extends AppCompatActivity {

    Handler handler;
    AppDatabase db;
    User user;
    List<Operator> operators;
    List<FieldWithParcels> fields;
    ExpandableListView fieldsLV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
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
        int i = 0;
        SparseArray<FieldsGroup> groups = new SparseArray<>();
        for (FieldWithParcels item : fields) {
            FieldsGroup group = new FieldsGroup(item.toString());
            for (Parcel parcel : item.parcels) {
                group.children.add(parcel.getParcelNumber() + " - " + parcel.getCultivatedArea());
            }
            groups.append(i++, group);
        }

        ExpandableListAdapter adapter = new ExpandableListAdapter(this, fields);
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

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


}
