package com.example.agrosup_app;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.api.GetData;
import com.example.api.RetrofitClient;
import com.example.apiModels.FeatureCollection;
import com.example.apiModels.Layer;
import com.example.apiModels.TransformationApi;
import com.example.bluetooth.BluetoothDataModel;
import com.example.bluetooth.ConnectThread;
import com.example.bluetooth.ConnectedThread;
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
import java.util.Set;

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
    TextView temperatureTV;
    TextView totalCombustionTV;
    TextView velocityTV;
    TextView currentCombustionTV;
    TextView machineNameTV;
    TextView cultivatedAreaTV;
    TextView machineWidthTV;
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

    // BT
    ConnectThread connectThread;
    ConnectedThread connectedThread;
    private BroadcastReceiver receiver;
    boolean receiverRegistered = false;
    BluetoothDevice bluetoothDevice;
    BluetoothAdapter bluetoothAdapter;
    String moduleName = "HC-05";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_treatment);
        initialize();
        bluetoothConnection();


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        if (receiverRegistered) {
            unregisterReceiver(receiver);
        }
        if (connectThread != null) {
            connectThread.cancel();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), getString(R.string.startTreatment_bluetoothNotAccepted), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    // Bluetoth connection
    private void bluetoothConnection() {
        checkBluetoothAvailable();
        if (!(isModuleInPairedDevices())) {
            // Register for broadcasts when a device is discovered.
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);
            receiverRegistered = true;
            Log.d("BT", "szukam");
        } else {
            sendMessage(4, new String("found"));
        }
    }

    // Check bluetooth
    private void checkBluetoothAvailable() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(getApplication(), getString(R.string.startTreatment_deviceNotSupportBluetooth), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Enable bluetooth
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    // check in paired devices
    private boolean isModuleInPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName.equals(moduleName)) {
                    bluetoothDevice = device;
                    return true;
                }
            }
        }
        return false;
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
        temperatureTV = findViewById(R.id.streatment_temperatureTV);
        totalCombustionTV = findViewById(R.id.streatment_totalCombustionTV);
        velocityTV = findViewById(R.id.streatment_velocityTV);
        currentCombustionTV = findViewById(R.id.streatment_currentCombustionTV);
        machineNameTV = findViewById(R.id.streatment_machineNameTV);
        cultivatedAreaTV = findViewById(R.id.streatment_cultivatedAreaTV);
        machineWidthTV = findViewById(R.id.streatment_machineWidthTV);
        saveBTN = findViewById(R.id.streatment_saveBTN);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        service = RetrofitClient.getRetrofitTransInstance().create(GetData.class);
        db = AppDatabase.getInstance(getApplicationContext());


        foundFields = new ArrayList<>();


        tv = findViewById(R.id.test);
        startb = findViewById(R.id.startbtn);
        stopb = findViewById(R.id.stopbtn);

        onClickListeners();

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

        // Create a BroadcastReceiver for ACTION_FOUND.
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    Log.d("BT nazwa", deviceName + "x");
                    if (deviceName.equals(moduleName)) {
                        bluetoothDevice = device;
                        if (receiverRegistered) {
                            unregisterReceiver(receiver);
                        }
                        sendMessage(4, new String("found"));
                    }

                }
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
            if (transformationApi != null) getParcelEwApi(transformationApi);

        }
        //get parcel ewi object FROM getParcelEwApi()
        if (msg.what == 2) {
            Layer layer = (Layer) msg.obj;
            Log.d("obj", layer.toString());
            findFieldByParcelNumber(layer.getMap().get("Numer działki"));
//            for (Map.Entry<String, String> entry : layer.getMap().entrySet()) {
//                Log.d(entry.getKey(), entry.getValue());
//            }
        }
        //get field FROM findFieldByParcelNumber()
        if (msg.what == 3) {
            Field field = (Field) msg.obj;
            if (isNotInArray(foundFields, field)) {
                foundFields.add(field);
                Button fieldButton = createButton(field.getName());
                fieldButton.setOnClickListener(v -> {
                    Log.d("wiadomosc", "przed");
                    Machine machine = new Machine("Pług", 165);
                    connectedThread.write(new String("C"+machine.getWidth()+",D"+user.getAmountImpuls()+",00000").getBytes());
                    Log.d("wiadomosc", "C"+machine.getWidth()+",D"+user.getAmountImpuls()+",00000");
                    user.setSelectedMachine(machine);
                    // remove location
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                    choosenField = field;
                    updateUI();
                });
                fieldListLayout.addView(fieldButton);
            }

        }
        // found bluetooth device
        if (msg.what == 4) {
            Log.d("BT", bluetoothDevice.getAddress().toString());
            connectThread = new ConnectThread(bluetoothDevice, bluetoothAdapter, handler);
            connectThread.start();

        }
        // connection ok
        if (msg.what == 5) {
            connectedThread = (ConnectedThread) msg.obj;
            fetchDB();
            startLocationUpdates();
        }

        if (msg.what == 6) {

            BluetoothDataModel bluetoothDataModel = (BluetoothDataModel) msg.obj;
//            Log.d("temp", bluetoothDataModel.getTemperature() + "st");
//            Log.d("temp", bluetoothDataModel.getHumidity() + "st");
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            clockTV.setText(currentTime);
            temperatureTV.setText(bluetoothDataModel.getTemperature() + ""+(char)176+"C");
            totalCombustionTV.setText(bluetoothDataModel.getTotalCombustion() + " l");
            velocityTV.setText(bluetoothDataModel.getVelocity()+" km/h");
            currentCombustionTV.setText(bluetoothDataModel.getCurrentCombustion() + " l/ha");
            cultivatedAreaTV.setText(bluetoothDataModel.getArea() + " ha");


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
        fieldListLayout.setVisibility(View.GONE);
        dataTL.setVisibility(View.VISIBLE);
        setFieldAreaTV(choosenField);

        operatorTV.setText(choosenOperator.toString());
        fieldTV.setText(choosenField.getName());
        machineNameTV.setText(user.getSelectedMachine().getName());
        machineWidthTV.setText(user.getSelectedMachine().getWidthM()+" m");
    }

    // update fieldAreaTV
    private void setFieldAreaTV(Field field) {
        Thread thread = new Thread(() -> {
            try {
                float area = db.fieldDao().fieldsWithParcels(field.getId()).getFieldArea();
                runOnUiThread(() -> {
                    fieldAreaTV.setText(area + "");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        thread.start();
    }

    // set user and choosen operator
    private void fetchDB() {
        Thread thread = new Thread(() -> {
            user = db.userDao().findLogged(true);
            choosenOperator = db.operatorDao().findOperatorById(user.getSelectedOperatorId());
            sendMessage(3,new Field(0,"brak", user.getSelectedYearPlanId(),"brak"));
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
