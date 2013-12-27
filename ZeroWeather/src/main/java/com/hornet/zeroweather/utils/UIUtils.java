package com.hornet.zeroweather.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

import com.hornet.zeroweather.R;

/**
 * Created by Ahmed on 23.12.13.
 */
public class UIUtils {

// #MARK - Constants

    public static final int ROBOTO_THIN = 0;
    public static final int ROBOTO_SLAB_BOLD = 1;
    public static final int ROBOTO_BOLD_CONDENSED = 2;
    public static final int ROBOTO_CONDENSED = 3;

// #MARK - Methods

    public static final Typeface getCustomFont(int fontIndex, Context context){
        Typeface font;
        String fontPath;
        switch(fontIndex){
            case ROBOTO_THIN:
                fontPath = "fonts/Roboto-Thin.ttf";
                break;

            case ROBOTO_SLAB_BOLD:
                fontPath = "fonts/RobotoSlab-Bold.ttf";
                break;

            case ROBOTO_BOLD_CONDENSED:
                fontPath = "fonts/Roboto-BoldCondensed.ttf";
                break;

            case ROBOTO_CONDENSED:
                fontPath = "fonts/Roboto-Condensed.ttf";
                break;

            default:
                fontPath = null;
                break;
        }
        font = Typeface.createFromAsset(context.getAssets(), fontPath);
        return font;
    }

    public static void changeActionBarColorWithAnimation(int newColor, int oldColor, Activity activity){
        ActionBar actionBar = activity.getActionBar();
        Drawable newColorDrawable = new ColorDrawable(newColor);
        Drawable oldColorDrawable = new ColorDrawable(oldColor);
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{oldColorDrawable, newColorDrawable});
        actionBar.setBackgroundDrawable(transitionDrawable);
        transitionDrawable.startTransition(200);
        // hack!
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    public static int getWeatherIconId(int weatherCode){
        if(weatherCode >= 200 && weatherCode <= 232){
            // Thunderstorm
            return R.drawable.thunderstorms;
        }

        if(weatherCode >= 300 && weatherCode <= 321){
            // Drizzle
            return R.drawable.drizzle_snow;
        }

        if(weatherCode >= 500 && weatherCode <= 531){
            // Rain
            return R.drawable.drizzle;
        }

        if(weatherCode >= 600 && weatherCode <= 622){
            // Snow
            return R.drawable.snow;
        }

        if(weatherCode >= 701 && weatherCode <= 781){
            // Atmosphere
            return R.drawable.haze;
        }

        if(weatherCode >= 800 && weatherCode <= 804){
            // Clouds
            return R.drawable.cloudy;
        }
        return -1;
    }


}
