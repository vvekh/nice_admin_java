package com.example.diplomast2.Adapters;

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

import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.NoteActivity;
import com.example.diplomast2.R;
import com.example.diplomast2.SpecialistActivity;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

public class SpecialistAdapter extends RecyclerView.Adapter<SpecialistAdapter.SpecialistViewHolder> {
    private List<Specialist> specialists;

    public SpecialistAdapter(List<Specialist> specialists){this.specialists = specialists;}

    @NonNull
    @Override
    public SpecialistAdapter.SpecialistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.specialist_sample, parent, false);
        return new SpecialistAdapter.SpecialistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialistAdapter.SpecialistViewHolder holder, int position) {
        Specialist specialist = specialists.get(position);
        holder.SpName.setText(specialist.username + " " + specialist.usersurname);

        if(specialist.graduationid == 1){
            holder.SpEducate1.setText("Высшее образование");
        } else if (specialist.graduationid == 2) {
            holder.SpEducate1.setText("Два высших образования");
        }

        holder.itemView.setOnClickListener(v -> {
            showSpecialist(v.getContext(), specialist);
        });
    }

    private void showSpecialist(Context context, Specialist specialist){
        SharedPreferences prefs = context.getSharedPreferences("MyPrefsS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        Gson gson = new Gson();
        String specialistJson = gson.toJson(specialist);
        editor.putString("specialist", specialistJson);
        editor.apply();

        Intent intent = new Intent(context, SpecialistActivity.class);
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
    public int getItemCount() {return specialists.size();}

    public static class SpecialistViewHolder extends RecyclerView.ViewHolder{
        TextView SpName, SpEducate1;
        public SpecialistViewHolder(@NonNull View itemView){
            super(itemView);
            SpName = itemView.findViewById(R.id.sp_name);
            SpEducate1 = itemView.findViewById(R.id.sp_educate1);
        }
    }
}
