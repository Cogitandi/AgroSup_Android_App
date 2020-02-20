package com.example.agrosup_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.api.GetData;
import com.example.api.LoginCredentials;
import com.example.api.RetrofitClient;
import com.example.apiModels.Field;
import com.example.apiModels.Operator;
import com.example.apiModels.Parcel;
import com.example.apiModels.User;
import com.example.apiModels.YearPlan;

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

    User user;
    List<YearPlan> yearPlans;
    List<Operator> operators;
    List<Field> fields;
    List<Parcel> parcels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        onClickListeners();
        service = RetrofitClient.getRetrofitInstance().create(GetData.class);

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
        Call<User> call = service.getToken(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    token = "bearer " + response.body().getToken();
                    getUser(token);
                    getOperators(token, 2);

                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorToken), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorUser), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected User getUser(String token) {
        Call<List<User>> getUser = service.getUser(token);
        getUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    for (User item : userList) {
                        user = item;
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorUser), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServer), Toast.LENGTH_SHORT).show();
            }
        });
    return user;
    }

    protected void getYearPlans(String token) {
        Call<List<YearPlan>> getYearPlans = service.getYearPlans(token);

        getYearPlans.enqueue(new Callback<List<YearPlan>>() {
            @Override
            public void onResponse(Call<List<YearPlan>> call, Response<List<YearPlan>> response) {
                if (response.isSuccessful()) {
                    yearPlans = response.body();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorYearPlan), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<YearPlan>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServer), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getOperators(String token, Integer yearPlanId) {
        Call<List<Operator>> getOperators = service.getOperators(token, yearPlanId);
        getOperators.enqueue(new Callback<List<Operator>>() {
            @Override
            public void onResponse(Call<List<Operator>> call, Response<List<Operator>> response) {
                if (response.isSuccessful()) {
                    operators = response.body();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorOperator), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Operator>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServer), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getFields(String token, Integer yearPlanId) {
        Call<List<Field>> getFields = service.getFields(token, yearPlanId);
        getFields.enqueue(new Callback<List<Field>>() {
            @Override
            public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {
                if (response.isSuccessful()) {
                    fields = response.body();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorField), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Field>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServer), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getParcels(String token, Integer yearPlanId) {
        Call<List<Parcel>> getParcels = service.getParcels(token, yearPlanId);
        getParcels.enqueue(new Callback<List<Parcel>>() {
            @Override
            public void onResponse(Call<List<Parcel>> call, Response<List<Parcel>> response) {
                if (response.isSuccessful()) {
                    parcels = response.body();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.sync_errorParcel), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Parcel>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.sync_errorServer), Toast.LENGTH_SHORT).show();
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
