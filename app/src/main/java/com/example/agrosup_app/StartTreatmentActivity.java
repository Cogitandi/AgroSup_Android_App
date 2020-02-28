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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartTreatmentActivity extends AppCompatActivity {

    TextView tv;
    Button startb;
    Button stopb;
    GetData service;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    Handler handler;


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void onClickListeners() {
        startb.setOnClickListener(v -> {
            startLocationUpdates();
        });
        stopb.setOnClickListener(v -> {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        });
    }

    private void initialize() {
        tv = findViewById(R.id.test);
        startb = findViewById(R.id.startbtn);
        stopb = findViewById(R.id.stopbtn);
        service = RetrofitClient.getRetrofitTransInstance().create(GetData.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        onClickListeners();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Log.d("lokalizacja", "start");
                for (Location location : locationResult.getLocations()) {
                    Log.d("lokalizacja", "petla");
                    convertCordinates(location.getLongitude(), location.getLatitude());
                    tv.setText("precyzja:" + location.getAccuracy());
                    // Update UI with location data
                    // ...
                }
            }

            ;
        };

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                messageAction(msg);
            }
        };


    }

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

    private void messageAction(Message msg) {
        // conversion done send to fetch information about parcel
        if (msg.what == 1) {
            TransformationApi transformationApi = (TransformationApi) msg.obj;
            getParcelEwApi(transformationApi);

        }
        //get parcel ewi object
        if (msg.what == 2) {
            Layer layer = (Layer) msg.obj;
            for (Map.Entry<String, String> entry : layer.getMap().entrySet()) {
                Log.d(entry.getKey(), entry.getValue());
            }
        }

    }

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
