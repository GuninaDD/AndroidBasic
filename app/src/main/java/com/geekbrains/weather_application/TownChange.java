package com.geekbrains.weather_application;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.geekbrains.weather_application.presenter.WthrPresenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TownChange extends Fragment {

    boolean isExistCoatOfArms;
    int currentPosition = 0;

    static final String TOWN_DATA_KEY = "town_data_key";
    static final String WING_DATA_KEY = "wing_data_key";
    static final String PRESSURE_DATA_KEY = "pressure_data_key";
    Button btnSelectTown;
    EditText searchTown;
    final WthrPresenter presenter = WthrPresenter.getInstance();

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
        setTownOnClick();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isExistCoatOfArms = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentCity", 0);
        }
        if (isExistCoatOfArms) {
            showCoatOfArms();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] cities = getResources().getStringArray(R.array.cities);

        for(int i=0; i < cities.length; i++){
            String city = cities[i];
            TextView tv = new TextView(getContext());
            tv.setText(city);
            tv.setTextSize(30);
            layoutView.addView(tv);
            final int fi = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = fi;
                    showCoatOfArms();
                }
            });
        }
    }

    private void showCoatOfArms() {
        if (isExistCoatOfArms) {
            CoatOfWthrValuesFragment detail = (CoatOfWthrValuesFragment)
                    getFragmentManager().findFragmentById(R.id.coat_of_arms);
            if (detail == null || detail.getIndex() != currentPosition) {
                detail = CoatOfWthrValuesFragment.create(currentPosition);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.coat_of_arms, detail);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), CoatOfWthrActivity.class);
            intent.putExtra("index", currentPosition);
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
        btnSelectTown = view.findViewById(R.id.btn_selectTown);
        searchTown = view.findViewById(R.id.et_town);
    }

    private void setTownOnClick() {
        btnSelectTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WthrActivity.class);
                intent.putExtra(TOWN_DATA_KEY, searchTown.getText().toString());
                if (presenter.getWing()) {
                    intent.putExtra(WING_DATA_KEY, getResources().getString(R.string.tv_wthr_wing_info));
                } else {
                    intent.putExtra(WING_DATA_KEY, "");
                }

                if (presenter.getPressure()) {
                    intent.putExtra(PRESSURE_DATA_KEY, getResources().getString(R.string.tv_wthr_pressure_info));
                } else {
                    intent.putExtra(PRESSURE_DATA_KEY, "");
                }

                startActivity(intent);
            }
        });
    }
}
