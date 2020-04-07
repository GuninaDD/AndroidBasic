package com.geekbrains.weather_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geekbrains.weather_application.adapter.Week;
import com.geekbrains.weather_application.adapter.WeeklyAdapter;
import com.geekbrains.weather_application.loader.WeatherDataLoader;
import com.geekbrains.weather_application.parcel.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WthrFragment extends Fragment {
    private final Handler handler = new Handler();
    static final String PARCEL = "parcel";
    private String emptyTextView = "";
    private TextView city;
    private TextView windValue;
    private TextView pressureValue;
    private TextView value;
    private TextView date;
    private TextView description;
    private TextView wthrFiveDays;
    private RecyclerView recyclerView;
    private List<Week> weekList;
    private boolean setWind;
    private boolean setPressure;

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
        updateWeatherData(getContext());
    }

    private void updateWeatherData(final Context context) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getParcel().getCityName(), 1);
                final JSONObject jsonObjectFiveDays = WeatherDataLoader.getJSONData(getParcel().getCityName(), 2);

                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder
                                    .setTitle(R.string.place_not_found)
                                    .setPositiveButton(R.string.btn_selectTown, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                                                Objects.requireNonNull(getActivity()).onBackPressed();
                                            }
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                            wthrFiveDays.setText(R.string.tv_onWeek);
                            renderWeatherOnFiveDays(jsonObjectFiveDays);
                            initRecyclerView();
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject wind = jsonObject.getJSONObject("wind");

            setCityName();
            setWthrDscrptn(details);
            setWindValue(wind, setWind);
            setPressureValue(main, setPressure);
            setWthrValue(main);
            setCurrentDate(jsonObject);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void renderWeatherOnFiveDays(JSONObject jsonObject) {
        try {
            for (int i = 0; i < 40; i++) {
                JSONObject list = jsonObject.getJSONArray("list").getJSONObject(i);
                JSONObject details = list.getJSONArray("weather").getJSONObject(0);
                JSONObject main = list.getJSONObject("main");
                JSONObject wind = list.getJSONObject("wind");

                String dayValue = getCurrentDate(list);
                String hourValue = getCurrentHour(list);
                String wthrValue = getWthrValue(main);
                String wthrWind = getWindValue(wind, setWind);
                String wthrPressure = getPressureValue(main, setPressure);
                String wthrDescription = getWthrDscrptn(details);

                weekList.add(new Week(dayValue, hourValue, wthrValue, wthrWind, wthrPressure, wthrDescription));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private String getWthrValue(JSONObject main) throws JSONException {
        return String.format(Locale.getDefault(), "%.2f",
                main.getDouble("temp")) + "\u2103";
    }

    private String getWindValue(JSONObject wind, boolean setWind) throws JSONException {
        if (setWind) {
            return wind.getString("speed") + " m/s";
        } else {
            return emptyTextView;
        }
    }

    private String getPressureValue(JSONObject main, boolean setPressure) throws JSONException {
        if (setPressure) {
            return main.getString("pressure") + " hPa";
        } else {
            return emptyTextView;
        }
    }

    private String getCurrentDate(JSONObject list) throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd", Locale.getDefault());
        return dateFormat.format(new Date(list.getLong("dt") * 1000));
    }

    private String getCurrentHour(JSONObject list) throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return dateFormat.format(new Date(list.getLong("dt") * 1000));
    }

    private String getWthrDscrptn(JSONObject details) throws JSONException {
        return details.getString("description").toUpperCase();
    }

    private void setCityName() {
        city.setText(getParcel().getCityName());
    }

    @SuppressLint("SetTextI18n")
    private void setWthrValue(JSONObject main) throws JSONException {
        value.setText(String.format(Locale.getDefault(), "%.2f",
                main.getDouble("temp")) + "\u2103");
    }

    private void setWindValue(JSONObject wind, boolean setWind) throws JSONException {
        if (setWind) {
            String detailsText = wind.getString("speed") + " m/s";
            windValue.setText(detailsText);
        } else {
            windValue.setText(emptyTextView);
        }
    }

    private void setPressureValue(JSONObject main, boolean setPressure) throws JSONException {
        if (setPressure) {
            String detailsText = main.getString("pressure") + " hPa";
            pressureValue.setText(detailsText);
        } else {
            pressureValue.setText(emptyTextView);
        }
    }

    private void setCurrentDate(JSONObject jsonObject) throws JSONException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
        date.setText(updateOn);
    }

    private void setWthrDscrptn(JSONObject details) throws JSONException {
        String detailsText = details.getString("description").toUpperCase();
        description.setText(detailsText);
    }

    private void initViews(View view) {
        setWind = getParcel().isSetWind();
        setPressure = getParcel().isSetPressure();
        city = view.findViewById(R.id.tv_wthr_location);
        windValue = view.findViewById(R.id.tv_wthr_wind_info);
        pressureValue = view.findViewById(R.id.tv_wthr_pressure_info);
        value = view.findViewById(R.id.tv_wthr_value);
        date = view.findViewById(R.id.tv_current_date);
        description = view.findViewById(R.id.tv_wthr_dscrptn);
        recyclerView = view.findViewById(R.id.rv_wthr_to_week);
        wthrFiveDays = view.findViewById(R.id.tv_onWeek);
        weekList = new ArrayList<>();
        setEmptyViews();
    }

    private void setEmptyViews() {
        city.setText(emptyTextView);
        windValue.setText(emptyTextView);
        pressureValue.setText(emptyTextView);
        value.setText(emptyTextView);
        date.setText(emptyTextView);
        description.setText(emptyTextView);
        wthrFiveDays.setText(emptyTextView);
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