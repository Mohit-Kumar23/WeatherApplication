package com.mohit.weatherapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<viewHolder> {
    public Context context;
    public ArrayList<model_value> dayTempList;

    public RecyclerViewAdapter(Context context,ArrayList<model_value> dayTempList)
    {
        this.context = context;
        this.dayTempList = dayTempList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_daily_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        model_value modelValue = dayTempList.get(position);

        holder.dayValue.setText(modelValue.getdayV());
        holder.dayTempValue.setText(modelValue.getdayTempV());
        int setCloudImg;
        switch(modelValue.getWeatherCloudCode())
        {

            case "1000"  :
            case "1100"  :  setCloudImg = R.drawable.sun;
                            break;

            case "1001"  :
            case "1102"  :  setCloudImg = R.drawable.cloudy;
                            break;

            case "1101"  :  setCloudImg = R.drawable.partly_cloudy;
                            break;

            case "2000"  :
            case "2100"  : setCloudImg = R.drawable.fog;
                           break;

            case "3000"  :
            case "3001"  : setCloudImg = R.drawable.wind;
                           break;

            case "3002"  : setCloudImg = R.drawable.strong_wind;
                           break;

            case "4001"  :
            case "4201"  :  setCloudImg = R.drawable.rainy;
                            break;

            case "4000"  :
            case "4200"  :  setCloudImg = R.drawable.light_rain;
                            break;

            case "5000"  :
            case "5001"  :
            case "5100"  : setCloudImg = R.drawable.snow;
                           break;

            case "5101"  : setCloudImg = R.drawable.heavy_snow;
                           break;

            case "6000"  :
            case "6001"  :
            case "6200"  :
            case "6201"  : setCloudImg = R.drawable.frezzing_drizzle;
                           break;

            case "7000"  :
            case "7101"  :
            case "7102"  : setCloudImg = R.drawable.ice_pellets;
                           break;

            case "8000"  : setCloudImg = R.drawable.thunderstorm;
                           break;

            default      : setCloudImg = R.drawable.unknown_weather;
        }

        holder.dayCloudImg.setImageResource(setCloudImg);

    }

    @Override
    public int getItemCount() {
        return dayTempList.size();
    }
}
