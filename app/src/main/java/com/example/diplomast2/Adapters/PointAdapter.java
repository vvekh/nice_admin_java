package com.example.diplomast2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diplomast2.DTO.Point;
import com.example.diplomast2.R;

import java.util.List;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.PointViewHolder>{
    private List<Point> points;

    public PointAdapter(List<Point> points){this.points = points;}

    @NonNull
    @Override
    public PointAdapter.PointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.point_sample, parent, false);
        return new PointAdapter.PointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointAdapter.PointViewHolder holder, int position) {
        Point point = points.get(position);
        holder.PointName.setText(point.pointname);
    }

    @Override
    public int getItemCount() {return points.size();}

    public static class PointViewHolder extends RecyclerView.ViewHolder{
        TextView PointName;
        public PointViewHolder(@NonNull View itemView){
            super(itemView);
            PointName = itemView.findViewById(R.id.point_name_text_view);
        }
    }
}
