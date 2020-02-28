package com.example.agrosup_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.database.AppDatabase;

public class TreatmentsActivity extends AppCompatActivity {

    private Button startBTN;
    private Button machinesBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatments);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    private void onClickListeners() {
        startBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), StartTreatmentActivity.class);
            startActivity(intent);
        });

        machinesBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MachinesActivity.class);
            startActivity(intent);
        });


    }

    private void initialize() {
        startBTN = findViewById(R.id.treatment_startBTN);
        machinesBTN = findViewById(R.id.treatment_machinesBTN);

        onClickListeners();
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
