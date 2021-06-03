package com.mohit.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    Fragment selectedFragment;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button drawer;

    String latitude, longitude, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            city = savedInstanceState.getString("City");
            latitude = savedInstanceState.getString("Latitude");
            longitude = savedInstanceState.getString("Longitude");
            Log.i("ma", latitude + "-" + longitude + "-" + city);
        }
        setContentView(R.layout.activity_main);

        chipNavigationBar = (ChipNavigationBar) findViewById(R.id.chip_bnv_ma);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_ma);
        navigationView = (NavigationView) findViewById(R.id.navigationView_ma);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, GravityCompat.END);
        drawerLayout.closeDrawer(GravityCompat.END, true);

        chipNavigationBar.setItemSelected(R.id.home, true);

        Intent intent = getIntent();
        latitude = intent.getStringExtra("currentLatitude");
        longitude = intent.getStringExtra("currentLongitude");
        city = intent.getStringExtra("currentCity");
        
        Log.i("mav", latitude + longitude + city);

        Fragment currentWeatherFrag = new currentWeather_home_fragment(latitude, longitude, city);
        selectedFragment = currentWeatherFrag;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ma, selectedFragment).commit();

        Fragment searchFrag = new searchCity_fragment(0);

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
               /* if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                    chipNavigationBar.setItemSelected(R.id.dashboard, false);
                    chipNavigationBar.setElevation(18f);
                } else {
                    chipNavigationBar.setElevation(30f);
                }*/
                switch (i) {
                    case R.id.home:
                        selectedFragment = currentWeatherFrag;
                        Log.i("class1", String.valueOf(selectedFragment.getClass()));

                        break;
                    case R.id.search:
                        selectedFragment = new searchCity_fragment(0);
                        Log.i("class", String.valueOf(selectedFragment.getClass()));
                        break;
                    case R.id.dashboard:
                        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                            drawerLayout.closeDrawer(GravityCompat.END);
                            chipNavigationBar.setItemSelected(R.id.dashboard, false);
                            chipNavigationBar.setElevation(20f);

                        } else {
                            chipNavigationBar.setElevation(18f);
                            drawerLayout.openDrawer(GravityCompat.END);

                        }

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ma, selectedFragment).commit();

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.atmosphere:
                        Intent intent1 = new Intent(getApplicationContext(), AtmosphereActivity.class);
                        intent1.putExtra("lat", latitude);
                        intent1.putExtra("long", longitude);
                        intent1.putExtra("city", city);
                        startActivity(intent1);
                        break;
                    case R.id.map_menu:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        break;
                    case R.id.currWeather:
                    case R.id.closeDrawer:
                        drawerLayout.closeDrawer(GravityCompat.END);
                        chipNavigationBar.setElevation(30f);
                        if (String.valueOf(selectedFragment.getClass()).equals("class com.mohit.weatherapp.currentWeather_home_fragment"))
                            chipNavigationBar.setItemSelected(R.id.home, true);
                        else
                            chipNavigationBar.setItemSelected(R.id.search, true);
                        break;
                }
                return false;
            }
        });


    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Latitude", latitude);
        outState.putString("Longitude", longitude);
        outState.putString("City", city);
    }
}