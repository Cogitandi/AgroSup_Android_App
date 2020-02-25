package com.example.agrosup_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.AppDatabase;
import com.example.database.FieldWithParcels;
import com.example.entities.Operator;
import com.example.entities.Parcel;
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
        fetchFromDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    private void initialize() {
        db = AppDatabase.getInstance(getApplicationContext());
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                messageAction(msg);
            }
        };

    }

    private void messageAction(Message msg) {
        if (msg.what == 1) {
            createSummary(operators,fields);
        }

    }

    private void createSummary(List<Operator> operators, List<FieldWithParcels> fields) {
        String text = "";
        for (Operator operator : operators) {
            text += "\noperator: "+operator.getFirstName()+" "+operator.getSurname()+"\n";
            List<String> plantNames = fetchPlants(fields, operator.getId());
            for (String plantName : plantNames) {
                text+=plantName+": "+countArea(fields, operator.getId(), plantName+"\n");
                //Log.d(plantName, countArea(fields, operator.getId(), plantName)+"");
            }
        }
        TextView tv= findViewById(R.id.summary_smTV);
        tv.setText(text);
    }

    private List<String> fetchPlants(List<FieldWithParcels> fields, int operatorId) {
        List<String> plantNames = new ArrayList<>();
        for (FieldWithParcels field : fields) {
            String plantName = field.getPlant();
            for (Parcel parcel : field.parcels) {
                if (!plantNames.contains(plantName) && parcel.getArimrOperatorId() == operatorId) {
                    plantNames.add(plantName);
                }
            }
        }
        return plantNames;
    }

    private int countArea(List<FieldWithParcels> fields, int operatorId, String plantName) {
        int area = 0;
        for (FieldWithParcels field : fields) {
            for (Parcel parcel : field.parcels) {
                if (field.getPlant() == plantName && parcel.getArimrOperatorId() == operatorId) {
                    area += parcel.getCultivatedArea();
                }
            }
        }
        return area;
    }

    private void sendMessage(int code) {
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
