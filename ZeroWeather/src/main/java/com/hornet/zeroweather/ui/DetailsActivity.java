package com.hornet.zeroweather.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hornet.zeroweather.R;
import com.hornet.zeroweather.adapter.CustomArrayAdapter;
import com.hornet.zeroweather.utils.DataUtils;
import com.hornet.zeroweather.utils.NetworkUtils;
import com.hornet.zeroweather.utils.UIUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by Ahmed on 26.12.13.
 */
public class DetailsActivity extends Activity {

// #MARK - Constants

    private String CITY;
    private ConnectivityManager connectivityManager;
    private ListView detailsList;

    private String[] descriptionArray = new String[7];
    private String[] dateArray = new String[7];
    private String[] minTemperatureArray = new String[7];
    private String[] maxTemperatureArray = new String[7];

// #MARK - Activity states

    @Override
    protected void onCreate(Bundle savedInstanceStates){
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        CITY = intent.getStringExtra(WeatherFragment.DATA_CITY_KEY);
        getActionBar().setTitle(CITY);
        int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
        if(actionBarTitleView != null){
            actionBarTitleView.setTypeface(UIUtils.getCustomFont(UIUtils.ROBOTO_SLAB_BOLD, this));
        }
        getActionBar().setIcon(null);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        detailsList = (ListView) findViewById(R.id.activityDetails_detailsList_listView);
        updateWeatherDataAsyncTask.execute();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

// #MARK - Listeners

// #MARK - Custom methods

    private void showForecast(){
        detailsList.setAdapter(new CustomArrayAdapter(getApplicationContext(), R.layout.item_details_list, dateArray, descriptionArray, minTemperatureArray, maxTemperatureArray));
    }

// #MARK - AsyncTask

    AsyncTask<Void, Void, Integer> updateWeatherDataAsyncTask = new AsyncTask<Void, Void, Integer>() {
        @Override
        protected Integer doInBackground(Void... voids) {
            if(NetworkUtils.isInternetConnectionAvailable(connectivityManager)){
                try{
                    URL downloadUrl = NetworkUtils.composeForecastWeatherDownloadURL(CITY);
                    InputStream inputStream = NetworkUtils.getJSONStream(downloadUrl);
                    String json = NetworkUtils.getStringJSON(inputStream);
                    for(int i = 0; i < 7; i++){
                        String description = DataUtils.getWeatherDescriptionForecast(json, i);
                        String minTemperature = DataUtils.getWeatherMinTemperatureForecast(json, i);
                        String maxTemperature = DataUtils.getWeatherMaxTemperatureForecast(json, i);
                        String date = DataUtils.getWeatherDateForecast(json, i);
                        descriptionArray[i] = description;
                        minTemperatureArray[i] = DataUtils.getCelsiusFromKelvin(Float.parseFloat(minTemperature)) + "˚";
                        maxTemperatureArray[i] = DataUtils.getCelsiusFromKelvin(Float.parseFloat(maxTemperature)) + "˚";
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(date) * 1000);
                        dateArray[i] = calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR);
                    }

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
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activityDetails_rootLayout_layout);
                mainLayout.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.fragmentMain_offlineMessage), Toast.LENGTH_LONG).show();
                return;
            }
            showForecast();
        }
    };

// #MARK - Menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
