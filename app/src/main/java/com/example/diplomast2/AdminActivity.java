package com.example.diplomast2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.diplomast2.DTO.Admin;
import com.example.diplomast2.DTO.Note;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.jar.Attributes;

public class AdminActivity extends AppCompatActivity {
    TextView LoginView, PasswordView, NameView, SurnameView, RoleView;
    Button DeleteBtn;
    APIinterface api;
    SharedPreferences prefs; SharedPreferences.Editor editor;
    Gson gson;
    Admin admin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoginView = findViewById(R.id.login_view);
        PasswordView = findViewById(R.id.password_view);
        NameView = findViewById(R.id.name_view);
        SurnameView = findViewById(R.id.surname_view);
        RoleView = findViewById(R.id.role_view);
        DeleteBtn = findViewById(R.id.delete_btn);

        api = APIclient.start().create(APIinterface.class);
        prefs = getSharedPreferences("MyPrefsD", MODE_PRIVATE);
        editor = prefs.edit();

        gson = new Gson();
        String adminJson = prefs.getString("admin", "");
        admin = gson.fromJson(adminJson, Admin.class);

        loadPage();
    }

    public void DeleteOnClick(View view){
        Call<Void> call = api.deleteAdmin(admin.id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Аккаунт удалён", Toast.LENGTH_SHORT).show();
                    closeActivity();
                }else {
                    Log.d("FAIL", response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });
    }

    private void loadPage(){
        if ("0".equals(admin.issenior)){
            RoleView.setText("Администратор");
        }else if ("1".equals(admin.issenior)){
            RoleView.setText("Старший администратор");
        }

        LoginView.setText(admin.login);
        PasswordView.setText("********");
        NameView.setText(admin.username);
        SurnameView.setText(admin.usersurname);
    }

    private void closeActivity() {
        Intent intent = new Intent(AdminActivity.this, AdminListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }
}