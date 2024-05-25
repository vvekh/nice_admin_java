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

import com.example.diplomast2.ApplicationActivity;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.R;
import com.google.gson.Gson;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private List<Specialist> applications;

    public ApplicationAdapter(List<Specialist> applications){
        this.applications = applications;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.application_sample, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ApplicationViewHolder holder, int position) {
        Specialist application = applications.get(position);
        holder.SpName.setText(application.username + " " + application.usersurname);

        holder.itemView.setOnClickListener(v -> {
            showApplication(v.getContext(), application);
        });
    }

    private void showApplication(Context context, Specialist application){
        SharedPreferences prefs = context.getSharedPreferences("MyPrefsA", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        Gson gson = new Gson();
        String applicationJson = gson.toJson(application);
        editor.putString("application", applicationJson);
        editor.apply();

        Intent intent = new Intent(context, ApplicationActivity.class);
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
    public int getItemCount() {return applications.size();}

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder{
        TextView SpName;
        public ApplicationViewHolder(@NonNull View itemView){
            super(itemView);
            SpName = itemView.findViewById(R.id.sp_name);
        }
    }
}
