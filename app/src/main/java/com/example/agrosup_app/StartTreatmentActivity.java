package com.example.agrosup_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.api.GetData;
import com.example.api.RetrofitClient;
import com.example.apiModels.FeatureCollection;
import com.example.apiModels.Layer;
import com.example.apiModels.TransformationApi;
import com.example.database.AppDatabase;
import com.example.entities.Field;
import com.example.entities.Machine;
import com.example.entities.Operator;
import com.example.entities.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartTreatmentActivity extends AppCompatActivity {

    LinearLayout mainLayout;
    LinearLayout fieldListLayout;
    TableLayout dataTL;
    TextView operatorTV;
    TextView fieldTV;
    TextView clockTV;
    TextView fieldAreaTV;
    TextView tempTV;
    TextView fuelSumTV;
    TextView cultivatedAreaTV;
    TextView temporaryFuelTV;
    TextView machineTV;
    TextView widthTV;
    Button saveBTN;

    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;
    Handler handler;
    GetData service;
    AppDatabase db;
    User user;
    Operator choosenOperator;


    List<Field> foundFields;
    Field choosenField;

    TextView tv;
    Button startb;
    Button stopb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_treatment);
        initialize();

        boolean permissionAccessCoarseLocationApproved = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permissionAccessCoarseLocationApproved) {
            // App has permission to access location in the foreground. Start your
            // foreground service that has a foreground service type of "location".
        } else {
            // Make a request for foreground-only location access.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);

        startLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    // Start gps
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    // Buttons actions
    private void onClickListeners() {
        startb.setOnClickListener(v -> {
            startLocationUpdates();
        });
        stopb.setOnClickListener(v -> {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        });
        saveBTN.setOnClickListener(v -> {
            recreate();
        });
    }

    // Initialize variables
    private void initialize() {
        mainLayout = findViewById(R.id.streatment_mainLayout);
        fieldListLayout = findViewById(R.id.streatment_fieldListLayout);
        dataTL = findViewById(R.id.streatment_dataTL);
        operatorTV = findViewById(R.id.streatment_operatorTV);
        fieldTV = findViewById(R.id.streatment_fieldTV);
        clockTV = findViewById(R.id.streatment_clockTV);
        fieldAreaTV = findViewById(R.id.streatment_fieldAreaTV);
        tempTV = findViewById(R.id.streatment_tempTV);
        fuelSumTV = findViewById(R.id.streatment_fuelSumTV);
        cultivatedAreaTV = findViewById(R.id.streatment_cultivatedAreaTV);
        temporaryFuelTV = findViewById(R.id.streatment_temporaryFuelTV);
        machineTV = findViewById(R.id.streatment_machineTV);
        widthTV = findViewById(R.id.streatment_widthTV);
        saveBTN = findViewById(R.id.streatment_saveBTN);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        service = RetrofitClient.getRetrofitTransInstance().create(GetData.class);
        db = AppDatabase.getInstance(getApplicationContext());


        foundFields = new ArrayList<>();


        tv = findViewById(R.id.test);
        startb = findViewById(R.id.startbtn);
        stopb = findViewById(R.id.stopbtn);

        onClickListeners();
        fetchDB();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    convertCordinates(location.getLongitude(), location.getLatitude());
                    tv.setText("precyzja:" + location.getAccuracy());
                }
            }
        };

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                messageAction(msg);
            }
        };


    }

    // Convert coordinates from epsg:4326 to epsg:2180
    private void convertCordinates(double x, double y) {
        Call<TransformationApi> call = service.getConverted(x, y, 0, 4326, 2180);
        call.enqueue(new Callback<TransformationApi>() {
            @Override
            public void onResponse(Call<TransformationApi> call, Response<TransformationApi> response) {
                if (response.isSuccessful()) {
                    TransformationApi transformationApi = response.body();
                    sendMessage(1, transformationApi);
                }
            }

            @Override
            public void onFailure(Call<TransformationApi> call, Throwable t) {
                Toast.makeText(getApplication(), getString(R.string.startTreatment_convertError), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Action depends on code received by handler
    private void messageAction(Message msg) {
        // conversion done - send to fetch information about parcel FROM convertCordinates()
        if (msg.what == 1) {
            TransformationApi transformationApi = (TransformationApi) msg.obj;
            getParcelEwApi(transformationApi);

        }
        //get parcel ewi object FROM getParcelEwApi()
        if (msg.what == 2) {
            Layer layer = (Layer) msg.obj;
            findFieldByParcelNumber(layer.getMap().get("Numer działki"));
            for (Map.Entry<String, String> entry : layer.getMap().entrySet()) {
                Log.d(entry.getKey(), entry.getValue());
            }
        }
        //get field FROM findFieldByParcelNumber()
        if (msg.what == 3) {
            Field field = (Field) msg.obj;
            if (isNotInArray(foundFields, field)) {
                foundFields.add(field);
                Button fieldButton = createButton(field.getName());
                fieldButton.setOnClickListener(v -> {
                    Machine machine = new Machine("Pług", 165);
                    user.setSelectedMachine(machine);
                    // remove location
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                    choosenField = field;
                    fieldListLayout.setVisibility(View.GONE);
                    dataTL.setVisibility(View.VISIBLE);
                    updateUI();
                });
                fieldListLayout.addView(fieldButton);
            }

        }

    }

    private boolean isNotInArray(List<Field> fieldList, Field field) {
        for (Field item : fieldList) {
            if (item == field) {
                return false;
            }
        }
        return true;
    }

    // Send field object with typed parcel number to messageAction
    private void findFieldByParcelNumber(String number) {
        Thread thread = new Thread(() -> {
            if (user != null) {
                int fieldId = db.parcelDao().findFieldIdByParcelNumber(user.getSelectedYearPlanId(), number);
                if (fieldId != 0) {
                    Field field = db.fieldDao().getFieldById(fieldId);
                    sendMessage(3, field);
                }

            }

        });
        thread.start();
    }

    private Button createButton(String name) {
        Button button = new Button(this);
        button.setText(name);
        return button;
    }

    private void updateUI() {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        setFieldAreaTV(choosenField);

        clockTV.setText(currentTime);
        operatorTV.setText(choosenOperator.toString());
        fieldTV.setText(choosenField.getName());
        tempTV.setText("23" + (char) 0x00B0);
//        fuelSumTV.setText("");
//        cultivatedAreaTV.setText("");
//        temporaryFuelTV.setText("");
        machineTV.setText(user.getSelectedMachine().getName());
        widthTV.setText(user.getSelectedMachine().getWidthM());
    }

    // update fieldAreaTV
    private void setFieldAreaTV(Field field) {
        Thread thread = new Thread(() -> {
            float area = db.fieldDao().fieldsWithParcels(field.getId()).getFieldArea();
            runOnUiThread(() -> {
                fieldAreaTV.setText(area + "");
            });
        });
        thread.start();
    }

    // set user and choosen operator
    private void fetchDB() {
        Thread thread = new Thread(() -> {
            user = db.userDao().findLogged(true);
            choosenOperator = db.operatorDao().findOperatorById(user.getSelectedOperatorId());
        });
        thread.start();
    }

    // Fetch parcel information
    private void getParcelEwApi(TransformationApi transformationApi) {
        GetData retro = RetrofitClient.getRetrofitInstanceXML().create(GetData.class);
        Call<FeatureCollection> call = retro.getParcelEw(transformationApi.getCoordinates());
        call.enqueue(new Callback<FeatureCollection>() {
            @Override
            public void onResponse(Call<FeatureCollection> call, Response<FeatureCollection> response) {
                if (response.isSuccessful()) {
                    Layer layer = response.body().getFeatureMember().getLayer();
                    sendMessage(2, layer);
                }

            }

            @Override
            public void onFailure(Call<FeatureCollection> call, Throwable t) {
                Toast.makeText(getApplication(), getString(R.string.startTreatment_fetchParcelError), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Send message to handler
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
