package com.geekbrains.weatherinworld;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.weatherinworld.adapter.Week;
import com.geekbrains.weatherinworld.adapter.WeeklyAdapter;
import com.geekbrains.weatherinworld.loader.WeatherDataLoader;
import com.geekbrains.weatherinworld.parcel.Parcel;
import com.geekbrains.weatherinworld.render.RenderWthr;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class WthrFragment extends Fragment {
    private final Handler handler = new Handler();
    public static final String PARCEL = "parcel";
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
    private ProgressBar loadingBar;

    private Parcel getParcel() {
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

                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getParcel().
                        getCityName(), 1);
                final JSONObject jsonObjectFiveDays = WeatherDataLoader.getJSONData(getParcel().
                        getCityName(), 2);
                final RenderWthr render = new RenderWthr(setWind, setPressure);

                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callAlertDialog(context);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            render.renderWeather(jsonObject);
                            wthrFiveDays.setText(R.string.tv_onWeek);
                            city.setText(getParcel().getCityName());
                            windValue.setText(render.getCurrentWthrWind());
                            pressureValue.setText(render.getCurrentWthrPressure());
                            value.setText(render.getCurrentWthrValue());
                            date.setText(render.getCurrentWthrDate());
                            description.setText(render.getCurrentWthrDscrptn());
                            weekList = render.renderWeatherOnFiveDays(jsonObjectFiveDays);
                            initRecyclerView();
                            loadingBar.setVisibility(ProgressBar.INVISIBLE);
                        }
                    });
                }
            }
        }.start();
    }

    private void callAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.place_not_found)
                .setPositiveButton(R.string.btn_selectTown, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!(getResources().getConfiguration().
                                orientation == Configuration.
                                ORIENTATION_LANDSCAPE)) {
                            Objects.requireNonNull(getActivity()).
                                    onBackPressed();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void initViews(View view) {
        loadingBar = view.findViewById(R.id.progressBar);
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
        setEmptyViews();
    }

    private void setEmptyViews() {
        String emptyTextView = "";
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
        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.
                requireNonNull(getContext()),
                layoutManager.getOrientation());
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        WeeklyAdapter adapter = new WeeklyAdapter(getContext(), weekList);
        recyclerView.setAdapter(adapter);
    }
}