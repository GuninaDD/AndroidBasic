package com.geekbrains.weatherinworld;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.weatherinworld.adapter.Week;
import com.geekbrains.weatherinworld.adapter.WeeklyAdapter;
import com.geekbrains.weatherinworld.helper.DBHelper;
import com.geekbrains.weatherinworld.parcel.Parcel;
import com.geekbrains.weatherinworld.render.RenderWthr;
import com.geekbrains.weatherinworld.rest.OpenWeatherRepo;
import com.geekbrains.weatherinworld.rest.wthrentities.WthrRequestRestModel;
import com.geekbrains.weatherinworld.rest.wthrfivedaysentities.WthrFiveDaysRequestRestModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WthrFragment extends Fragment {
    private static final String OPEN_WEATHER_API_KEY = "63883755a16707de5b23bc3fc0179a19";
    public static final String PARCEL = "parcel";
    private TextView city;
    private TextView windValue;
    private TextView pressureValue;
    private TextView value;
    private TextView date;
    private TextView description;
    private TextView wthrFiveDays;
    private TextView wthrIcon;
    private RecyclerView recyclerView;
    private List<Week> weekList;
    private boolean setWind;
    private boolean setPressure;
    private ProgressBar loadingBar;
    private ImageView ivBg;
    private SQLiteDatabase database;
    private static final String APP_PREFERENCES = "LastCityLoader";
    private static final String APP_PREFERENCES_CITY = "City";
    private SharedPreferences lastCity;

    private Parcel getParcel() {
        return (Parcel) requireArguments().getSerializable(PARCEL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_wthr, container, false);
        initViews(layout);
        initFonts();
        lastCity = requireContext().getSharedPreferences
                (APP_PREFERENCES, Context.MODE_PRIVATE);
        return layout;
    }

    private void initFonts() {
        Typeface weatherFont = Typeface.createFromAsset(requireContext().getAssets(), "fonts/weather.ttf");
        wthrIcon.setTypeface(weatherFont);
    }

    private void loadImgWithPicasso(int imgUrl) {
        Picasso.get().
                load(imgUrl).
                fit().
                into(ivBg);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        updateWeatherData(getContext());

    }

    private void updateWeatherData(final Context context) {
        final RenderWthr render = new RenderWthr(setWind, setPressure);
        OpenWeatherRepo.getSingleton().getAPI().loadWeather(getParcel().getCityName(),
                OPEN_WEATHER_API_KEY, "metric").
                enqueue(new Callback<WthrRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WthrRequestRestModel> call,
                                           @NonNull Response<WthrRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            render.renderWeather(response.body());
                            loadWthr(render);
                            loadFiveDaysWthr(render);
                            loadingBar.setVisibility(ProgressBar.INVISIBLE);
                            initHistory(getParcel().getCityName());
                            Toast.makeText(context, "Press back to change city", Toast.LENGTH_LONG).show();
                            saveLastCity(getParcel().getCityName());
                        } else {
                            callAlertDialog(context);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WthrRequestRestModel> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.network_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveLastCity(String cityName) {
        SharedPreferences.Editor editor = lastCity.edit();
        editor.putString(APP_PREFERENCES_CITY, cityName);
        editor.apply();
    }

    private void initHistory(String city) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_NAME, city);
            database.insert(DBHelper.TABLE_HISTORY, null, contentValues);
    }

    private void loadWthr(RenderWthr render) {
        wthrFiveDays.setText(R.string.tv_onWeek);
        city.setText(getParcel().getCityName());
        windValue.setText(render.getCurrentWthrWind());
        pressureValue.setText(render.getCurrentWthrPressure());
        value.setText(render.getCurrentWthrValue());
        date.setText(render.getCurrentWthrDate());
        description.setText(render.getCurrentWthrDscrptn());
        setWthrIcon(render.getWthrIcon());
    }

    private void setWthrIcon(int id) {
        String icon = "";
        if (id == 800) {
            icon = getString(R.string.weather_sunny); //weather clear
            loadImgWithPicasso(R.drawable.bg_clear);
        } else {
            id = id / 100;
            switch (id) {
                case 2: {
                    icon = getString(R.string.weather_thunder); //weather thunder
                    loadImgWithPicasso(R.drawable.bg_thunderstorm);
                    break;
                }
                case 3: {
                    icon = getString(R.string.weather_drizzle); //weather drizzle
                    loadImgWithPicasso(R.drawable.bg_drizzle);
                    break;
                }
                case 5: {
                    icon = getString(R.string.weather_rainy); //weather rainy
                    loadImgWithPicasso(R.drawable.bg_rain);
                    break;
                }
                case 6: {
                    icon = getString(R.string.weather_snowy); //weather snowy
                    loadImgWithPicasso(R.drawable.bg_snow);
                    break;
                }
                case 7: {
                    icon = getString(R.string.weather_foggy); //weather foggy
                    loadImgWithPicasso(R.drawable.bg_fog);
                    break;
                }
                case 8: {
                    icon = getString(R.string.weather_cloudy); //weather cloudy
                    loadImgWithPicasso(R.drawable.bg_fog);
                    break;
                }
            }
        }
        wthrIcon.setText(icon);
    }

    private void loadFiveDaysWthr(final RenderWthr render) {
        OpenWeatherRepo.getSingleton().getAPI().loadFiveDaysWeather(getParcel().getCityName(),
                OPEN_WEATHER_API_KEY, "metric").
                enqueue(new Callback<WthrFiveDaysRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WthrFiveDaysRequestRestModel> call,
                                           @NonNull Response<WthrFiveDaysRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            weekList = render.renderWeatherOnFiveDays(response.body());
                            initRecyclerView();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.five_days_wthr_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WthrFiveDaysRequestRestModel> call,
                                          @NonNull Throwable t) {
                    }
                });
    }

    private void callAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.place_not_found)
                .setPositiveButton(R.string.btn_selectTown, (dialog, which) -> {
                    if (!(getResources().getConfiguration().
                            orientation == Configuration.
                            ORIENTATION_LANDSCAPE)) {
                        requireActivity().
                                onBackPressed();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void initViews(View view) {
        DBHelper dbHelper = new DBHelper(getContext());
        database = dbHelper.getWritableDatabase();
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
        wthrIcon = view.findViewById(R.id.weather_icon);
        ivBg = view.findViewById(R.id.iv_bg);

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
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(),
                layoutManager.getOrientation());
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        WeeklyAdapter adapter = new WeeklyAdapter(getContext(), weekList);
        recyclerView.setAdapter(adapter);
    }

}