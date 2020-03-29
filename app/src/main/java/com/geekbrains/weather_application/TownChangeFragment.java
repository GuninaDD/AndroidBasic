package com.geekbrains.weather_application;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.geekbrains.weather_application.generator.WeatherGenerator;
import com.geekbrains.weather_application.parcel.Parcel;
import com.geekbrains.weather_application.presenter.WthrPresenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import static com.geekbrains.weather_application.WthrFragment.PARCEL;

public class TownChangeFragment extends Fragment {

    private boolean isExistCoatOfWthr;
    private Parcel currentParcel;

    private WeatherGenerator wthrGen = new WeatherGenerator();

    private final WthrPresenter presenter = WthrPresenter.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_town_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initList(view);
        saveTownState(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isExistCoatOfWthr = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentParcel = (Parcel) savedInstanceState.getSerializable("CurrentCity");
        } else {
            currentParcel = new Parcel("", "", "", "", "", "");
        }
        if (isExistCoatOfWthr) {
            showCoatOfWthr(currentParcel);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("CurrentCity", currentParcel);
        super.onSaveInstanceState(outState);
    }

    private void initList(View view) {
        int padding = getResources().getDimensionPixelSize(R.dimen.spacing_xxsmall);
        LinearLayout layoutView = (LinearLayout) view;
        String[] cities = getResources().getStringArray(R.array.cities);

        for (String city : cities) {
            TextView tv = new TextView(getContext());
            tv.setText(city);
            tv.setTextSize(30);
            tv.setPadding(padding, padding, padding, padding);
            layoutView.addView(tv);
            final String cityName = city;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentParcel = new Parcel
                            (cityName, wthrGen.getWeatherValue(), wthrGen.getWing(presenter.getWing()),
                                    wthrGen.getPressure(presenter.getPressure()), wthrGen.getDate(), wthrGen.getDscrptn(getResources().getStringArray(R.array.descriptions)));
                    showCoatOfWthr(currentParcel);
                }
            });
        }
    }

    private void showCoatOfWthr(Parcel parcel) {
        if (isExistCoatOfWthr) {
            assert getFragmentManager() != null;
            WthrFragment detail = (WthrFragment)
                    getFragmentManager().findFragmentById(R.id.coat_of_wthr);
            if (detail == null || !detail.getParcel().getCityName().equals(parcel.getCityName())) {
                detail = WthrFragment.create(parcel);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.coat_of_wthr, detail);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), WthrActivity.class);
            intent.putExtra(PARCEL, parcel);
            startActivity(intent);
        }
    }

    private void saveTownState(View view) {
        final CheckBox cbWing = view.findViewById(R.id.cb_wing);
        final CheckBox cbPressure = view.findViewById(R.id.cb_pressure);

        cbWing.setChecked(presenter.getWing());
        cbPressure.setChecked(presenter.getPressure());

        cbWing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setWing(cbWing);
            }
        });
        cbPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setPressure(cbPressure);
            }
        });
    }

    private void initViews(View view) {
        view.findViewById(R.id.et_town);
    }
}
