package com.example.agrosup_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.AppDatabase;
import com.example.entities.User;
import com.example.entities.YearPlan;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    AppDatabase db;
    User user;
    private Button farmBTN;
    private Button settingsBTN;
    private Button logoutBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideStatusBar();
        initialize();
        onClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSelectedOptions();
    }

    private void getLoggedUser() {
        Thread thread = new Thread(() -> {
            user = db.userDao().findLogged(true);
        });
        thread.start();
    }

    private void initialize() {
        farmBTN = findViewById(R.id.main_farmBTN);
        settingsBTN = findViewById(R.id.main_settingsBTN);
        logoutBTN = findViewById(R.id.main_logoutBTN);

        db = AppDatabase.getInstance(getApplicationContext());
        getLoggedUser();

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {

                }
            }
        };
    }

    private void onClickListeners() {
        farmBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), FarmActivity.class);
            startActivity(intent);
        });
        logoutBTN.setOnClickListener(v -> {
            logoutBTN.setClickable(false);
            Thread thread = new Thread(() -> {
                user.setLogged(false);
                db.userDao().updateUsers(user);
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                finish();
            });
            thread.start();
        });

        settingsBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), SettingsActivity.class);
            startActivity(intent);
        });

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

    private void checkSelectedOptions() {
        Thread thread = new Thread(() -> {
            user = db.userDao().findLogged(true);
            YearPlan yearPlan = db.yearPlanDao().getYearPlanById(user.getSelectedYearPlanId());
            Log.d("yearPlan", yearPlan+"");
            if (yearPlan == null) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplication(), getString(R.string.main_chooseYearPlanToast), Toast.LENGTH_SHORT).show();
                });

                Intent intent = new Intent(getApplication(), SettingsActivity.class);
                startActivity(intent);

            }
        });
        thread.start();

    }
}
