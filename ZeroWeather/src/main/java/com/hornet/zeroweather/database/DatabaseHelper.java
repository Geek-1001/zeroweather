package com.hornet.zeroweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmed on 23.12.13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

// #MARK - Constants

    private static final String DATABASE_NAME = "zeroWeather.db";
    private static final int DATABASE_VERSION = 1;

    // MAIN DATA TABLE
    public static final String MAIN_TABLE_NAME = "mainDataTable";
    public static final String MAIN_TABLE_COLUMN_ID = "_id";
    public static final String MAIN_TABLE_COLUMN_CITY = "city_title";
    public static final String MAIN_TABLE_COLUMN_COUNTRY = "country_title";
    public static final String MAIN_TABLE_COLUMN_TEMPERATURE = "temperature";
    public static final String MAIN_TABLE_COLUMN_DESCRIPTION = "description";
    public static final String MAIN_TABLE_COLUMN_HUMIDITY = "humidity";
    public static final String MAIN_TABLE_COLUMN_PRESSURE = "pressure";
    public static final String MAIN_TABLE_COLUMN_CLOUDY = "cloudy";
    public static final String MAIN_TABLE_COLUMN_WEATHER_CODE = "weather_code";
    public static final String MAIN_TABLE_COLUMN_UPDATE_TIME = "lastUpdateTime";

    // CITY TABLE
    public static final String CITY_TABLE_NAME = "cityTable";
    public static final String CITY_TABLE_COLUMN_ID = "_id";
    public static final String CITY_TABLE_COLUMN_CITY = "city_title";
    public static final String CITY_TABLE_COLUMN_TEMPERATURE = "city_temperature";

    // CREATE SCRIPT
    private static final String CREATE_CITY_TABLE = "CREATE TABLE " + CITY_TABLE_NAME + "( " + CITY_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY_TABLE_COLUMN_CITY + " TEXT, " + CITY_TABLE_COLUMN_TEMPERATURE + " TEXT " + ");";
    private static final String CREATE_MAIN_TABLE = "CREATE TABLE " + MAIN_TABLE_NAME + "( " + MAIN_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MAIN_TABLE_COLUMN_CITY + " TEXT, " + MAIN_TABLE_COLUMN_COUNTRY + " TEXT, " + MAIN_TABLE_COLUMN_TEMPERATURE + " REAL, " + MAIN_TABLE_COLUMN_DESCRIPTION + " TEXT, " + MAIN_TABLE_COLUMN_WEATHER_CODE + " INTEGER, " + MAIN_TABLE_COLUMN_HUMIDITY + " REAL, " + MAIN_TABLE_COLUMN_PRESSURE + " REAL, " + MAIN_TABLE_COLUMN_CLOUDY + " REAL, " + MAIN_TABLE_COLUMN_UPDATE_TIME + " TEXT " + " );";

    // DROP SCRIPT
    private static final String DROP_CITY_TABLE = " DROP TABLE IF EXISTS " + CITY_TABLE_NAME;
    private static final String DROP_MAIN_TABLE = " DROP TABLE IF EXISTS " + MAIN_TABLE_NAME;

// #MARK - DatabaseHelper states

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_CITY_TABLE);
        database.execSQL(CREATE_MAIN_TABLE);

        String[] defaultCity = {"Moscow", "Saint-Petersburg"};
        for(String city : defaultCity){
            ContentValues cv = new ContentValues();
            cv.put(CITY_TABLE_COLUMN_CITY, city);
            cv.put(CITY_TABLE_COLUMN_TEMPERATURE, "");
            database.insert(CITY_TABLE_NAME, null, cv);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(DROP_CITY_TABLE);
        database.execSQL(DROP_MAIN_TABLE);

        onCreate(database);
    }

// #MARK - Custom methods

}
