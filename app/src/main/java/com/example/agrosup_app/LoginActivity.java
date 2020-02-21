package com.example.agrosup_app;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.api.GetData;
import com.example.api.LoginCredentials;
import com.example.api.RetrofitClient;
import com.example.apiModels.FieldApi;
import com.example.apiModels.OperatorApi;
import com.example.apiModels.ParcelApi;
import com.example.apiModels.UserApi;
import com.example.apiModels.YearPlanApi;
import com.example.database.AppDatabase;
import com.example.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    Button submitBTN;
    private static String token;
    GetData service;

    UserApi userApi;
    List<YearPlanApi> yearPlanApis;
    List<OperatorApi> operatorApis;
    List<FieldApi> fieldApis;
    List<ParcelApi> parcelApis;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        onClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    protected void initialize() {
        emailET = findViewById(R.id.login_emailET);
        passwordET = findViewById(R.id.login_passwordET);
        submitBTN = findViewById(R.id.login_submitBTN);
        //db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "agroSupDB").build();
        db = AppDatabase.getInstance(getApplicationContext());
        service = RetrofitClient.getRetrofitInstance().create(GetData.class);
    }

    protected void onClickListeners() {
        submitBTN.setOnClickListener((View v) -> {
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            getToken("a@b.pl", "ziomek");
        });
    }

    protected void getToken(String email, String password) {
        LoginCredentials login = new LoginCredentials("a@b.pl", "ziomek");
        Call<UserApi> call = service.getToken(login);
        call.enqueue(new Callback<UserApi>() {
            @Override
            public void onResponse(Call<UserApi> call, Response<UserApi> response) {
                if (response.isSuccessful()) {
                    token = "bearer " + response.body().getToken();
                    getUserApi(token);
                    //getOperatorsApi(token, 2);

                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorTokenApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserApi> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorUserApi), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getUserApi(String token) {
        Call<List<UserApi>> getUserApi = service.getUser(token);
        getUserApi.enqueue(new Callback<List<UserApi>>() {
            @Override
            public void onResponse(Call<List<UserApi>> call, Response<List<UserApi>> response) {
                if (response.isSuccessful()) {
                    Log.d("nowy","sensowna");
                    List<UserApi> userApiList = response.body();
                    for (UserApi item : userApiList) {
                        userApi = item;
                        User user = new User(userApi.getId(), userApi.getEmail());
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                db.runInTransaction(() -> {
                                    Log.d("nowy","weszedl");
                                    db.userDao().insertAll(user);
                                    if (db.userDao().getAll().size() != 0)
                                        Log.d("nowy",db.userDao().getAll().get(0).getUid()+"");
                                        //Toast.makeText(LoginActivity.this, "z bazy:" + db.userDao().getAll().get(0).getUid(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
                        thread.start();


                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorUserApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserApi>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getYearPlansApi(String token) {
        Call<List<YearPlanApi>> getYearPlanApis = service.getYearPlans(token);

        getYearPlanApis.enqueue(new Callback<List<YearPlanApi>>() {
            @Override
            public void onResponse(Call<List<YearPlanApi>> call, Response<List<YearPlanApi>> response) {
                if (response.isSuccessful()) {
                    yearPlanApis = response.body();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorYearPlanApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<YearPlanApi>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getOperatorsApi(String token, Integer YearPlanApiId) {
        Call<List<OperatorApi>> getOperatorApis = service.getOperators(token, YearPlanApiId);
        getOperatorApis.enqueue(new Callback<List<OperatorApi>>() {
            @Override
            public void onResponse(Call<List<OperatorApi>> call, Response<List<OperatorApi>> response) {
                if (response.isSuccessful()) {
                    operatorApis = response.body();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorOperatorApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OperatorApi>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getFieldsApi(String token, Integer YearPlanApiId) {
        Call<List<FieldApi>> getFieldApis = service.getFields(token, YearPlanApiId);
        getFieldApis.enqueue(new Callback<List<FieldApi>>() {
            @Override
            public void onResponse(Call<List<FieldApi>> call, Response<List<FieldApi>> response) {
                if (response.isSuccessful()) {
                    fieldApis = response.body();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorFieldApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FieldApi>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getParcelsApi(String token, Integer YearPlanApiId) {
        Call<List<ParcelApi>> getParcelApis = service.getParcels(token, YearPlanApiId);
        getParcelApis.enqueue(new Callback<List<ParcelApi>>() {
            @Override
            public void onResponse(Call<List<ParcelApi>> call, Response<List<ParcelApi>> response) {
                if (response.isSuccessful()) {
                    parcelApis = response.body();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorParcelApi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ParcelApi>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServerApi), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void hideStatusBar() {
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
