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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SpecialistActivity extends AppCompatActivity {
    TextView LoginView, InfoView, GradView, Grad2View, IsPoint, TimelineView, PriceView;
    RecyclerView PointView;
    Button BlockBtn;
    APIinterface api;
    SharedPreferences prefs; SharedPreferences.Editor editor;
    Gson gson;
    Specialist specialist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_specialist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoginView = findViewById(R.id.login_view);
        InfoView = findViewById(R.id.info_view);
        GradView = findViewById(R.id.grad_view);
        Grad2View = findViewById(R.id.grad2_view);
        IsPoint = findViewById(R.id.ispoint);
        PointView = findViewById(R.id.point_view);
        TimelineView = findViewById(R.id.timeline_view);
        PriceView = findViewById(R.id.price_view);
        BlockBtn = findViewById(R.id.block_btn);

        api = APIclient.start().create(APIinterface.class);
        prefs = getSharedPreferences("MyPrefsS", MODE_PRIVATE);
        editor = prefs.edit();

        gson = new Gson();
        String specialistJson = prefs.getString("specialist", "");
        specialist = gson.fromJson(specialistJson, Specialist.class);

        new Thread(() ->{
            Call<List<Point>> call = api.getSpecialistPoints(specialist.id);
            call.enqueue(new Callback<List<Point>>() {
                @Override
                public void onResponse(Call<List<Point>> call, Response<List<Point>> response) {
                    //Создание списка поинтов с помощью адаптера
                    PointView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    List<Point> points = response.body();
                    PointAdapter adapter = new PointAdapter(points);
                    PointView.setAdapter(adapter);
                }
                @Override
                public void onFailure(Call<List<Point>> call, Throwable t) {Log.e("ERROR", t.getMessage());}
            });
            Call<List<Timeline>> call1 = api.getAllTimelines();
            call1.enqueue(new Callback<List<Timeline>>() {
                @Override
                public void onResponse(Call<List<Timeline>> call, Response<List<Timeline>> response) {
                    List<Timeline> lines = response.body();
                    Timeline line = lines.get(specialist.timelineid - 1);
                    TimelineView.setText("Часовой пояс: " + line.timelinename);
                }
                @Override
                public void onFailure(Call<List<Timeline>> call, Throwable t) {Log.e("ERROR", t.getMessage());}
            });

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthdate;
            try {
                birthdate = sdf.parse(specialist.birthdate);
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
                if (specialist.graduationid == 1){
                    GradView.setText("Образование: полное высшее");
                }else if (specialist.graduationid == 2){
                    GradView.setText("Образование: два полных высших");
                }
                if ("1".equals(specialist.graduatuon2)){
                    Grad2View.setText("Дополнительное образование: есть");
                }else {
                    Grad2View.setText("Дополнительное образование: нет");
                }
                LoginView.setText(specialist.login);

                PriceView.setText("Стоимость сеанса: " + specialist.price + "руб.");

                InfoView.setText(specialist.username + " " + specialist.usersurname + ", " + FinalAge + AgeSuffix);
            });
        }).start();
    }

    public void BlockOnClick(View view){
        Call<Void> call = api.blockSpecialist(specialist.id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(), "Специалист заблокирован!", Toast.LENGTH_SHORT).show();
                closeActivity();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });
    }

    public void DeleteOnClick(View view){
        Call<Void> call = api.blockSpecialist(specialist.id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(), "Специалист заблокирован!", Toast.LENGTH_SHORT).show();
                closeActivity();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });
    }

    private void closeActivity() {
        Intent intent = new Intent(SpecialistActivity.this, SpecialistListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }
}