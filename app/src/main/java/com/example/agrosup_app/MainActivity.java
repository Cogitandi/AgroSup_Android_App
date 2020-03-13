package com.example.agrosup_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.api.GetData;
import com.example.api.RetrofitClient;
import com.example.apiModels.Attribut;
import com.example.apiModels.FeatureCollection;
import com.example.apiModels.Layer;
import com.example.database.AppDatabase;
import com.example.entities.Treatment;
import com.example.entities.User;
import com.example.entities.YearPlan;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    AppDatabase db;
    User user;
    private Button treatmentBTN;
    private Button farmBTN;
    private Button settingsBTN;
    private Button logoutBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSelectedOptions();
        hideStatusBar();
    }

    private void getLoggedUser() {
        Thread thread = new Thread(() -> {
            user = db.userDao().findLogged(true);
        });
        thread.start();
    }

    private void initialize() {
        treatmentBTN = findViewById(R.id.main_treatmentBTN);
        farmBTN = findViewById(R.id.main_farmBTN);
        settingsBTN = findViewById(R.id.main_settingsBTN);
        logoutBTN = findViewById(R.id.main_logoutBTN);

        db = AppDatabase.getInstance(getApplicationContext());
        getLoggedUser();

        new Thread(() -> {
            for(Treatment item: db.treatmentDao().getAll()) {
                Log.d("logi",item.getId()+","+item.getStartDate());
            }

        }).start();
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {

                }
            }
        };
        onClickListeners();
    }

    private void onClickListeners() {
        treatmentBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), TreatmentsActivity.class);
            startActivity(intent);
        });

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
            Log.d("yearPlan", yearPlan + "");
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
