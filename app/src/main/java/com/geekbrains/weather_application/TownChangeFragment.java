package com.geekbrains.weather_application;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.geekbrains.weather_application.adapter.Week;
import com.geekbrains.weather_application.generator.WeatherGenerator;
import com.geekbrains.weather_application.parcel.Parcel;
import com.geekbrains.weather_application.presenter.WthrPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.geekbrains.weather_application.WthrFragment.PARCEL;

public class TownChangeFragment extends Fragment {

    private boolean isExistCoatOfWthr;
    private Parcel currentParcel;
    private WeatherGenerator wthrGen = new WeatherGenerator();
    private TextInputEditText townSearch;
    private Button townSelect;
    private Pattern checkTown = Pattern.compile("^[A-z][a-z]{2,}$");

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
        List<Week> emptyList = new ArrayList<>();
        isExistCoatOfWthr = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentParcel = (Parcel) savedInstanceState.getSerializable("CurrentCity");
        } else {
            currentParcel = new Parcel("", "", "", "", "", "", emptyList);
        }
        if (isExistCoatOfWthr) {
            showCoatOfWthr(currentParcel);
        }
    }

    private List<Week> initWeekList() {
        List<Week> weekList = new ArrayList<>();
        WeatherGenerator wthrGen = new WeatherGenerator();
        for (String re : wthrGen.getWeeklyLog()) {
            weekList.add(new Week(re, wthrGen.getWeatherValue(),
                    wthrGen.getWing(presenter.getWing()),
                    wthrGen.getPressure(presenter.getPressure())));
        }
        return weekList;
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
                    setTown(cityName, v);
                }
            });
        }
    }


    private void setTown(String cityName, View view) {
        callSnackBar(view, cityName);
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
        townSelect = view.findViewById(R.id.btn_select_town);
        townSearch = view.findViewById(R.id.ti_et_town);
        initViewsListeners();
    }

    private void initViewsListeners() {
        townSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectClick(townSearch);
            }
        });
        townSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onSelectClick(v);
                    return true;
                }
                return false;
            }
        });
    }

    private void callSnackBar(View view, final String cityName) {
        Snackbar.make(view, getResources().getString(R.string.confirm_select_town), Snackbar.LENGTH_LONG)
                .setDuration(3000)
                .setBackgroundTint(getResources().getColor(R.color.colorHighlight))
                .setTextColor(getResources().getColor(R.color.colorTextDark))
                .setAction(getResources().getString(R.string.confirm_action), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentParcel = new Parcel
                                (cityName, wthrGen.getWeatherValue(), wthrGen.getWing(presenter.getWing()),
                                        wthrGen.getPressure(presenter.getPressure()), wthrGen.getDate(),
                                        wthrGen.getDscrptn(getResources().getStringArray(R.array.descriptions)),
                                        initWeekList());
                        showCoatOfWthr(currentParcel);
                        townSearch.setText("");
                        hideError(townSearch);
                    }
                }).setActionTextColor(getResources().getColor(R.color.colorTextDark))
                .show();
    }

    private void onSelectClick(View view) {
        TextView tv = (TextView) view;
        if (validate(tv, checkTown, getResources().
                getString(R.string.til_town_error_msg))) {
            setTown(String.valueOf(townSearch.getText()), view);
        }
    }


    private boolean validate(TextView tv, Pattern checkTown, String msg) {
        String value = tv.getText().toString();
        if (checkTown.matcher(value).matches()) {
            hideError(tv);
            return true;
        } else {
            showError(tv, msg);
            return false;
        }
    }

    private void showError(TextView tv, String msg) {
        tv.setError(msg);
    }

    private void hideError(TextView tv) {
        tv.setError(null);
    }
}
