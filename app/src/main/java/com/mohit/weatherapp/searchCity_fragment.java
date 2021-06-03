package com.mohit.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class searchCity_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public int flag_call;

    public ArrayList<String> cityList, countryList;
    SearchView citySearchView;
    ListView cityListView;
    ArrayAdapter<String> arrayAdapter;
    Fragment requireFrag;


    public searchCity_fragment() {
        // Required empty public constructor
    }

    public searchCity_fragment(int flag_call) {
        this.flag_call = flag_call;
    }

    public static searchCity_fragment newInstance(String param1, String param2) {
        searchCity_fragment fragment = new searchCity_fragment();
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
        View view = inflater.inflate(R.layout.fragment_search_city_fragment, container, false);
        citySearchView = (SearchView) view.findViewById(R.id.sv_swf_searchCity);
        cityListView = (ListView) view.findViewById(R.id.lv_swf_cityList);
        return view;
    }

    @SuppressLint("WrongConstant")
    private void json() {
        String url = "https://raw.githubusercontent.com/David-Haim-zz/CountriesToCitiesJSON/master/countriesToCities.json";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                countryList = new ArrayList<String>();
                for (int i = 0; i < response.names().length(); i++) {
                    try {
                        countryList.add(response.names().getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                cityList = new ArrayList<String>();
                for (int j = 0; j < countryList.size(); j++) {
                    JSONArray countryName = null;

                    try {
                        countryName = response.getJSONArray(countryList.get(j));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int k = 0; k < countryName.length(); k++) {
                        try {
                            cityList.add(countryName.getString(k));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cityList);
                cityListView.setAdapter(arrayAdapter);
                Log.i("responseListener", String.valueOf(response.length()));
                Log.i("names", String.valueOf(countryList));
                Log.i("cityNameSize", String.valueOf(cityList.size()));
                Log.i("cityname", String.valueOf(cityList));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error Occured! Please Check your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        json();
    }

    @Override
    public void onStart() {
        super.onStart();

        citySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.isEmpty())
                    cityListView.setVisibility(View.INVISIBLE);
                else {
                    cityListView.setVisibility(View.VISIBLE);
                    Log.i("touch", "submit touched");
                    if (flag_call == 0) {
                        requireFrag = new CityWeatherFragment(s);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ma, requireFrag).commit();
                    } else if (flag_call == 1) {
                        requireFrag = new CityAtmosphereFragment(s);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_atm, requireFrag).commit();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty())
                    cityListView.setVisibility(View.INVISIBLE);
                else {
                    cityListView.setVisibility(View.VISIBLE);
                    arrayAdapter.getFilter().filter(s);
                    cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            citySearchView.setQuery(String.valueOf(adapterView.getItemAtPosition(i)), true);
                            Log.i("pos", String.valueOf(adapterView.getItemAtPosition(i)));
                            if (flag_call == 0) {
                                requireFrag = new CityWeatherFragment(String.valueOf(adapterView.getItemAtPosition(i)));
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_ma, requireFrag).commit();

                            } else if (flag_call == 1) {
                                requireFrag = new CityAtmosphereFragment(String.valueOf(adapterView.getItemAtPosition(i)));
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_atm, requireFrag).commit();

                            }
                        }
                    });
                }
                return false;
            }
        });


    }
}