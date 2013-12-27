package com.hornet.zeroweather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hornet.zeroweather.database.DatabaseHelper;

/**
 * Created by Ahmed on 24.12.13.
 */
public class DatabaseUtils {

// #MARK - Constants

    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

// #MARK - Constructors

    public DatabaseUtils(Context context){
        this.context = context;
    }

//  #MARK - Custom methods

    public void openAccess(){
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    public void closeAccess(){
        if(databaseHelper != null){
            databaseHelper.close();
        }
    }

    public Cursor getAllDataForTable(String tableName){
        Cursor cursor;
        cursor = database.query(tableName, null, null, null, null, null, null);
        return cursor;
    }

    public void addNewCity(String cityName, String lastTemperature){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.CITY_TABLE_COLUMN_CITY, cityName);
        cv.put(DatabaseHelper.CITY_TABLE_COLUMN_TEMPERATURE, lastTemperature);
        database.insert(DatabaseHelper.CITY_TABLE_NAME, null, cv);
    }

    public void addNewCurrentWeatherData(String city, String country, String description, String temperature, String humidity, String pressure, String cloudy, String weatherCode, String lastUpdateTime){
        database.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_CITY, city);
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_COUNTRY, country);
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_DESCRIPTION, description);
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_TEMPERATURE, temperature);
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_HUMIDITY, humidity);
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_PRESSURE, pressure);
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_CLOUDY, cloudy);
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_WEATHER_CODE, weatherCode);
            cv.put(DatabaseHelper.MAIN_TABLE_COLUMN_UPDATE_TIME, lastUpdateTime);
            database.insert(DatabaseHelper.MAIN_TABLE_NAME, null, cv);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void removeItem(String tableName, long id){
        database.delete(tableName, "_id" + " = " + id, null);
    }

    public void updateCityItem(String city, String temperature, long id){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.CITY_TABLE_COLUMN_CITY, city);
        cv.put(DatabaseHelper.CITY_TABLE_COLUMN_TEMPERATURE, temperature);
        database.update(DatabaseHelper.CITY_TABLE_NAME, cv, "_id" + " = " + id, null);
    }

    public boolean isFieldPresentInColumn(String tableName, String columnName, String field){
        Cursor cursor = database.query(tableName, null, null, null, null, null, null);
        int columnIndex = cursor.getColumnIndex(columnName);
        if(cursor.moveToFirst()){
            do{
                String currentValue = cursor.getString(columnIndex);
                if(currentValue.equals(field)){
                    return true;
                }
            }while(cursor.moveToNext());
        }
        return false;
    }

    public Cursor getCursorForFieldInColumn(String tableName, String columnName, String field){
        Cursor cursor = getAllDataForTable(tableName);
        int columnIndex = cursor.getColumnIndex(columnName);
        if(cursor.moveToFirst()){
            do{
                String currentValue = cursor.getString(columnIndex);
                if(currentValue.equals(field)){
                    return cursor;
                }
            }while(cursor.moveToNext());
        }
        return null;
    }

    public Cursor getFirstRow(String tableName){
        Cursor cursor = getAllDataForTable(tableName);
        if(cursor.moveToFirst()){
            return cursor;
        }
        return null;
    }

    public String getStringFromDatabase(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }

    public float getRealFromDatabase(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getFloat(columnIndex);
    }

    public int getIntegerFromDatabase(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    public boolean isDatabaseEmpty(String table){
        Cursor cursor = getAllDataForTable(table);
        if(cursor.getCount() == 0){
            return true;
        }
        return false;
    }

}
