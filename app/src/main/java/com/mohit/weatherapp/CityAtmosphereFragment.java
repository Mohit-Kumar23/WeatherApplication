package com.mohit.weatherapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityAtmosphereFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityAtmosphereFragment extends Fragment {

    public static final String TOMORROW_API_KEY = BuildConfig.TOMORROW_WEATHER_API_KEY;

    public TextView humidityTxt,windSpeedTxt,windDirTxt,precipitationTxt,pressureTxt,visibilityTxt,aqiTxt,cityTxt;
    public String humidity,precipitation,windSpeed,windDir,pressure,visibility;
    Integer aqi;
    public String url,currentLatitude,currentLongitude,aqiStmtVal;

    public RequestQueue requestQueue;

    public String citySearch;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CityAtmosphereFragment() {
        // Required empty public constructor
    }

    public CityAtmosphereFragment(String citySearch)
    {
        this.citySearch = citySearch;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CityAtmosphereFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CityAtmosphereFragment newInstance(String param1, String param2) {
        CityAtmosphereFragment fragment = new CityAtmosphereFragment();
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
        View view = inflater.inflate(R.layout.fragment_current_atmosphere_home, container, false);
        humidityTxt = (TextView)view.findViewById(R.id.tv_catmf_humidityVal);
        visibilityTxt = (TextView)view.findViewById(R.id.tv_catmf_visibilityVal);
        windSpeedTxt = (TextView)view.findViewById(R.id.tv_catmf_windSpeedVal);
        windDirTxt = (TextView)view.findViewById(R.id.tv_catmf_windDirectionVal);
        precipitationTxt = (TextView)view.findViewById(R.id.tv_catmf_precipitationProbVal);
        pressureTxt = (TextView)view.findViewById(R.id.tv_catmf_pressureSeaLevelVal);
        aqiTxt = (TextView)view.findViewById(R.id.tv_catmf_aqiVal);
        cityTxt = (TextView)view.findViewById(R.id.tv_catmf_cityTxt);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cityTxt.setText(citySearch);

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(citySearch,2);
            currentLatitude = String.valueOf(addresses.get(0).getLatitude());
            currentLongitude = String.valueOf(addresses.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException exception) {
            Toast.makeText(getActivity(), "Sorry But Can't Locate the Required City", Toast.LENGTH_LONG).show();
        }
        Log.i("atmCity",currentLatitude+"--"+currentLongitude+"--"+citySearch);

        getRequest();
    }
    public void getRequest()
    {
        requestQueue = Volley.newRequestQueue(getActivity());
        url = "https://api.tomorrow.io/v4/timelines?location="+currentLatitude+","+currentLongitude+"&fields=humidity,windSpeed,windDirection,pressureSeaLevel,precipitationProbability,visibility,epaIndex&timesteps=current&units=metric&apikey="+TOMORROW_API_KEY;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject dataObj = response.getJSONObject("data");
                    JSONArray timelineArr = dataObj.getJSONArray("timelines");
                    JSONObject timeline_0_obj = timelineArr.getJSONObject(0);
                    JSONArray intervalArr = timeline_0_obj.getJSONArray("intervals");
                    JSONObject interval_0_obj = intervalArr.getJSONObject(0);
                    JSONObject values = interval_0_obj.getJSONObject("values");

                    humidity = values.getString("humidity");
                    windSpeed = values.getString("windSpeed");
                    windDir = values.getString("windDirection");
                    pressure = values.getString("pressureSeaLevel");
                    precipitation = values.getString("precipitationProbability");
                    visibility = values.getString("visibility");
                    aqi = Integer.parseInt(values.getString("epaIndex"));

                    humidityTxt.setText(humidity+" %");
                    windSpeedTxt.setText(windSpeed+" m/s");
                    windDirTxt.setText(windDir+" \u00B0");
                    pressureTxt.setText(pressure+" hPa");
                    precipitationTxt.setText(precipitation+" %");
                    visibilityTxt.setText(visibility+" KM");

                    aqiStmtVal =  aqiStmt();

                    aqiTxt.setText(String.valueOf(aqi)+" "+aqiStmtVal);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(objectRequest);
    }

    private String aqiStmt() {
        if(aqi >= 0 && aqi<=50 )
            return "(Good)";
        else if(aqi>=51 && aqi<=100)
            return "(Moderate)";
        else if(aqi>=101 && aqi<=150)
            return "(Unhealthy for Sensitive Groups)";
        else if(aqi>=151 && aqi<=200)
            return "(Unhealthy)";
        else if(aqi>=201 && aqi<=300)
            return "(Very Unhealthy)";
        else if(aqi>=301)
            return "(Hazardous)";
        else
            return "(Unknown)";
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
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ma,new searchCity_fragment(1)).commit();
                        return true;
                    }

                    return false;
                }
            });
        }
    }
}