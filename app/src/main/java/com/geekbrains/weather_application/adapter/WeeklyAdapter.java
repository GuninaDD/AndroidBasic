package com.geekbrains.weather_application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.weather_application.R;

import java.util.List;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.ViewHolder> {
    private Context mCtx;
    private List<Week> weekList;

    public WeeklyAdapter(Context mCtx, List<Week> weekList) {
        this.mCtx = mCtx;
        this.weekList = weekList;
    }

    @NonNull
    @Override
    public WeeklyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mCtx)
                .inflate(R.layout.item_wthr_on_week, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyAdapter.ViewHolder holder, int position) {
        Week week = weekList.get(position);
        holder.textDay().setText(week.getDay());
        holder.textValue().setText(week.getValue());
        holder.textWing().setText(week.getWing());
        holder.textPressure().setText(week.getPressure());
    }

    @Override
    public int getItemCount() {
        return weekList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_day;
        private TextView tv_value;
        private TextView tv_wing;
        private TextView tv_pressure;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_wthr_onWeek_day);
            tv_value = itemView.findViewById(R.id.tv_wthr_onWeek_value);
            tv_wing = itemView.findViewById(R.id.tv_wthr_onWeek_wing);
            tv_pressure = itemView.findViewById(R.id.tv_wthr_onWeek_pressure);
        }

        TextView textDay() {
            return tv_day;
        }

        TextView textValue() {
            return tv_value;
        }

        TextView textWing() {
            return tv_wing;
        }

        TextView textPressure() {
            return tv_pressure;
        }
    }
}
