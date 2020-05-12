package com.geekbrains.weatherinworld;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.geekbrains.weatherinworld.receiver.LowBatteryReceiver;
import com.geekbrains.weatherinworld.receiver.NetworkStateReceiver;
import com.geekbrains.weatherinworld.ui.home.HomeFragment;
import com.geekbrains.weatherinworld.ui.settings.SettingsFragment;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private LowBatteryReceiver lowBatteryReceiver = new LowBatteryReceiver();
    private NetworkStateReceiver disconnectNetworkReceiver = new NetworkStateReceiver();
    private DrawerLayout drawer;
    private static final int PERMISSION_REQUEST_CODE = 10;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(lowBatteryReceiver);
        unregisterReceiver(disconnectNetworkReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestHomeFragment();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestHomeFragment() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            System.exit(0);
        registerReceiver(lowBatteryReceiver, new
                IntentFilter(Intent.ACTION_BATTERY_LOW));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolBar();
        initDrawer(toolbar);

        initNotificationChanel();
        IntentFilter disconnectNetwork = new IntentFilter();
        disconnectNetwork.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(disconnectNetworkReceiver, disconnectNetwork);
        initNotificationChanel();
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void initNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void initDrawer(Toolbar toolbar) {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings, R.id.nav_exit)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private Toolbar initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = new Fragment();
        switch (item.getItemId()) {

            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;

            case R.id.nav_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.confirm_select_town)
                        .setPositiveButton(R.string.btn_selectTown,
                                (dialogInterface, i) -> System.exit(0));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;

            default:
                fragment = new HomeFragment();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();

        item.setChecked(true);

        setTitle(item.getTitle());
        drawer.closeDrawers();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Permission
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestHomeFragment();
            }
        }
    }

}
