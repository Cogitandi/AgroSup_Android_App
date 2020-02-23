package com.example.agrosup_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.database.AppDatabase;
import com.example.entities.Operator;
import com.example.entities.User;
import com.example.entities.YearPlan;

import java.lang.reflect.Array;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    AppDatabase db;

    Spinner yearPlanSPN;
    Spinner operatorSPN;
    EditText impulsET;
    Button saveBTN;

    User user;
    List<YearPlan> yearPlans;
    List<Operator> operators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initialize();
        hideStatusBar();
    }

    private void initialize() {
        db = AppDatabase.getInstance(getApplicationContext());
        yearPlanSPN = findViewById(R.id.settings_yearPlanSPN);
        operatorSPN = findViewById(R.id.settings_operatorSPN);
        impulsET = findViewById(R.id.settings_impulsET);
        saveBTN = findViewById(R.id.settings_saveBTN);
        onClickListeners();
        fetchFromDB(1);
    }

    private void onClickListeners() {
        saveBTN.setOnClickListener(v -> {
            int amountImpus = new Integer("0" + impulsET.getText().toString());
            user.setAmountImpuls(amountImpus);
            saveUser();
            Toast.makeText(getApplication(), getString(R.string.settings_savedToast), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void yearPlanSPN() {
        ArrayAdapter yearPlanSPNAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearPlans);
        yearPlanSPNAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearPlanSPN.setAdapter(yearPlanSPNAdapter);
        yearPlanSPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YearPlan yearPlan = (YearPlan) parent.getItemAtPosition(position);
                user.setSelectedYearPlanId(yearPlan.getId());
                fetchFromDB(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearPlanSPN.setSelection(selectYearPlanPositionByID(yearPlanSPN, user.getSelectedYearPlanId()));
    }

    private int selectYearPlanPositionByID(Spinner spinner, int id) {
        ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            YearPlan yearPlan = (YearPlan) arrayAdapter.getItem(i);
            if (yearPlan.getId() == id) {
                return i;
            }
        }
        return 0;
    }

    private int selectOperatorPositionByID(Spinner spinner, int id) {
        ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            Operator operator = (Operator) arrayAdapter.getItem(i);
            if (operator.getId() == id) {
                return i;
            }
        }
        return 0;
    }

    private void operatorSPN() {
        ArrayAdapter operatorsSPNAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, operators);
        operatorsSPNAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSPN.setAdapter(operatorsSPNAdapter);
        operatorSPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Operator operator = (Operator) parent.getItemAtPosition(position);
                user.setSelectedOperatorId(operator.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        operatorSPN.setSelection(selectOperatorPositionByID(operatorSPN, user.getSelectedOperatorId()));
    }

    private void fetchFromDB(int operation) {
        // 1 - fetch year plans
        // 2 - fetch operator for year plan
        Thread thread = new Thread(() -> {
            if (operation == 1) {
                user = db.userDao().findLogged(true);
                impulsET.setText(Integer.toString(user.getAmountImpuls()));
                yearPlans = db.yearPlanDao().loadAllByUser(user.getId());
                yearPlanSPN();
            }
            if (operation == 2) {
                operators = db.operatorDao().loadAllByYearPlan(user.getSelectedYearPlanId());
                runOnUiThread(() -> {
                    operatorSPN();
                });

            }
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

    private void saveUser() {
        Thread thread = new Thread(() -> {
            db.userDao().updateUsers(user);
        });
        thread.start();
    }
}
