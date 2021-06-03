package com.mohit.weatherapp;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class CityWeatherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TOMORROW_API_KEY = BuildConfig.TOMORROW_WEATHER_API_KEY;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public String citySearched;
    String cityLatitude, cityLongitude, city;

    public RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    public RequestQueue mRequestQueue;
    String url, temp, minTemp, maxTemp;
    SimpleDateFormat oldFormat, newFormat;
    public ArrayList<model_value> dayTempList;
    Date sunriseT, sunsetT;
    String sunriseTime, sunsetTime;

    public TextView max_temp, min_temp, current_temp, sunrise_time, sunset_time, cityName;
    public ImageView sunsetImg, sunriseImg, maxTempImg, minTempImg;


    public CityWeatherFragment() {
        // Required empty public constructor
    }

    public CityWeatherFragment(String citySearched) {
        this.citySearched = citySearched;
    }

    public static CityWeatherFragment newInstance(String param1, String param2) {
        CityWeatherFragment fragment = new CityWeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        cityName = (TextView) view.findViewById(R.id.tv_cwhf_currentCity);
        max_temp = (TextView) view.findViewById(R.id.tv_cwhf_max_temp);
        min_temp = (TextView) view.findViewById(R.id.tv_cwhf_min_temp);
        current_temp = (TextView) view.findViewById(R.id.tv_cwhf_current_temp);
        sunrise_time = (TextView) view.findViewById(R.id.tv_cwhf_sunriseTime);
        sunset_time = (TextView) view.findViewById(R.id.tv_cwhf_sunsetTime);
        maxTempImg = (ImageView) view.findViewById(R.id.iv_cwhf_max_temp_icon);
        minTempImg = (ImageView) view.findViewById(R.id.iv_cwhf_min_temp_icon);
        sunriseImg = (ImageView) view.findViewById(R.id.iv_cwhf_sunriseImg);
        sunsetImg = (ImageView) view.findViewById(R.id.iv_cwhf_sunsetImg);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dayTempList = new ArrayList<model_value>();

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(citySearched, 2);
            cityLatitude = String.valueOf(addresses.get(0).getLatitude());
            cityLongitude = String.valueOf(addresses.get(0).getLongitude());
            city = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (IndexOutOfBoundsException exception) {
            Toast.makeText(getActivity(), "Sorry But Can't Locate the Required City", Toast.LENGTH_LONG).show();
        }

        Log.i("cityS", cityLatitude + "--" + cityLongitude + "--" + city);
        getRequest();
    }

    private void getRequest() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        url = "https://api.tomorrow.io/v4/timelines?location=" + cityLatitude + "," + cityLongitude + "&fields=temperatureAvg,temperatureMax,temperatureMin,sunriseTime,sunsetTime,weatherCode&timesteps=1d&units=metric&apikey="+TOMORROW_API_KEY;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject dataObj = response.getJSONObject("data");
                            JSONArray timelineArr = dataObj.getJSONArray("timelines");
                            JSONObject timeline_0_Obj = timelineArr.getJSONObject(0);
                            JSONArray intervalArr = timeline_0_Obj.getJSONArray("intervals");
                            JSONObject interval_0_obj = intervalArr.getJSONObject(0);
                            JSONObject values_0 = interval_0_obj.getJSONObject("values");

                            cityName.setText(citySearched);
                            temp = values_0.getString("temperatureAvg");
                            current_temp.setText(temp);
                            minTemp = values_0.getString("temperatureMin");
                            min_temp.setText((minTemp) + "\u2103");
                            maxTemp = values_0.getString("temperatureMax");
                            max_temp.setText((maxTemp) + "\u2103");

                            oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            newFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                            oldFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            newFormat.setTimeZone(TimeZone.getDefault());
                            sunriseT = oldFormat.parse(values_0.getString("sunriseTime"));
                            sunsetT = oldFormat.parse(values_0.getString("sunsetTime"));
                            assert sunriseT != null;
                            sunriseTime = newFormat.format(sunriseT);
                            sunsetTime = newFormat.format(sunsetT);
                            Log.i("sun1", sunriseTime);
                            Log.i("sun", values_0.getString("sunriseTime"));
                            sunrise_time.setText(sunriseTime.substring(sunriseTime.indexOf("-") + 2));
                            sunset_time.setText(sunsetTime.substring(sunsetTime.indexOf("-") + 2));


                            for (int i = 1; i < intervalArr.length(); i++) {
                                JSONObject objectInner = intervalArr.getJSONObject(i);
                                JSONObject iterativeValuesObj = objectInner.getJSONObject("values");

                                String dayAvgTemp = iterativeValuesObj.getString("temperatureAvg");
                                String dayWeatherCode = iterativeValuesObj.getString("weatherCode");
                                Date day = oldFormat.parse(objectInner.getString("startTime"));
                                String dayValue = newFormat.format(day);

                                dayTempList.add(new model_value(dayAvgTemp + "\u2103", dayValue.substring(0, 5), dayWeatherCode));

                                Log.i("val", dayValue.substring(0, 5));
                            }

                            mAdapter = new RecyclerViewAdapter(getActivity(), dayTempList);
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error Occured" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(objectRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() == null)
            return;
        else {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if(keyEvent.getAction()==KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK)
                    {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ma,new searchCity_fragment(0)).commit();
                        return true;
                    }

                    return false;
                }
            });
        }
    }
}
