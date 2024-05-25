package com.example.diplomast2.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplomast2.AdminActivity;
import com.example.diplomast2.NewAdminActivity;
import com.example.diplomast2.DTO.Admin;
import com.example.diplomast2.R;
import com.google.gson.Gson;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {
    private List<Admin> admins;

    public AdminAdapter(List<Admin> admins){this.admins = admins;}

    @NonNull
    @Override
    public AdminAdapter.AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.admin_sample, parent, false);
        return new AdminAdapter.AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.AdminViewHolder holder, int position) {
        Admin admin = admins.get(position);
        holder.AdName.setText(admin.username + " " + admin.usersurname);
        if ("0".equals(admin.issenior)){
            holder.AdRole.setText("Администратор");
        }else if ("1".equals(admin.issenior)){
            holder.AdRole.setText("Старший администратор");
        }

        holder.itemView.setOnClickListener(v ->{
            showAdmin(v.getContext(), admin);
        });
    }

    private void showAdmin(Context context, Admin admin){
        SharedPreferences prefs = context.getSharedPreferences("MyPrefsD", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        Gson gson = new Gson();
        String adminJson = gson.toJson(admin);
        editor.putString("admin", adminJson);
        editor.apply();

        Intent intent = new Intent(context, AdminActivity.class);
        context.startActivity(intent);

        // Получение Activity из контекста
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        } else if (context instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (baseContext instanceof Activity) {
                activity = (Activity) baseContext;
            }
        }

        // Закрытие Activity, если она была получена
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public int getItemCount() {
        return admins.size();
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder{
        TextView AdName, AdRole;
        public AdminViewHolder(@NonNull View itemView){
            super(itemView);
            AdName = itemView.findViewById(R.id.ad_name);
            AdRole = itemView.findViewById(R.id.ad_role);
        }
    }
}
