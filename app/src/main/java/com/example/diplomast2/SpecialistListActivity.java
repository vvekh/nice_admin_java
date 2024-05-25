package com.example.diplomast2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplomast2.Adapters.SpecialistAdapter;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialistListActivity extends AppCompatActivity {
    RecyclerView SpecialistView;
    APIinterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_specialist_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SpecialistView = findViewById(R.id.specialist_view);
        api = APIclient.start().create(APIinterface.class);

        SpecialistView.setLayoutManager(new LinearLayoutManager(this));

        loadPage();
    }

    private void loadPage(){
        Call<List<Specialist>> call = api.getSpecialists1();
        call.enqueue(new Callback<List<Specialist>>() {
            @Override
            public void onResponse(Call<List<Specialist>> call, Response<List<Specialist>> response) {
                List<Specialist> specialists = response.body();
                SpecialistAdapter adapter = new SpecialistAdapter(specialists);
                SpecialistView.setAdapter(adapter);
                Log.d("LOGGG", response.message() + " " + response.body());
            }
            @Override
            public void onFailure(Call<List<Specialist>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }
}