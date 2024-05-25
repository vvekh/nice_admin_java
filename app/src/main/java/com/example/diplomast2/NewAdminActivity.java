package com.example.diplomast2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAdminActivity extends AppCompatActivity {
    EditText NameBox, SurnameBox, LoginBox, PasswordBox;
    CheckBox RoleCheck;
    Button SaveBtn;
    APIinterface api;
    Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NameBox = findViewById(R.id.name_box);
        SurnameBox = findViewById(R.id.surname_box);
        LoginBox = findViewById(R.id.login_box);
        PasswordBox = findViewById(R.id.password_box);
        RoleCheck = findViewById(R.id.role_check);
        SaveBtn = findViewById(R.id.save_btn);
        api = APIclient.start().create(APIinterface.class);
    }

    public void SaveOnClick(View view){

        if (NameBox.getText().length() < 2 || SurnameBox.getText().length() < 2){
            Toast.makeText(getApplicationContext(), "Введите корректные имя и фамилию!", Toast.LENGTH_SHORT).show();
        }else if (LoginBox.getText().length() < 4){
            Toast.makeText(getApplicationContext(), "Слишком короткий логин!", Toast.LENGTH_SHORT).show();
        }else if (PasswordBox.getText().length() < 8){
            Toast.makeText(getApplicationContext(), "Слишком короткий пароль!", Toast.LENGTH_SHORT).show();
        }else {
            Save();
        }
    }

    private void Save(){
        admin = new Admin();
        admin.login = String.valueOf(LoginBox.getText());
        admin.password = String.valueOf(PasswordBox.getText());
        admin.username = String.valueOf(NameBox.getText());
        admin.usersurname = String.valueOf(SurnameBox.getText());
        if(RoleCheck.isChecked()){
            admin.issenior = "1";
        }else{
            admin.issenior = "0";
        }

        Call<Void> call = api.insertAdmin(admin);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("SUCCESS", response.message());
                }else {
                    Log.d("FAIL", response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });

        closeActivity();
    }

    private void closeActivity() {
        Intent intent = new Intent(NewAdminActivity.this, AdminListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }
}