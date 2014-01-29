package com.hornet.zeroweather.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.hornet.zeroweather.R;
import com.hornet.zeroweather.database.DatabaseHelper;
import com.hornet.zeroweather.utils.DatabaseUtils;
import com.hornet.zeroweather.utils.UIUtils;


public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

// #MARK - Constants

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView navigationList;

    private DatabaseUtils databaseUtils;

    private SimpleCursorAdapter adapter;

    public static final String DATA_CITY_KEY = "city_key";

// #MARK - Activity states

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
        if(actionBarTitleView != null){
            actionBarTitleView.setTypeface(UIUtils.getCustomFont(UIUtils.ROBOTO_SLAB_BOLD, this));
        }
        getActionBar().setIcon(null);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        drawerLayout = (DrawerLayout) findViewById(R.id.activityMain_navigationDrawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name,  R.string.app_name) {
            @Override
            public void onDrawerClosed(View view) {
                UIUtils.changeActionBarColorWithAnimation(getResources().getColor(R.color.actionBar_default), getResources().getColor(R.color.actionBar_navigationDrawerOpened), MainActivity.this);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                UIUtils.changeActionBarColorWithAnimation(getResources().getColor(R.color.actionBar_navigationDrawerOpened), getResources().getColor(R.color.actionBar_default), MainActivity.this);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        databaseUtils = new DatabaseUtils(this);
        databaseUtils.openAccess();

        String[] from = new String[] { DatabaseHelper.CITY_TABLE_COLUMN_CITY, DatabaseHelper.CITY_TABLE_COLUMN_TEMPERATURE };
        int[] to = new int[] { R.id.itemNavigationDrawer_cityTitle_textView, R.id.itemNavigationDrawer_lastTemperatureTitle_textView };
        Cursor cityCursor = databaseUtils.getAllDataForTable(DatabaseHelper.CITY_TABLE_NAME);
        adapter = new SimpleCursorAdapter(this, R.layout.item_navigation_drawer, cityCursor, from, to, 0){
            @Override
            public void setViewText(TextView textView, String text){
                textView.setTypeface(UIUtils.getCustomFont(UIUtils.ROBOTO_CONDENSED, getApplicationContext()));
                textView.setText(text);
            }
        };

        navigationList = (ListView) findViewById(R.id.activityMain_navigationList_listView);
        navigationList.setAdapter(adapter);
        navigationList.setOnItemClickListener(new DrawerItemClickListener());
        navigationList.setOnItemLongClickListener(drawerItemLongClickListener);
        getSupportLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
            Cursor cursor = databaseUtils.getFirstRow(DatabaseHelper.CITY_TABLE_NAME);
            int cityColumnIndex = cursor.getColumnIndex(DatabaseHelper.CITY_TABLE_COLUMN_CITY);
            String currentCity = cursor.getString(cityColumnIndex);
            Bundle weatherBundle = new Bundle();
            weatherBundle.putString(DATA_CITY_KEY, currentCity);
            WeatherFragment weatherFragment = new WeatherFragment();
            weatherFragment.setArguments(weatherBundle);
            getSupportFragmentManager().beginTransaction().add(R.id.activityMain_contentFrame, weatherFragment).commit();
        }

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
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

// #MARK - LoaderManager callback methods (from implementation)

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CityCursorLoader(this, databaseUtils);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

// #MARK - Listeners

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView cityTextView = (TextView) view.findViewById(R.id.itemNavigationDrawer_cityTitle_textView);
            String city = cityTextView.getText().toString();
            selectItem(position, city);
        }
    }

    private ListView.OnItemLongClickListener drawerItemLongClickListener = new ListView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            if(databaseUtils.getRowsNumber(DatabaseHelper.CITY_TABLE_NAME) != 1){
                databaseUtils.removeItem(DatabaseHelper.CITY_TABLE_NAME, id);
                getSupportLoaderManager().getLoader(0).forceLoad();
            }
            return true;
        }
    };

    private DialogInterface.OnClickListener cityDialogClickListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == DialogInterface.BUTTON_POSITIVE){
                EditText cityEditText = (EditText) ((AlertDialog) dialog).findViewById(R.id.dialogAddCity_city_editText);
                String newCity = cityEditText.getText().toString();
                if(!newCity.equals("")){
                    databaseUtils.addNewCity(newCity, "");
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            }
        }
    };

// #MARK - Custom methods

    private void selectItem(int position, String city) {
        Bundle weatherBundle = new Bundle();
        weatherBundle.putString(DATA_CITY_KEY, city);
        Fragment fragmentToReplace = new WeatherFragment();
        fragmentToReplace.setArguments(weatherBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.activityMain_contentFrame, fragmentToReplace).commit();
        navigationList.setItemChecked(position, true);
        drawerLayout.closeDrawers();
    }

// #MARK - Loaders

    static class CityCursorLoader extends CursorLoader {

        DatabaseUtils databaseUtils;

        public CityCursorLoader(Context context, DatabaseUtils databaseUtils) {
            super(context);
            this.databaseUtils = databaseUtils;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = this.databaseUtils.getAllDataForTable(DatabaseHelper.CITY_TABLE_NAME);
            return cursor;
        }

    }

// #MARK - Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_add){
            AddCityDialogFragment addCityDialogFragment = new AddCityDialogFragment();
            addCityDialogFragment.setListener(cityDialogClickListener);
            addCityDialogFragment.show(getSupportFragmentManager().beginTransaction(), "addCityDialog");
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
