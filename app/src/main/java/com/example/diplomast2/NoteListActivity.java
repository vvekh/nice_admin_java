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
import com.example.diplomast2.Adapters.NoteAdapter;
import com.example.diplomast2.DTO.Note;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteListActivity extends AppCompatActivity {
    RecyclerView NoteView;
    TextView ErrorView;
    APIinterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NoteView = findViewById(R.id.note_view);
        ErrorView = findViewById(R.id.error_view);
        api = APIclient.start().create(APIinterface.class);

        NoteView.setLayoutManager(new LinearLayoutManager(this));

        loadPage();
    }

    private void loadPage(){
        Call<List<Note>> call = api.getNotes3();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
               if (response.isSuccessful()){
                   ErrorView.setVisibility(View.GONE);
                   NoteView.setVisibility(View.VISIBLE);
                   List<Note> notes = response.body();
                   NoteAdapter adapter = new NoteAdapter(notes);
                   NoteView.setAdapter(adapter);
                   Log.d("LOGGG", response.message() + " " + response.body());
               }else {
                   ErrorView.setVisibility(View.VISIBLE);
                   NoteView.setVisibility(View.GONE);
                   Log.d("LOGGG", response.message() + " " + response.body());
               }
            }
            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }
}