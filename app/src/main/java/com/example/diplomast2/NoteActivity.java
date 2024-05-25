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

import com.example.diplomast2.DTO.Note;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteActivity extends AppCompatActivity {
    TextView NoteTitle, NoteContent;
    Button RejectBtn, ApproveBtn;
    APIinterface api;
    SharedPreferences prefs; SharedPreferences.Editor editor;
    Gson gson;
    Note note;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        api = APIclient.start().create(APIinterface.class);
        prefs = getSharedPreferences("MyPrefsN", MODE_PRIVATE);
        editor = prefs.edit();

        gson = new Gson();
        String noteJson = prefs.getString("note", "");
        note = gson.fromJson(noteJson, Note.class);

        NoteTitle = findViewById(R.id.note_title);
        NoteContent = findViewById(R.id.note_content);

        RejectBtn = findViewById(R.id.reject_btn);
        ApproveBtn = findViewById(R.id.approve_btn);

        loadPage(note);
    }

    private void loadPage(Note note){
        NoteTitle.setText(note.title);
        NoteContent.setText(note.content);
    }

    public void ApproveOnClick(View view){
        Call<Void> call = api.approveNote(note.id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Заметка одобрена!", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), NoteListActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });
    }
    public void RejectOnClick(View view){
        Call<Void> call = api.rejectNote(note.id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Заметка отклонена!", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), NoteListActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {Log.e("ERROR", t.getMessage());}
        });
    }

    private void closeActivity() {
        Intent intent = new Intent(NoteActivity.this, NoteListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }
}