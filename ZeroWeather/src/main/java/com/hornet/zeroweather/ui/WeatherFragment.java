package com.hornet.zeroweather.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hornet.zeroweather.R;
import com.hornet.zeroweather.database.DatabaseHelper;
import com.hornet.zeroweather.utils.DataUtils;
import com.hornet.zeroweather.utils.DatabaseUtils;
import com.hornet.zeroweather.utils.DateTimeUtils;
import com.hornet.zeroweather.utils.NetworkUtils;
import com.hornet.zeroweather.utils.UIUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by Ahmed on 23.12.13.
 */
public class WeatherFragment extends Fragment {

// #MARK - Constants

    private ConnectivityManager connectivityManager;
    private DatabaseUtils databaseUtils;

    private String CITY;
    public static String DATA_CITY_KEY = "city_key";

    private View mainView;

// #MARK - Fragment states

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        databaseUtils = new DatabaseUtils(getActivity());
        databaseUtils.openAccess();

        Bundle bundle = getArguments();
        String city = bundle.getString(MainActivity.DATA_CITY_KEY);
        CITY = city;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        mainView = inflater.inflate(R.layout.fragment_main, viewGroup, false);

        TextView temperatureValueTextView = (TextView) mainView.findViewById(R.id.fragmentMain_temperatureValue_textView);
        TextView cityNameTextView = (TextView) mainView.findViewById(R.id.fragmentMain_currentCityName_textView);
        TextView countryNameTextView = (TextView) mainView.findViewById(R.id.fragmentMain_currentCountryName_textView);
        TextView weatherDescriptionTextView = (TextView) mainView.findViewById(R.id.fragmentMain_weatherDescription_textView);
        Button moreButton = (Button) mainView.findViewById(R.id.fragmentMain_more_button);

        temperatureValueTextView.setTypeface(UIUtils.getCustomFont(UIUtils.ROBOTO_THIN, getActivity()));
        cityNameTextView.setTypeface(UIUtils.getCustomFont(UIUtils.ROBOTO_BOLD_CONDENSED, getActivity()));
        countryNameTextView.setTypeface(UIUtils.getCustomFont(UIUtils.ROBOTO_CONDENSED, getActivity()));
        weatherDescriptionTextView.setTypeface(UIUtils.getCustomFont(UIUtils.ROBOTO_CONDENSED, getActivity()));
        moreButton.setOnClickListener(moreButtonClickListener);

        if(databaseUtils.isFieldPresentInColumn(DatabaseHelper.MAIN_TABLE_NAME, DatabaseHelper.MAIN_TABLE_COLUMN_CITY, CITY)){
            Cursor cursor = databaseUtils.getCursorForFieldInColumn(DatabaseHelper.MAIN_TABLE_NAME, DatabaseHelper.MAIN_TABLE_COLUMN_CITY, CITY);
            int lastUpdateTimeColumnIndex = cursor.getColumnIndex(DatabaseHelper.MAIN_TABLE_COLUMN_UPDATE_TIME);
            String lastUpdateTimeString = cursor.getString(lastUpdateTimeColumnIndex);
            Calendar currentTime = DateTimeUtils.getCurrentTimeCalendar();
            Calendar lastUpdateTimeCalendar = DateTimeUtils.getCalendarFromString(lastUpdateTimeString);
            if(!DateTimeUtils.isUpdateTimeRecently(currentTime, lastUpdateTimeCalendar)){
                cursor = databaseUtils.getCursorForFieldInColumn(DatabaseHelper.MAIN_TABLE_NAME, DatabaseHelper.MAIN_TABLE_COLUMN_CITY, CITY);
                int idColumnIndex = cursor.getColumnIndex(DatabaseHelper.MAIN_TABLE_COLUMN_ID);
                long currentCityRowId = cursor.getInt(idColumnIndex);
                databaseUtils.removeItem(DatabaseHelper.MAIN_TABLE_NAME, currentCityRowId);
                updateWeatherDataAsyncTask.execute();
            } else {
                updateWeatherFromDatabase();
            }
        } else {
            updateWeatherDataAsyncTask.execute();
        }

        return mainView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        connectivityManager = null;
        databaseUtils.closeAccess();
    }

// #MARK - Listeners

    View.OnClickListener moreButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent detailsIntent = new Intent(getActivity(), DetailsActivity.class);
            detailsIntent.putExtra(DATA_CITY_KEY, CITY);
            startActivity(detailsIntent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    };

