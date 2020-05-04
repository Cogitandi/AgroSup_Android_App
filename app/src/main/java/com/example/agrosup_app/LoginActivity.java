package com.example.agrosup_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.api.GetData;
import com.example.api.RetrofitClient;
import com.example.api.Synchronizer;
import com.example.database.AppDatabase;
import com.example.database.UserWithYearPlans;
import com.example.entities.Field;
import com.example.entities.Operator;
import com.example.entities.Parcel;
import com.example.entities.User;
import com.example.entities.YearPlan;

public class LoginActivity extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    Button submitBTN;
    GetData service;
    AppDatabase db;
    Handler handler;
    ProgressBar loadingBar;
    String email;

    // msg codes
    // 1 - successfull synchronization
    // 2 - fail login credentials

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    private void initialize() {
        emailET = findViewById(R.id.login_emailET);
        passwordET = findViewById(R.id.login_passwordET);
        submitBTN = findViewById(R.id.login_submitBTN);
        loadingBar = findViewById(R.id.login_loadingBar);
        db = AppDatabase.getInstance(getApplicationContext());
        service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        handler = new Handler(getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    successSynchronization();
                }
                if (msg.what == 2) {
                    failSynchronization(false);
                }
                if (msg.what == 3) {
                    failSynchronization(true);
                }

            }

        };
        onClickListeners();
    }

    private void onClickListeners() {
        submitBTN.setOnClickListener((View v) -> {
            email = emailET.getText().toString();
            String password = passwordET.getText().toString();


            emailET.setVisibility(View.GONE);
            passwordET.setVisibility(View.GONE);
            submitBTN.setVisibility(View.GONE);
            loadingBar.setVisibility(View.VISIBLE);

            Synchronizer sync = new Synchronizer(LoginActivity.this, handler);
            sync.startSynchronization(email, password);
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

    private void printDB() {
        UserWithYearPlans user = db.userDao().userWithYearPlans(1);
        if(user == null) return;
        for (YearPlan yearPlan : user.yearPlans) {
            Log.d("yearPlanId", yearPlan.getId() + "");
            for (Operator operator : db.yearPlanDao().yearPlanWithOperators(1, yearPlan.getId()).operators) {
                Log.d("operator id: ", operator.getFirstName());
            }
            for (Field field : db.yearPlanDao().yearPlanWithFields(1, yearPlan.getId()).fields) {
                Log.d("field id: ", field.getName());
            }
            for (Parcel parcel : db.yearPlanDao().yearPlanWithParcels(1, yearPlan.getId()).parcels) {
                Log.d("parcel id: ", parcel.getParcelNumber());
                Log.d("operator id: ", parcel.getArimrOperatorId()+"x");
            }

        }
    }

    private void successSynchronization() {
        printDB();
        makeLogged(email);
        Toast.makeText(getApplication(), getString(R.string.login_successfullSynchronization), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void failSynchronization(boolean isIncorrect) {

        Log.d("d", "dost");
        emailET.setVisibility(View.VISIBLE);
        passwordET.setVisibility(View.VISIBLE);
        submitBTN.setVisibility(View.VISIBLE);
        loadingBar.setVisibility(View.GONE);
        if (isIncorrect)
            Toast.makeText(getApplication(), getString(R.string.login_incorrectCredentials), Toast.LENGTH_SHORT).show();
    }

    private void makeLogged(String email) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                User user = db.userDao().findByName(email);
                user.setLogged(true);
                db.userDao().updateUsers(user);
            }
        });
        thread.start();
    }

}
