package com.geekbrains.weather_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CoatOfWthrValuesFragment extends Fragment {

    public static CoatOfWthrValuesFragment create(int index) {
        CoatOfWthrValuesFragment f = new CoatOfWthrValuesFragment();    // создание

        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getIndex() {
        int index = getArguments().getInt("index", 0);
        return index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView coatOfWthr = new TextView(getContext());
        coatOfWthr.setTextSize(100);
        coatOfWthr.setText(R.string.tv_wthr_value);
        return coatOfWthr;
    }
}
