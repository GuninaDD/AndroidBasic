package com.geekbrains.weather_application;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WthrActivity extends AppCompatActivity {

    private TextView tvTown;
    private TextView tvWing;
    private TextView tvPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wthr);
        initViews();
        setWthrValue();
    }

    private void setWthrValue() {
        tvTown.setText(getIntent().getStringExtra(TownChange.TOWN_DATA_KEY));
        tvWing.setText(getIntent().getStringExtra(TownChange.WING_DATA_KEY));
        tvPressure.setText(getIntent().getStringExtra(TownChange.PRESSURE_DATA_KEY));
    }

    private void initViews() {
        tvTown = findViewById(R.id.tv_wthr_location);
        tvWing = findViewById(R.id.tv_wthr_wing_info);
        tvPressure = findViewById(R.id.tv_wthr_pressure_info);
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