// #MARK - Custom methods

    private void updateWeatherFromDatabase(){
        Cursor currentCursor = databaseUtils.getCursorForFieldInColumn(DatabaseHelper.MAIN_TABLE_NAME, DatabaseHelper.MAIN_TABLE_COLUMN_CITY, CITY);
        TextView temperatureValueTextView = (TextView) mainView.findViewById(R.id.fragmentMain_temperatureValue_textView);
        TextView cityNameTextView = (TextView) mainView.findViewById(R.id.fragmentMain_currentCityName_textView);
        TextView countryNameTextView = (TextView) mainView.findViewById(R.id.fragmentMain_currentCountryName_textView);
        TextView weatherDescriptionTextView = (TextView) mainView.findViewById(R.id.fragmentMain_weatherDescription_textView);
        ImageView weatherIconImageView = (ImageView) mainView.findViewById(R.id.fragmentMain_weatherIcon_imageView);

        TextView cloudyTextView = (TextView) mainView.findViewById(R.id.fragmentMain_cloudy_textView);
        TextView humidityTextView = (TextView) mainView.findViewById(R.id.fragmentMain_humidity_textView);
        TextView pressureTextView = (TextView) mainView.findViewById(R.id.fragmentMain_pressure_textView);

        if(!databaseUtils.isDatabaseEmpty(DatabaseHelper.MAIN_TABLE_NAME)){
            String country = databaseUtils.getStringFromDatabase(currentCursor, DatabaseHelper.MAIN_TABLE_COLUMN_COUNTRY);
            String description = databaseUtils.getStringFromDatabase(currentCursor, DatabaseHelper.MAIN_TABLE_COLUMN_DESCRIPTION);
            float temperature = databaseUtils.getRealFromDatabase(currentCursor, DatabaseHelper.MAIN_TABLE_COLUMN_TEMPERATURE);
            int weatherCode = databaseUtils.getIntegerFromDatabase(currentCursor, DatabaseHelper.MAIN_TABLE_COLUMN_WEATHER_CODE);
            float humidity = databaseUtils.getRealFromDatabase(currentCursor, DatabaseHelper.MAIN_TABLE_COLUMN_HUMIDITY);
            float pressure = databaseUtils.getRealFromDatabase(currentCursor, DatabaseHelper.MAIN_TABLE_COLUMN_PRESSURE);
            float cloudy = databaseUtils.getRealFromDatabase(currentCursor, DatabaseHelper.MAIN_TABLE_COLUMN_CLOUDY);

            long celsius = DataUtils.getCelsiusFromKelvin(temperature);
            temperatureValueTextView.setText(celsius + "°");
            cityNameTextView.setText(CITY);
            countryNameTextView.setText(country);
            weatherDescriptionTextView.setText(description);
            cloudyTextView.setText(cloudy + "%");
            pressureTextView.setText(pressure + " Pa");
            humidityTextView.setText(humidity + "%");
            weatherIconImageView.setImageDrawable(getResources().getDrawable(UIUtils.getWeatherIconId(weatherCode)));
        }
    }

// #MARK - AsyncTask

    AsyncTask<Void, Void, Integer> updateWeatherDataAsyncTask = new AsyncTask<Void, Void, Integer>() {
        @Override
        protected Integer doInBackground(Void... voids) {
            if(NetworkUtils.isInternetConnectionAvailable(connectivityManager)){
                try{
                    URL downloadUrl = NetworkUtils.composeMainWeatherDownloadURL(CITY);
                    InputStream inputStream = NetworkUtils.getJSONStream(downloadUrl);
                    String json = NetworkUtils.getStringJSON(inputStream);
                    String country = DataUtils.getWeatherCountry(json);
                    String temperature = DataUtils.getWeatherTemperature(json);
                    String description = DataUtils.getWeatherDescription(json);
                    String weatherCode = DataUtils.getWeatherCode(json);
                    String humidity = DataUtils.getWeatherHumidity(json);
                    String pressure = DataUtils.getWeatherPressure(json);
                    String cloudy = DataUtils.getWeatherCloudy(json);

                    databaseUtils.addNewCurrentWeatherData(CITY, country, description, temperature, humidity, pressure, cloudy, weatherCode, DateTimeUtils.getCurrentTimeString());
                    Cursor cursor = databaseUtils.getCursorForFieldInColumn(DatabaseHelper.CITY_TABLE_NAME, DatabaseHelper.CITY_TABLE_COLUMN_CITY, CITY);
                    int idColumnIndex = cursor.getColumnIndex(DatabaseHelper.CITY_TABLE_COLUMN_ID);
                    long currentCityId = cursor.getInt(idColumnIndex);
                    long celsius = DataUtils.getCelsiusFromKelvin(Float.parseFloat(temperature));
                    databaseUtils.updateCityItem(CITY, celsius + "˚", currentCityId);
                    getActivity().getSupportLoaderManager().getLoader(0).forceLoad();

                } catch(MalformedURLException exception){
                    exception.printStackTrace();
                } catch(IOException exception){
                    exception.printStackTrace();
                } catch(JSONException exception){
                    exception.printStackTrace();
                }
                return 1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == null){
                RelativeLayout mainLayout = (RelativeLayout) mainView.findViewById(R.id.fragmentMain_mainLayout);
                mainLayout.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), getResources().getString(R.string.fragmentMain_offlineMessage), Toast.LENGTH_LONG).show();
                return;
            }
            updateWeatherFromDatabase();
        }

    };

// #MARK - Menu

}
