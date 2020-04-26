package com.geekbrains.weatherinworld;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WthrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coat_of_wthr);

        if (savedInstanceState == null) {
            WthrFragment details = new WthrFragment();
            details.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, details).commit();
        }
    }
}
