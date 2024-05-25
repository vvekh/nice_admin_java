package com.example.diplomast2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.diplomast2.DTO.Admin;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText LoginBox, PasswordBox;
    Button EnterBtn;
    private APIinterface api;
    SharedPreferences prefs; SharedPreferences.Editor editor;
    Gson gson;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoginBox = findViewById(R.id.login_box);
        PasswordBox = findViewById(R.id.password_box);
        EnterBtn = findViewById(R.id.enter_btn);

        api = APIclient.start().create(APIinterface.class);
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = prefs.edit();

        loadSavedCredentials();
    }

    public void EnterOnClick (View view){
        String login, password;
        login = LoginBox.getText().toString();
        password = PasswordBox.getText().toString();
        Authorization(login, password);
    }

    private void Authorization(String login, String password){
        Call<Admin> call = api.getAdminAccount(login, password);
        call.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                Admin admin = response.body();
                if (admin != null){
                    gson = new Gson();
                    String userJson = gson.toJson(admin);
                    editor.putString("login", admin.login);
                    editor.putString("password", admin.password);
                    editor.putString("user", userJson);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    Log.d("SUCCESS", "УСПЕШНАЯ РЕГИСТРАЦИЯ");
                }else {
                    Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                    Log.d("FAIL", response.message());
                }
            }
            @Override
            public void onFailure(Call<Admin> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });
    }

    private void loadSavedCredentials(){
        String savedLogin = prefs.getString("login", "");
        String savedPassword = prefs.getString("password", "");
        LoginBox.setText(savedLogin);
        PasswordBox.setText(savedPassword);
        if (savedLogin != ""){
            Authorization(savedLogin, savedPassword);
        }
    }
}