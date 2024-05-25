package com.example.diplomast2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.diplomast2.DTO.Admin;
import com.example.diplomast2.Retrofit.APIclient;
import com.example.diplomast2.Retrofit.APIinterface;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {
    TextView LoginView, InfoView, RoleView;
    ImageView FirstBtn, SecondBtn, ThirdBtn, FourthBtn, FifthBtn;
    Button ExitBtn;
    APIinterface api;
    SharedPreferences prefs; SharedPreferences.Editor editor;
    Gson gson;
    Admin user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        api = APIclient.start().create(APIinterface.class);
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = prefs.edit();

        gson = new Gson();
        String userJson = prefs.getString("user", "");
        user = gson.fromJson(userJson, Admin.class);

        LoginView = findViewById(R.id.login_view);
        InfoView = findViewById(R.id.info_view);
        RoleView = findViewById(R.id.role_view);
        FirstBtn = findViewById(R.id.first_btn);
        SecondBtn = findViewById(R.id.second_btn);
        ThirdBtn = findViewById(R.id.third_btn);
        FourthBtn = findViewById(R.id.fourth_btn);
        FifthBtn = findViewById(R.id.fifth_btn);
        ExitBtn = findViewById(R.id.exit_btn);

        if (user != null){
            loadPage(user);
        }
    }

    public void PanelOnClick(View view){
        String name = getResources().getResourceEntryName(view.getId());
        switch (name){
            case "first_btn":
                Intent intent1 = new Intent(getApplicationContext(), ApplicationListActivity.class);
                startActivity(intent1);
                break;
            case "second_btn":
                Intent intent2 = new Intent(getApplicationContext(), NoteListActivity.class);
                startActivity(intent2);
                break;
            case "third_btn":
                Intent intent3 = new Intent(getApplicationContext(), SpecialistListActivity.class);
                startActivity(intent3);
                break;
            case "fourth_btn":
                Intent intent4 = new Intent(getApplicationContext(), AdminListActivity.class);
                startActivity(intent4);
                break;
            case "fifth_btn":
                Intent intent5 = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent5);
                break;
        }
    }

    public void ExitOnClick(View view){
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadPage(Admin admin){
        LoginView.setText("@" + admin.login);
        InfoView.setText(admin.username + " " + admin.usersurname);
        if ("0".equals(admin.issenior)){
            RoleView.setText("Администратор");
            FourthBtn.setVisibility(View.GONE);
        }else if ("1".equals(admin.issenior)){
            RoleView.setText("Старший администратор");
        }
    }
}