package com.hornet.zeroweather.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Ahmed on 23.12.13.
 */
public class NetworkUtils {

// #MARK - Constants

    private static final String WEATHER_DATA_API_ID = "5e2545b73e36636f9c2b49280b9a078e";
    private static final int TIME_CONNECTION_TIMEOUT = 20000;
    private static final int TIME_READ_TIMEOUT = 10000;

// #MARK - Methods

    public static InputStream getJSONStream(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(TIME_CONNECTION_TIMEOUT);
        connection.setReadTimeout(TIME_READ_TIMEOUT);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        return connection.getInputStream();
    }

    public static String getStringJSON(InputStream inputStream) throws IOException {
        String jsonString = DataUtils.streamToString(inputStream);
        return jsonString;
    }

    public static boolean isInternetConnectionAvailable(ConnectivityManager connectivityManager){
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    public static URL composeMainWeatherDownloadURL(String city) throws MalformedURLException {
        String rootUrlPart = "http://api.openweathermap.org/data/2.5";

        String[] cityTitleParts = city.split("\\S");
        String finalCity = city;
        if(cityTitleParts.length >= 2){
            finalCity = "";
            for(int i = 0; i < cityTitleParts.length - 1; i++){
                finalCity = finalCity + cityTitleParts[i] + "-";
            }
            finalCity = finalCity + cityTitleParts[cityTitleParts.length];
        }
        URL url = new URL(rootUrlPart + "/" + "weather?q=" + finalCity + "&APPID=" + WEATHER_DATA_API_ID);
        return url;
    }

    public static URL composeForecastWeatherDownloadURL(String city) throws MalformedURLException {
        String rootUrlPart = "http://api.openweathermap.org/data/2.5";
        String[] cityTitleParts = city.split("\\S");    // check if city name have SPACE symbol and change it to "-"
        String finalCity = city;
        if(cityTitleParts.length >= 2){
            finalCity = "";
            for(int i = 0; i < cityTitleParts.length - 1; i++){
                finalCity = finalCity + cityTitleParts[i] + "-";
            }
            finalCity = finalCity + cityTitleParts[cityTitleParts.length];
        }
        URL url = new URL(rootUrlPart + "/" + "forecast/daily?q=" + finalCity + "&cnt=7" + "&APPID=" + WEATHER_DATA_API_ID);
        return url;
    }


}
