package com.geekbrains.weather_application;

import android.content.Intent;
import android.os.Bundle;

import com.geekbrains.weather_application.presenter.WthrPresenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class TownChange extends AppCompatActivity {

    static final String TOWN_DATA_KEY = "town_data_key";
    static final String WING_DATA_KEY = "wing_data_key";
    static final String PRESSURE_DATA_KEY = "pressure_data_key";
    Button btnSelectTown;
    EditText searchTown;
    final WthrPresenter presenter = WthrPresenter.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_change);

        initViews();



        final CheckBox cbWing = findViewById(R.id.cb_wing);
        final CheckBox cbPressure = findViewById(R.id.cb_pressure);

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


        setTownOnClick();
    }

    private void initViews() {
        btnSelectTown = findViewById(R.id.btn_selectTown);
        searchTown = findViewById(R.id.et_town);
    }

    private void setTownOnClick() {
        btnSelectTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TownChange.this, WthrActivity.class);
                intent.putExtra(TOWN_DATA_KEY, searchTown.getText().toString());
                if(presenter.getWing()) {
                    intent.putExtra(WING_DATA_KEY, getResources().getString(R.string.tv_wthr_wing_info));
                } else {
                    intent.putExtra(WING_DATA_KEY, "");
                }

                if(presenter.getPressure()) {
                    intent.putExtra(PRESSURE_DATA_KEY, getResources().getString(R.string.tv_wthr_pressure_info));
                } else {
                    intent.putExtra(PRESSURE_DATA_KEY, "");
                }

                startActivity(intent);
            }
        });
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

}
