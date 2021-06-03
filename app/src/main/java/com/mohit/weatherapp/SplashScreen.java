package com.mohit.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.security.Permission;
import java.security.acl.Group;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    public double currentLatitude, currentLongitude;
    public String city;
    Handler mHandler;
    Geocoder geocoder;
    Boolean permissionsStatus = false;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mHandler = new Handler();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SplashScreen.this);

        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
           ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            getCurrentLocation();
        }
        else
        {
            ActivityCompat.requestPermissions(SplashScreen.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0 && (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED)
        {
            getCurrentLocation();
        }
        else
        {
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation()
    {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if(location!=null)
                        {
                            geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                            List<Address> addressList = null;
                            try {
                                addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            city = addressList.get(0).getLocality();
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();
                            Log.i("lt",String.valueOf(currentLatitude));
                            Log.i("lg",String.valueOf(currentLongitude));
                            Log.i("city",city);

                            transitionFun();
                        }
                        else
                        {
                            LocationRequest locationRequest = new LocationRequest()
                                                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                                                .setInterval(10000)
                                                                .setFastestInterval(1000)
                                                                .setNumUpdates(1);

                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    Location location1 = locationResult.getLocations().get(0);
                                    geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                                    List<Address> addressList = null;
                                    try {
                                        addressList = geocoder.getFromLocation(location1.getLatitude(),location1.getLongitude(),1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    city = addressList.get(0).getLocality();

                                    currentLatitude=location.getLatitude();
                                    currentLongitude=location.getLongitude();
                                    Log.i("lt1",String.valueOf(currentLatitude));
                                    Log.i("lg1",String.valueOf(currentLongitude));
                                    Log.i("city1",city);
                                    transitionFun();
                                }
                            };
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                        }
                }
            });
        }
        else
        {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void transitionFun()
    {
        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
        intent.putExtra("currentLatitude",String.valueOf(currentLatitude));
        intent.putExtra("currentLongitude",String.valueOf(currentLongitude));
        intent.putExtra("currentCity",city);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        },3000);
    }
}