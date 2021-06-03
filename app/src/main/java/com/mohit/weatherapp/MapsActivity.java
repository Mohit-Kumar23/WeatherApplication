package com.mohit.weatherapp;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String OPEN_WEATHER_API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY;
    private GoogleMap mMap;
    ExtendedFloatingActionButton extend_fab;
    FloatingActionButton fab_temp, fab_cloud, fab_wind, fab_pressure, fab_prec;
    TextView tv_temp, tv_wind, tv_cloud, tv_pressure, tv_prec;
    Boolean isAllFabVisible = false;

    static String dataField;

    TileOverlay tileOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        extend_fab = (ExtendedFloatingActionButton) findViewById(R.id.efab_map);
        fab_cloud = (FloatingActionButton) findViewById(R.id.fab_clouds);
        fab_prec = (FloatingActionButton) findViewById(R.id.fab_precipitation);
        fab_pressure = (FloatingActionButton) findViewById(R.id.fab_pressure);
        fab_temp = (FloatingActionButton) findViewById(R.id.fab_temperature);
        fab_wind = (FloatingActionButton) findViewById(R.id.fab_wind);

        tv_cloud = (TextView) findViewById(R.id.tv_map_clouds);
        tv_prec = (TextView) findViewById(R.id.tv_map_precipitation);
        tv_pressure = (TextView) findViewById(R.id.tv_map_pressure);
        tv_temp = (TextView) findViewById(R.id.tv_map_temperature);
        tv_wind = (TextView) findViewById(R.id.tv_map_wind);

        dataField = "temp_new";
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        extend_fab.shrink();

        extend_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibleFunction();
            }

        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        setUpMap();
    }

    public void setUpMap() {
        TileProvider tileProvider = getTileProvider();
        Log.i("called", "here");
        tileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    private static TileProvider getTileProvider() {
        TileProvider tileApiProvider = new UrlTileProvider(256, 256) {
            @Nullable
            @Override
            public URL getTileUrl(int i, int i1, int i2) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                String timeStamp = sdf.format(new Date());
                Log.i("ts", timeStamp);
                String apiUrl = String.format("https://tile.openweathermap.org/map/%s/%d/%d/%d.png?appid=%s", dataField, i2, i, i1,OPEN_WEATHER_API_KEY);
                try {
                    return new URL(apiUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.i("error", e.toString());
                }
                return null;
            }
        };
        return tileApiProvider;
    }

    public void visibleFunction() {
        if (!isAllFabVisible) {
            extend_fab.extend();
            fab_cloud.setVisibility(View.VISIBLE);
            fab_wind.setVisibility(View.VISIBLE);
            fab_temp.setVisibility(View.VISIBLE);
            fab_pressure.setVisibility(View.VISIBLE);
            fab_prec.setVisibility(View.VISIBLE);

            tv_cloud.setVisibility(View.VISIBLE);
            tv_wind.setVisibility(View.VISIBLE);
            tv_temp.setVisibility(View.VISIBLE);
            tv_pressure.setVisibility(View.VISIBLE);
            tv_prec.setVisibility(View.VISIBLE);

            isAllFabVisible = true;

            fab_cloud.setOnClickListener(this::actionMethod);
            fab_wind.setOnClickListener(this::actionMethod);
            fab_temp.setOnClickListener(this::actionMethod);
            fab_pressure.setOnClickListener(this::actionMethod);
            fab_prec.setOnClickListener(this::actionMethod);
        } else {

            fab_cloud.setVisibility(View.INVISIBLE);
            fab_wind.setVisibility(View.INVISIBLE);
            fab_temp.setVisibility(View.INVISIBLE);
            fab_pressure.setVisibility(View.INVISIBLE);
            fab_prec.setVisibility(View.INVISIBLE);

            tv_cloud.setVisibility(View.INVISIBLE);
            tv_wind.setVisibility(View.INVISIBLE);
            tv_temp.setVisibility(View.INVISIBLE);
            tv_pressure.setVisibility(View.INVISIBLE);
            tv_prec.setVisibility(View.INVISIBLE);

            isAllFabVisible = false;

            extend_fab.shrink();
        }


    }

    private void actionMethod(View view) {

        switch (view.getId()) {
            case R.id.fab_temperature:
                dataField = "temp_new";
                break;
            case R.id.fab_clouds:
                dataField = "clouds_new";
                break;
            case R.id.fab_precipitation:
                dataField = "precipitation_new";
                break;
            case R.id.fab_pressure:
                dataField = "pressure_new";
                break;
            case R.id.fab_wind:
                dataField = "wind_new";
                break;
        }
        extend_fab.shrink();
        visibleFunction();
        isAllFabVisible = false;
        tileOverlay.remove();

        setUpMap();
    }
}

