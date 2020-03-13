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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.FieldFinder;
import com.example.bluetooth.BluetoothDataModel;
import com.example.bluetooth.ConnectThread;
import com.example.bluetooth.ConnectedThread;
import com.example.database.AppDatabase;
import com.example.database.FieldWithParcels;
import com.example.entities.Field;
import com.example.entities.Machine;
import com.example.entities.Operator;
import com.example.entities.Treatment;
import com.example.entities.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
    AppDatabase db;
    User user;
    Operator choosenOperator;

    Treatment treatment;
    List<Field> foundFields;
    Field choosenField;

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
        if (isPermissionGranted()) {
            initialize();
            bluetoothConnection();
        }


//        boolean permissionAccessCoarseLocationApproved = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//
//        if (permissionAccessCoarseLocationApproved) {
//            // App has permission to access location in the foreground. Start your
//            // foreground service that has a foreground service type of "location".
//        } else {
//            // Make a request for foreground-only location access.
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
//

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int item : grantResults) {
            if (item == PackageManager.PERMISSION_DENIED) finish();
        }
        recreate();

    }

    /* ********************** Bluetooth ********************** */

    private boolean isPermissionGranted() {
//        HashMap<Integer, String> permissions = new HashMap<>();
//        permissions.put(1, Manifest.permission.ACCESS_COARSE_LOCATION);
//        permissions.put(2, Manifest.permission.ACCESS_FINE_LOCATION);
//        permissions.put(3, Manifest.permission.BLUETOOTH);
//
//        for (Map.Entry<Integer, String> entry : permissions.entrySet()) {
//            if (ContextCompat.checkSelfPermission(this, entry.getValue())!= PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{entry.getValue()}, entry.getKey());
//            }

        int permissions_code = 42;
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH};

        for (String item : permissions) {
            if (ContextCompat.checkSelfPermission(this, item) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, permissions_code);
                return false;
            }
        }

        return true;
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

    // Bluetoth connection
    private void bluetoothConnection() {
        checkBluetoothAvailable();
        if (!(isModuleInPairedDevices())) {
            // Register for broadcasts when a device is discovered.
            Log.d("BT","SZUKAM");
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);
            receiverRegistered = true;
            bluetoothAdapter.startDiscovery();
        } else {
            // found in paired
            sendMessage(4,"");
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
                Log.d("BT","device "+deviceName);

                if (deviceName.equals(moduleName)) {
                    bluetoothDevice = device;
                    Log.d("BT","devicef"+deviceName);
                    return true;
                }
            }
        }
        return false;
    }

    // update UI with data from bluetooth
    private void updateUIBluetooth(BluetoothDataModel bluetoothDataModel) {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        clockTV.setText(currentTime);
        temperatureTV.setText(bluetoothDataModel.getTemperature() + "" + (char) 176 + "C");
        totalCombustionTV.setText(bluetoothDataModel.getTotalCombustion() + " l");
        velocityTV.setText(bluetoothDataModel.getVelocity() + " km/h");
        currentCombustionTV.setText(bluetoothDataModel.getCurrentCombustion() + " l/h");
        cultivatedAreaTV.setText(bluetoothDataModel.getArea() + " ha");
    }

    /* ********************** Other ********************** */

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
        saveBTN.setOnClickListener(v -> {
            new Thread(() -> {
                treatment.setEndDate();
                db.treatmentDao().insertAll(treatment);
                finish();
            }).start();
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
        db = AppDatabase.getInstance(getApplicationContext());


        foundFields = new ArrayList<>();

        onClickListeners();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    new FieldFinder(location, getApplicationContext(), handler, user);
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
                    Log.d("BT", "receiver:"+ deviceName);
                    if (deviceName.equals(moduleName)) {
                        bluetoothDevice = device;
                        sendMessage(4,"");
                        if (receiverRegistered) {
                            receiverRegistered = false;
                            unregisterReceiver(receiver);
                        }
                    }

                }
            }
        };


    }

    // Action depends on code received by handler
    private void messageAction(Message msg) {
        //get matched field from FieldFinder
        if (msg.what == 1) {
            Field field = (Field) msg.obj;
            if (isNotInArray(foundFields, field)) {
                foundFields.add(field);
                Button fieldButton = createButton(field.getName());
                fieldButton.setOnClickListener(v -> {
                    Machine machine = new Machine("Pług", 165);
                    user.setSelectedMachine(machine);
                    Log.d("BT","przed wyslaniem");
                    connectedThread.write(new String("C" + machine.getWidth() + ",D" + user.getAmountImpuls() + ",00000").getBytes());
                    Log.d("BT","C" + machine.getWidth() + ",D" + user.getAmountImpuls() + ",00000");
                    treatment = new Treatment(user, machine, field);
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
            Log.d("BT","lacze");
            connectThread = new ConnectThread(bluetoothDevice, bluetoothAdapter, handler);
            connectThread.start();
        }

        // bluetooth connection ok
        if (msg.what == 5) {
            Log.d("BT","polaczylem");
            connectedThread = (ConnectedThread) msg.obj;
            fetchDB();
            startLocationUpdates();
        }

        // data from bluetooth
        if (msg.what == 6) {
            Log.d("BT","doastalem");
            BluetoothDataModel bluetoothDataModel = (BluetoothDataModel) msg.obj;
            Log.d("BT",bluetoothDataModel.getHumidity()+"x");
            updateUIBluetooth(bluetoothDataModel);
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
        machineWidthTV.setText(user.getSelectedMachine().getWidthM() + " m");
    }

    // update fieldAreaTV
    private void setFieldAreaTV(Field field) {
        Thread thread = new Thread(() -> {
            try {
                FieldWithParcels fwp = db.fieldDao().fieldsWithParcels(field.getId());
                if (fwp != null) {
                    float area = fwp.getFieldArea();
                    runOnUiThread(() -> {
                        fieldAreaTV.setText(area + "");
                    });
                } else {
                    fieldAreaTV.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        thread.start();
    }

    // set user and choosen operator
    private void fetchDB() {
        new Thread(() -> {
            user = db.userDao().findLogged(true);
            choosenOperator = db.operatorDao().findOperatorById(user.getSelectedOperatorId());
            sendMessage(1, new Field(0, "brak", user.getSelectedYearPlanId(), "brak"));
        }).start();
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
