package com.geekbrains.weather_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geekbrains.weather_application.adapter.Week;
import com.geekbrains.weather_application.adapter.WeeklyAdapter;
import com.geekbrains.weather_application.parcel.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WthrFragment extends Fragment {
    static final String PARCEL = "parcel";
    private TextView city;
    private TextView wing;
    private TextView pressure;
    private TextView value;
    private TextView date;
    private TextView description;
    private RecyclerView recyclerView;
    private List<Week> weekList;

    static WthrFragment create(Parcel parcel) {
        WthrFragment f = new WthrFragment();

        Bundle args = new Bundle();
        args.putSerializable(PARCEL, parcel);
        f.setArguments(args);
        return f;
    }

    Parcel getParcel() {
        return (Parcel) Objects.requireNonNull(getArguments()).getSerializable(PARCEL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_wthr, container, false);
        initViews(layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setWthrValue();
        initRecyclerView();
    }

    private void setWthrValue() {
        weekList = new ArrayList<>();
        Parcel parcel = getParcel();
        city.setText((parcel.getCityName()));
        wing.setText((parcel.getWing()));
        pressure.setText((parcel.getPressure()));
        value.setText((parcel.getWthrValue()));
        date.setText((parcel.getCurrentDate()));
        description.setText((parcel.getWthrDescription()));
        weekList.addAll(parcel.getWeekList());
    }

    private void initViews(View view) {

        city = view.findViewById(R.id.tv_wthr_location);
        wing = view.findViewById(R.id.tv_wthr_wing_info);
        pressure = view.findViewById(R.id.tv_wthr_pressure_info);
        value = view.findViewById(R.id.tv_wthr_value);
        date = view.findViewById(R.id.tv_current_date);
        description = view.findViewById(R.id.tv_wthr_dscrptn);
        recyclerView = view.findViewById(R.id.rv_wthr_to_week);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()),
                layoutManager.getOrientation());
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        WeeklyAdapter adapter = new WeeklyAdapter(getContext(), weekList);
        recyclerView.setAdapter(adapter);
    }
}