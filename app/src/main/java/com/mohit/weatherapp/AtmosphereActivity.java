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

import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AtmosphereActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Fragment selectedFragment, currentAtm_frag, search_frag;
    String currLat, currLong, currCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmosphere);

        chipNavigationBar = (ChipNavigationBar) findViewById(R.id.chip_bnv_atm);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_atm);
        navigationView = (NavigationView) findViewById(R.id.navigationView_atm);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, GravityCompat.END);
        drawerLayout.closeDrawer(GravityCompat.END, true);

        Intent intent = getIntent();
        currLat = intent.getStringExtra("lat");
        currLong = intent.getStringExtra("long");
        currCity = intent.getStringExtra("city");


        chipNavigationBar.setItemSelected(R.id.home, true);
        currentAtm_frag = new CurrentAtmosphereHomeFragment(currLat, currLong, currCity);
        search_frag = new searchCity_fragment(1);

        selectedFragment = currentAtm_frag;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_atm, selectedFragment).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home:
                        selectedFragment = new CurrentAtmosphereHomeFragment(currLat, currLong, currCity);
                        break;

                    case R.id.search:
                        selectedFragment = new searchCity_fragment(1);
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_atm, selectedFragment).commit();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.currWeather:
                        Intent intent1 = new Intent(AtmosphereActivity.this, MainActivity.class);
                        intent1.putExtra("currentLatitude", currLat);
                        intent1.putExtra("currentLongitude", currLong);
                        intent1.putExtra("currentCity", currCity);
                        startActivity(intent1);
                        break;
                    case R.id.map_menu:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        break;

                    case R.id.atmosphere:
                    case R.id.closeDrawer:
                        drawerLayout.closeDrawer(GravityCompat.END);
                        chipNavigationBar.setElevation(30f);
                        Log.i("sf", String.valueOf(selectedFragment.getClass()));
                        if (String.valueOf(selectedFragment.getClass()).equals("class com.mohit.weatherapp.CurrentAtmosphereHomeFragment"))
                            chipNavigationBar.setItemSelected(R.id.home, true);
                        else
                            chipNavigationBar.setItemSelected(R.id.search, true);
                        break;
                }
                return false;
            }
        });

    }


}