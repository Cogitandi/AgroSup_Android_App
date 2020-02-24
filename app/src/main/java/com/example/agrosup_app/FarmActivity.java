package com.example.agrosup_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.database.AppDatabase;
import com.example.entities.User;
import com.example.entities.YearPlan;

public class FarmActivity extends AppCompatActivity {

    AppDatabase db;
    YearPlan yearPlan;
    TextView yearPlanTV;
    Button fieldsBTN;
    Button historyBTN;
    Button summaryBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        hideStatusBar();
        initialize();
    }

    private void initialize() {
        db = AppDatabase.getInstance(getApplicationContext());
        yearPlanTV = findViewById(R.id.farm_yearPlanTV);
        fieldsBTN = findViewById(R.id.farm_fieldsBTN);
        historyBTN = findViewById(R.id.farm_historyBTN);
        summaryBTN = findViewById(R.id.farm_summaryBTN);
        onClickListeners();
        getSelectedYearPlan();

    }

    private void onClickListeners() {
        fieldsBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), FieldsActivity.class);
            startActivity(intent);
        });
        historyBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HistoryActivity.class);
            startActivity(intent);
        });
        summaryBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), SummaryActivity.class);
            startActivity(intent);
        });
    }

    private void getSelectedYearPlan() {
        Thread thread = new Thread(() -> {
            User user = db.userDao().findLogged(true);
            yearPlan = db.yearPlanDao().getYearPlanById(user.getSelectedYearPlanId());
            runOnUiThread(() -> {
                yearPlanTV.setText(getString(R.string.farm_yearPlanTV) + "\n" + yearPlan.getStartYear() + "/" + yearPlan.getEndYear());
            });
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
