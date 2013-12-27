package com.hornet.zeroweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hornet.zeroweather.R;

import java.util.List;

/**
 * Created by Ahmed on 27.12.13.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

// #MARK - Constants

    private final int currentlayoutId;

    private final Context context;
    private final String[] maxTemperature;
    private final String[] minTempretaure;
    private final String[] date;
    private final String[] description;

// #MARK - Constructor

    public CustomArrayAdapter(Context context, int textViewResourceId, String[] date, String[] description, String[] minTemperature, String[] maxTemperature) {
        super(context, textViewResourceId, date);
        this.context = context;
        this.maxTemperature = maxTemperature;
        this.minTempretaure = minTemperature;
        this.date = date;
        this.description = description;

        this.currentlayoutId = textViewResourceId;
    }

// #MARK - Override methods

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(currentlayoutId, null, false);
            viewHolder = new ViewHolder();

            viewHolder.dateTextView = (TextView) rowView.findViewById(R.id.itemDetailsList_weatherDate_textView);
            viewHolder.minTemperatureTextView = (TextView) rowView.findViewById(R.id.itemDetailsList_minTemperature_textView);
            viewHolder.maxTemperatureTextView = (TextView) rowView.findViewById(R.id.itemDetailsList_maxTemperature_textView);
            viewHolder.descriptionTextView = (TextView) rowView.findViewById(R.id.itemDetailsList_description_textView);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.dateTextView.setText(date[position]);
        viewHolder.descriptionTextView.setText(description[position]);
        viewHolder.maxTemperatureTextView.setText(maxTemperature[position]);
        viewHolder.minTemperatureTextView.setText(minTempretaure[position]);

        return rowView;
    }

// #MARK - Custom methods

// #MARK - ViewHolder

    static class ViewHolder{
        public TextView dateTextView;
        public TextView maxTemperatureTextView;
        public TextView minTemperatureTextView;
        public TextView descriptionTextView;
    }

}
