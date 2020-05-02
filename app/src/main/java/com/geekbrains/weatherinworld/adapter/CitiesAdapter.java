package com.geekbrains.weatherinworld.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.weatherinworld.R;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder>{
    private OnCityClickListener onCityClickListener;
    private List<City> displayedList;
    class CityViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        CityViewHolder(View view){
            super(view);
            cityName = view.findViewById(R.id.tv_cities_list_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    City city = displayedList.get(getLayoutPosition());
                    onCityClickListener.onCityClick(city, v);
                }
            });
        }
    }
    public CitiesAdapter(List<City> displayedList, OnCityClickListener onCityClickListener){
        this.onCityClickListener = onCityClickListener;
        this.displayedList = displayedList;
    }

    public interface OnCityClickListener{
        void onCityClick(City city, View v);
    }


    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, final int position)    {
        // create a layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cities_list, parent, false);

        return new  CityViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        City city = displayedList.get(position);
        holder.cityName.setText(city.getCityName());
    }
    @Override
    public int getItemCount() {
        return displayedList.size();
    }
}
