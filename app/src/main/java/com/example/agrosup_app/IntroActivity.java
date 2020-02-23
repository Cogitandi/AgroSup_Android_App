package com.example.agrosup_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.database.AppDatabase;

public class IntroActivity extends AppCompatActivity {

    //msg
    // 1 - unlogged
    // 2 - logged
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initialize();
        isLogged();
    }

    private void initialize() {
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Intent intent = new Intent(getApplication(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (msg.what == 2) {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    private void isLogged() {
        Thread thread = new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            if (db.userDao().findLogged(true) != null) {
                sendMessage(2);
            } else {
                sendMessage(1);
            }

        });
        thread.start();
    }

    private void sendMessage(int id) {
        Message message = new Message();
        message.what = id;
        handler.sendMessage(message);
    }
}
