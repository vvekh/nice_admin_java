package com.example.diplomast2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplomast2.Adapters.AdminAdapter;
import com.example.diplomast2.Adapters.ApplicationAdapter;
import com.example.diplomast2.DTO.Admin;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class AdminListActivity extends AppCompatActivity {
    RecyclerView AdminView;
    Button NewAdminBtn;
    APIinterface api;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AdminView = findViewById(R.id.admin_view);
        NewAdminBtn = findViewById(R.id.new_admin_btn);
        api = APIclient.start().create(APIinterface.class);

        AdminView.setLayoutManager(new LinearLayoutManager(this));

        loadPage();
    }

    private void loadPage(){
        Call<List<Admin>> call = api.getAllAdmins();
        call.enqueue(new Callback<List<Admin>>() {
            @Override
            public void onResponse(Call<List<Admin>> call, Response<List<Admin>> response) {
                List<Admin> admins = response.body();
                AdminAdapter adapter = new AdminAdapter(admins);
                AdminView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<Admin>> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });
    }

    public void AdminOnClick(View view){
        Intent intent = new Intent(getApplicationContext(), NewAdminActivity.class);
        startActivity(intent);

        // Получение Activity из контекста
        Activity activity = null;
        if (getApplicationContext() instanceof Activity) {
            activity = (Activity) getApplicationContext();
        } else if (getApplicationContext() instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) getApplicationContext()).getBaseContext();
            if (baseContext instanceof Activity) {
                activity = (Activity) baseContext;
            }
        }

        // Закрытие Activity, если она была получена
        if (activity != null) {
            activity.finish();
        }
    }
}