package com.example.diplomast2;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diplomast2.Adapters.PointAdapter;
import com.example.diplomast2.DTO.Point;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.DTO.Timeline;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationActivity extends AppCompatActivity {
    TextView LoginView, InfoView, SexView, EmailView, PhoneView, PriceView, TimelineView;
    Button RejectBtn, ApproveBtn;
    APIinterface api;
    SharedPreferences prefs; SharedPreferences.Editor editor;
    Gson gson;
    Specialist application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_application);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        api = APIclient.start().create(APIinterface.class);
        prefs = getSharedPreferences("MyPrefsA", MODE_PRIVATE);
        editor = prefs.edit();

        gson = new Gson();
        String applicationJson = prefs.getString("application", "");
        application = gson.fromJson(applicationJson, Specialist.class);

        LoginView = findViewById(R.id.login_view);
        InfoView = findViewById(R.id.info_view);
        SexView = findViewById(R.id.sex_view);
        EmailView = findViewById(R.id.email_view);
        PhoneView = findViewById(R.id.phone_view);
        PriceView = findViewById(R.id.price_view);
        TimelineView = findViewById(R.id.timeline_view);

        RejectBtn = findViewById(R.id.reject_btn);
        ApproveBtn = findViewById(R.id.approve_btn);

        loadPage(application);
    }

    private void loadPage(Specialist application){
        //Сделать InfoView (имя, фамилия, возраст) и TimelineView (часовой пояс)

        new Thread(() ->{
            Call<List<Timeline>> call = api.getAllTimelines();
            call.enqueue(new Callback<List<Timeline>>() {
                @Override
                public void onResponse(Call<List<Timeline>> call, Response<List<Timeline>> response) {
                    List<Timeline> lines = response.body();
                    Timeline line = lines.get(application.timelineid - 1);
                    TimelineView.setText("Часовой пояс: " + line.timelinename);
                }
                @Override
                public void onFailure(Call<List<Timeline>> call, Throwable t) {Log.e("ERROR", t.getMessage());}
            });

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthdate;
            try {
                birthdate = sdf.parse(application.birthdate);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            Calendar CurrentDate = Calendar.getInstance();
            int CurrentYear = CurrentDate.get(Calendar.YEAR);
            int CurrentMonth = CurrentDate.get(Calendar.MONTH);
            int CurrentDay = CurrentDate.get(Calendar.DAY_OF_MONTH);
            Calendar Birth = Calendar.getInstance();
            Birth.setTime(birthdate);
            int BirthYear = Birth.get(Calendar.YEAR);
            int BirthMonth = Birth.get(Calendar.MONTH);
            int BirthDay = Birth.get(Calendar.DAY_OF_MONTH);
            int Age = CurrentYear - BirthYear;
            if (CurrentMonth < BirthMonth || (CurrentMonth == BirthMonth && CurrentDay < BirthDay)) {
                Age--;
            }
            String AgeSuffix;
            if (Age % 10 == 1 && Age % 100 != 11) {
                AgeSuffix = " год";
            } else if ((Age % 10 >= 2 && Age % 10 <= 4) && !(Age % 100 >= 12 && Age % 100 <= 14)) {
                AgeSuffix = " года";
            } else {
                AgeSuffix = " лет";
            }
            int FinalAge = Age;

            runOnUiThread(() ->{
                if (application.sexid == 1){
                    SexView.setText("Пол: женский");
                }else if (application.sexid == 2){
                    SexView.setText("Пол: мужской");
                }
                LoginView.setText(application.login);
                EmailView.setText("Email: " + application.email);
                PhoneView.setText("Номер телефона: "+ application.phone);
                PriceView.setText("Цена сессии: " + application.price + " руб.");

                InfoView.setText(application.username + " " + application.usersurname + ", " + FinalAge + AgeSuffix);
            });
        }).start();
    }

    public void ApproveOnClickA(View view){
        Call<Void> call = api.approveSpecialistProfile(application.id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Заявка одобрена", Toast.LENGTH_SHORT).show();
                    closeActivity();
                    Log.d("SUCCESS", response.message());
                }else {
                    Toast.makeText(getApplicationContext(), "Не хватает данных об образовании!", Toast.LENGTH_LONG).show();
                    Log.d("FAIL", response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void RejectOnClickA(View view){
        Call<Void> call = api.rejectSpecialistProfile(application.id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Заявка отклонена", Toast.LENGTH_SHORT).show();
                    closeActivity();
                    Log.d("SUCCESS", response.message());
                }else {
                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                    Log.d("FAIL", response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void closeActivity() {
        Intent intent = new Intent(ApplicationActivity.this, ApplicationListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }
}