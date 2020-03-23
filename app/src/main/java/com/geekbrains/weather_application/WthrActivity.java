package com.geekbrains.weather_application;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class WthrActivity extends AppCompatActivity {

    private TextView tvTown;
    private TextView tvWing;
    private TextView tvPressure;
    private static final String TAG = "wthrLogs";

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
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

}