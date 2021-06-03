package com.mohit.weatherapp;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class viewHolder extends RecyclerView.ViewHolder
{
    public TextView dayValue,dayTempValue;
    public ImageView dayCloudImg;

    public viewHolder(@NonNull View itemView) {
        super(itemView);
        dayValue = (TextView)itemView.findViewById(R.id.tv_dayValue);
        dayTempValue = (TextView)itemView.findViewById(R.id.tv_dayTempValue);
        dayCloudImg = (ImageView)itemView.findViewById(R.id.iv_dayCloudImg);
    }
}
