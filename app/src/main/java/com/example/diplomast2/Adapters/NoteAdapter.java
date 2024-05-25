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
import com.example.diplomast2.DTO.Note;
import com.example.diplomast2.DTO.Specialist;
import com.example.diplomast2.NoteActivity;
import com.example.diplomast2.R;
import com.google.gson.Gson;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;

    public NoteAdapter(List<Note> notes){ this.notes = notes; }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_sample, parent, false);
        return new NoteAdapter.NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.NoteTitle.setText(note.title);
        holder.NoteContent.setText(note.content);

        holder.itemView.setOnClickListener(v -> {
           showNote(v.getContext(), note);
        });
    }

    private void showNote(Context context, Note note){
        SharedPreferences prefs = context.getSharedPreferences("MyPrefsN", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        Gson gson = new Gson();
        String noteJson = gson.toJson(note);
        editor.putString("note", noteJson);
        editor.apply();

        Intent intent = new Intent(context, NoteActivity.class);
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
    public int getItemCount() {return notes.size();}

    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView NoteTitle, NoteContent;
        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            NoteTitle = itemView.findViewById(R.id.note_title);
            NoteContent = itemView.findViewById(R.id.note_content);
        }
    }
}
