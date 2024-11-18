package com.example.forecasttamz2;

public class ForecastItem {
    private String cityName;
    private String dateTime;
    private String temperature;
    private String description;
    private String iconUrl;

    public ForecastItem(String cityName, String dateTime, String temperature, String description, String iconUrl) {
        this.cityName = cityName;
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
