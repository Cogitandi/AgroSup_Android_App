package com.example.agrosup_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.TreatmentsListAdapter;
import com.example.database.AppDatabase;
import com.example.entities.Treatment;
import com.example.entities.User;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    AppDatabase db;
    ExpandableListView treatmentsLV;

    Handler handler;
    List<Treatment> treatments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    private void initialize() {
        db = AppDatabase.getInstance(getApplicationContext());
        treatmentsLV = findViewById(R.id.history_treatmentsLV);
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handlerMessage(msg);
            }
        };
        fetchDB();

    }

    private void createTreatmentsList() {
        ExpandableListAdapter adapter = new TreatmentsListAdapter(this, treatments);
        treatmentsLV.setAdapter(adapter);

    }

    private void fetchDB() {
        new Thread(() -> {
            User user = db.userDao().findLogged(true);
            treatments = db.treatmentDao().getAll();
            sendMessage(1,"");
        }).start();
    }

    private void handlerMessage(Message msg) {
        if (msg.what == 1) {
            createTreatmentsList();
        }
    }

    private <T> void sendMessage(int code, T object) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = object;
        handler.sendMessage(msg);
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
