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
import com.example.api.Login;
import com.example.api.RetrofitClient;
import com.example.api.User;

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
            getUser(token);
        });
    }

    protected void getToken(String email, String password) {
        Login login = new Login("a@b.pl", "ziomek");
        Call<User> call = service.getToken(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    token = "bearer " + response.body().getToken();

                } else {
                    Toast.makeText(LoginActivity.this, "nieprawidłowe dane", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "nie można połączyć się z serwerem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getUser(String token) {
        Call<User> getUser = service.getUser(token);
        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if(user instanceof User) Toast.makeText(LoginActivity.this, user.getEmail()+"", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "wystąpił błąd", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "nie można połączyć się z serwerem", Toast.LENGTH_SHORT).show();
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
