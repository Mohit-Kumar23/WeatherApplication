package com.mohit.weatherapp;


public class model_value
{
    private String dayTempV;
    private String dayV;
    private String weatherCloudCode;

    public model_value(String dayTempV, String dayV, String weatherCloudCode) {
        this.dayTempV = dayTempV;
        this.dayV = dayV;
        this.weatherCloudCode = weatherCloudCode;
    }

    public String getWeatherCloudCode() {
        return weatherCloudCode;
    }

    public void setWeatherCloudCode(String weatherCloudCode) {
        this.weatherCloudCode = weatherCloudCode;
    }

    public String getdayTempV() {
        return dayTempV;
    }

    public void setdayTempV(String dayTempV) {
        this.dayTempV = dayTempV;
    }

    public String getdayV() {
        return dayV;
    }

    public void setdayV(String dayV) {
        this.dayV = dayV;
    }
}
