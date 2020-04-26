package com.geekbrains.weatherinworld.ui.home;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.weatherinworld.R;
import com.geekbrains.weatherinworld.WthrActivity;
import com.geekbrains.weatherinworld.adapter.CitiesAdapter;
import com.geekbrains.weatherinworld.adapter.City;
import com.geekbrains.weatherinworld.helper.DBHelper;
import com.geekbrains.weatherinworld.parcel.Parcel;
import com.geekbrains.weatherinworld.presenter.WthrPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.geekbrains.weatherinworld.WthrFragment.PARCEL;

public class HomeFragment extends Fragment {
    private Parcel currentParcel;
    private TextInputEditText townSearch;
    private Button townSelect;
    private Button viewHistory;
    private Pattern checkTown = Pattern.compile("^[A-z][a-z]{2,}$");
    private final WthrPresenter presenter = WthrPresenter.getInstance();
    private RecyclerView recyclerView;
    private CitiesAdapter adapter;
    private List<City> cities;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_town_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initCities();
        saveTownState(view);
    }

    private List<City> loadCities() {
        cities = new ArrayList<>();
        String[] names = getResources().getStringArray(R.array.cities);
        for (String name : names) {
            cities.add(new City(name));
        }
        return cities;
    }

    private void initCities() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CitiesAdapter.OnCityClickListener onCityClickListener = new CitiesAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(City city, View v) {
                setTown(city.getCityName());
            }
        };
        adapter = new CitiesAdapter(loadCities(), onCityClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            currentParcel = (Parcel) savedInstanceState.getSerializable("CurrentCity");
        } else {
            currentParcel = new Parcel();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("CurrentCity", currentParcel);
        super.onSaveInstanceState(outState);
    }


    private void setTown(String cityName) {
        initHistory(cityName);
        callSnackBar(cityName);
    }

    private void showCoatOfWthr(Parcel parcel) {
        Intent intent = new Intent();
        intent.setClass(Objects.requireNonNull(getActivity()), WthrActivity.class);
        intent.putExtra(PARCEL, parcel);
        startActivity(intent);
    }

    private void initHistory(String city) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, city);
        database.insert(DBHelper.TABLE_HISTORY, null, contentValues);
    }

    private void saveTownState(View view) {
        final CheckBox cbWind = view.findViewById(R.id.cb_wind);
        final CheckBox cbPressure = view.findViewById(R.id.cb_pressure);

        cbWind.setChecked(presenter.getWind());
        cbPressure.setChecked(presenter.getPressure());

        cbWind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setWind(cbWind);
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
        dbHelper = new DBHelper(getContext());
        database = dbHelper.getWritableDatabase();
        recyclerView = view.findViewById(R.id.rv_cities);
        townSelect = view.findViewById(R.id.btn_select_town);
        townSearch = view.findViewById(R.id.ti_et_town);
        viewHistory = view.findViewById(R.id.btn_history);
        initViewsListeners();
    }

    private List<String> loadHistoryList() {
        final List<String> historyList = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_HISTORY, null, null,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            do {
                historyList.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyList;
    }

    private String[] initHistoryList(List<String> historyList) {
        final String[] history_items = new String[historyList.size()];
        for (int i = 0; i < historyList.size(); i++) {
            history_items[i] = historyList.get(i);
        }
        return history_items;
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
        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHistory();
            }
        });
    }

    private void loadHistory() {
        final String[] history_items = initHistoryList(loadHistoryList());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.btn_history);
        builder.setItems(history_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setTown(history_items[which]);
            }
        });
        builder.setNegativeButton(R.string.btn_selectTown, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void callSnackBar(final String cityName) {
        Snackbar.make(getView(), getResources().getString(R.string.confirm_select_town), Snackbar.LENGTH_LONG)
                .setDuration(3000)
                .setBackgroundTint(getResources().getColor(R.color.colorHighlight))
                .setTextColor(getResources().getColor(R.color.colorTextDark))
                .setAction(getResources().getString(R.string.confirm_action), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentParcel = new Parcel(cityName, presenter.getWind(), presenter.getPressure());
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
            setTown(String.valueOf(townSearch.getText()));
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