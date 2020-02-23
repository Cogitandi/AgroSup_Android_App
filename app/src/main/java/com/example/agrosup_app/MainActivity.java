package com.example.agrosup_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.database.AppDatabase;
import com.example.entities.User;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    AppDatabase db;
    User user;
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

    private void getLoggedUser() {
        Thread thread = new Thread(() -> {
            user = db.userDao().findLogged(true);
        });
        thread.start();
    }

    private void initialize() {
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

    private void sendMessage(int id) {
        Message message = new Message();
        message.what = id;
        handler.sendMessage(message);
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
