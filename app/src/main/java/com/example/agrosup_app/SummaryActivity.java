package com.example.agrosup_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
    LinearLayout layout;

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
        layout = findViewById(R.id.summary_Layout);
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
            createSummary(operators, fields);
        }

    }

    private void createSummary(List<Operator> operators, List<FieldWithParcels> fields) {
        operators.add(new Operator(0, "Brak", "dopłat", 0));
        for (Operator operator : operators) {
            TableLayout tableLayout = tableLayout();
            tableLayout.addView(createHeaderRow(operator.getFirstName() + " " + operator.getSurname()));
            List<String> plantNames = fetchPlants(fields, operator.getId(), false);

            for (String plantName : plantNames) {
                String area = countArea(fields, operator.getId(), plantName, false);
                tableLayout.addView(createPlantRow(plantName, area));
            }
            //fuel
            String area = countFuel(fields, operator.getId(), false);
            tableLayout.addView(createPlantRow(getString(R.string.summary_fuelTV), area));

            layout.addView(tableLayout);
        }

        // Whole farm
        TableLayout tableLayout = tableLayout();
        tableLayout.addView(createHeaderRow(getString(R.string.summary_wholeFarmTV)));
        List<String> plantNames = fetchPlants(fields, 0, true);

        for (String plantName : plantNames) {
            String area = countArea(fields, 0, plantName, true);
            tableLayout.addView(createPlantRow(plantName, area));
        }
        //fuel
        String area = countFuel(fields, -1, true);
        tableLayout.addView(createPlantRow(getString(R.string.summary_fuelTV), area));


        layout.addView(tableLayout);
    }

    private TableLayout tableLayout() {
        TableLayout tableLayout = new TableLayout(this);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams();
        float dpRatio = this.getResources().getDisplayMetrics().density;
        int pixelForDp = (int) (20 * dpRatio);
        params.setMargins(0, pixelForDp, 0, 0);
        tableLayout.setLayoutParams(params);

        return tableLayout;
    }

    private TableRow createPlantRow(String plantName, String area) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView left = new TextView(this);
        left.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        left.setGravity(Gravity.CENTER_HORIZONTAL);
        left.setText(plantName);
        left.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        TextView right = new TextView(this);
        right.setGravity(Gravity.CENTER_HORIZONTAL);
        right.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        right.setText(area + " ha");
        right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        row.addView(left);
        row.addView(right);
        return row;
    }

    private TableRow createHeaderRow(String name) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tv.setText(name);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);

        row.addView(tv);
        return row;
    }

    private String countFuel(List<FieldWithParcels> fields, int operatorId, boolean wholeFarm) {
        float area = 0;
        for (FieldWithParcels field : fields) {
            for (Parcel parcel : field.parcels) {
                Log.d("parcel id", parcel.getParcelNumber()+" paliwo:"+parcel.isFuelApplication());
                if (parcel.isFuelApplication() && (parcel.getArimrOperatorId() == operatorId || wholeFarm)) {
                    area += parcel.getCultivatedArea();
                }
            }
        }
        float areaHa = area / 100;
        String areaHaString = Float.toString(areaHa);
        areaHaString = String.format(areaHaString, "%.2f");
        return areaHaString;
    }

    private List<String> fetchPlants(List<FieldWithParcels> fields, int operatorId, boolean wholeFarm) {
        List<String> plantNames = new ArrayList<>();
        for (FieldWithParcels field : fields) {
            String plantName = field.getPlant();
            for (Parcel parcel : field.parcels) {
                if (!plantNames.contains(plantName) && (parcel.getArimrOperatorId() == operatorId || wholeFarm)) {
                    plantNames.add(plantName);
                }
            }
        }
        return plantNames;
    }

    private String countArea(List<FieldWithParcels> fields, int operatorId, String plantName, boolean wholeFarm) {
        float area = 0;
        for (FieldWithParcels field : fields) {
            for (Parcel parcel : field.parcels) {
                if (field.getPlant().equals(plantName) && (parcel.getArimrOperatorId() == operatorId || wholeFarm)) {
                    area += parcel.getCultivatedArea();
                }
            }
        }
        float areaHa = area / 100;
        String areaHaString = Float.toString(areaHa);
        areaHaString = String.format(areaHaString, "%.2f");
        return areaHaString;
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
