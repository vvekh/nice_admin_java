package com.example.diplomast2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplomast2.Adapters.ApplicationAdapter;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationListActivity extends AppCompatActivity {
    RecyclerView ApplicationView;
    TextView ErrorView;
    APIinterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_application_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ApplicationView = findViewById(R.id.application_view);
        ErrorView = findViewById(R.id.error_view);
        api = APIclient.start().create(APIinterface.class);

        ApplicationView.setLayoutManager(new LinearLayoutManager(this));

        loadPage();
    }

    private void loadPage(){
        Call<List<Specialist>> call = api.getSpecialistsApplications();
        call.enqueue(new Callback<List<Specialist>>() {
            @Override
            public void onResponse(Call<List<Specialist>> call, Response<List<Specialist>> response) {
                if (response.isSuccessful()){
                    ErrorView.setVisibility(View.GONE);
                    ApplicationView.setVisibility(View.VISIBLE);
                    List<Specialist> applications = response.body();
                    ApplicationAdapter adapter = new ApplicationAdapter(applications);
                    ApplicationView.setAdapter(adapter);
                }else{
                    ErrorView.setVisibility(View.VISIBLE);
                    ApplicationView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Specialist>> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });
    }
}