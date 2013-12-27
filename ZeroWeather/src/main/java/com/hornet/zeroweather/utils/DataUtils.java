package com.hornet.zeroweather.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Ahmed on 23.12.13.
 */
public class DataUtils {

// #MARK - Constants

// #MARK - Methods

    public static String getWeatherCountry(String json) throws JSONException{
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONObject sysJSONObject = jsonObject.getJSONObject("sys");
        return sysJSONObject.getString("country");
    }

    public static String getWeatherDescription(String json) throws JSONException{
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONArray weatherJSONArray = jsonObject.getJSONArray("weather");
        JSONObject currentWeatherJsonObject = weatherJSONArray.getJSONObject(0);
        return currentWeatherJsonObject.getString("description");
    }

    public static String getWeatherTemperature(String json) throws JSONException{
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONObject mainJSONObject = jsonObject.getJSONObject("main");
        return mainJSONObject.getString("temp");
    }

    public static String getWeatherCode(String json) throws JSONException{
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONArray weatherJSONArray = jsonObject.getJSONArray("weather");
        JSONObject currentWeatherJsonObject = weatherJSONArray.getJSONObject(0);
        return currentWeatherJsonObject.getString("id");
    }

    public static String getWeatherHumidity(String json) throws JSONException{
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONObject mainJSONObject = jsonObject.getJSONObject("main");
        return mainJSONObject.getString("humidity");
    }

    public static String getWeatherPressure(String json) throws JSONException{
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONObject mainJSONObject = jsonObject.getJSONObject("main");
        return mainJSONObject.getString("pressure");
    }

    public static String getWeatherCloudy(String json) throws JSONException{
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONObject mainJSONObject = jsonObject.getJSONObject("clouds");
        return mainJSONObject.getString("all");
    }

    public static String streamToString(InputStream inputStream) throws IOException {
        String output = "";
        if(inputStream != null){
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while( (line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            }finally{
                inputStream.close();
            }
            output = stringBuilder.toString();
        }
        return output;
    }

    public static long getCelsiusFromKelvin(float temperature){
        return Math.round(temperature - 273.15);
    }

    public static String getWeatherDescriptionForecast(String json, int index) throws JSONException{
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONArray listJSONArray = jsonObject.getJSONArray("list");
        JSONObject currentJSONObject = listJSONArray.getJSONObject(index);
        JSONArray weatherJSONArray = currentJSONObject.getJSONArray("weather");
        JSONObject currentWeatherJsonObject = weatherJSONArray.getJSONObject(0);
        return currentWeatherJsonObject.getString("description");
    }

    public static String getWeatherMinTemperatureForecast(String json, int index) throws JSONException {
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONArray listJSONArray = jsonObject.getJSONArray("list");
        JSONObject currentJSONObject = listJSONArray.getJSONObject(index);
        JSONObject tempJSONObject = currentJSONObject.getJSONObject("temp");
        return tempJSONObject.getString("min");
    }

    public static String getWeatherMaxTemperatureForecast(String json, int index) throws JSONException {
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONArray listJSONArray = jsonObject.getJSONArray("list");
        JSONObject currentJSONObject = listJSONArray.getJSONObject(index);
        JSONObject tempJSONObject = currentJSONObject.getJSONObject("temp");
        return tempJSONObject.getString("max");
    }

    public static String getWeatherDateForecast(String json, int index) throws JSONException {
        JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
        JSONArray listJSONArray = jsonObject.getJSONArray("list");
        JSONObject currentJSONObject = listJSONArray.getJSONObject(index);
        return currentJSONObject.getString("dt");
    }

}
