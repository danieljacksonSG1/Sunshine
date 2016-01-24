package au.com.wsit.sunshine.ui.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import au.com.wsit.sunshine.R;
import au.com.wsit.sunshine.ui.DetailActivity;
import au.com.wsit.sunshine.utils.SunshineConstants;

/**
 * Created by guyb on 6/01/16.
 */
public class DetailFragment extends Fragment
{
    public static final String TAG = DetailFragment.class.getSimpleName();

    TextView Date;
    TextView HighTemp;
    TextView LowTemp;
    TextView Condition;
    TextView Humidity;
    TextView Wind;
    TextView Pressure;

    ImageView icon;

    String date;
    String high;
    String low;
    String weatherCondition;
    String humidity;
    String wind;
    String pressure;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "View destroyed");


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Get reference to the XML IDs
        Date = (TextView) rootView.findViewById(R.id.dateTextView);
        HighTemp = (TextView) rootView.findViewById(R.id.highTempTextView);
        LowTemp = (TextView) rootView.findViewById(R.id.lowTempTextView);
        Condition = (TextView) rootView.findViewById(R.id.weatherConditionTextView);
        Humidity = (TextView) rootView.findViewById(R.id.humidityTextView);
        Wind = (TextView) rootView.findViewById(R.id.windTextView);
        Pressure = (TextView) rootView.findViewById(R.id.pressureTextView);
        icon = (ImageView) rootView.findViewById(R.id.detailViewIcon);


        getIntentData();



        setImage(weatherCondition);

        // Set in the UI
        Date.setText(date);
        HighTemp.setText(high);
        LowTemp.setText(low);
        Condition.setText(weatherCondition);
        Humidity.setText(humidity);
        Wind.setText(wind);
        Pressure.setText(pressure);



        return rootView;
    }


    private void getIntentData()
    {

        try
        {
            // Get the data from the intent
            Bundle bundle = getArguments();

            date = bundle.getString(SunshineConstants.KEY_DATE);
            high = bundle.getString(SunshineConstants.KEY_HIGH_TEMP);
            low = bundle.getString(SunshineConstants.KEY_LOW_TEMP);
            weatherCondition = bundle.getString(SunshineConstants.KEY_WEATHER_CONDITION);
            humidity = bundle.getString(SunshineConstants.KEY_HUMIDITY);
            wind = bundle.getString(SunshineConstants.KEY_WIND);
            pressure = bundle.getString(SunshineConstants.KEY_PRESSUE);
        }
        catch (NullPointerException e)
        {
            Log.i(TAG, e.getMessage());
        }

    }

    public void setImage(String condition)
    {
        try
        {
            switch (condition)
            {

                case "Clear":
                    // Set image to be sun
                    icon.setImageResource(R.drawable.sun);
                    break;
                case "Rain":
                    // Set image to be rain
                    icon.setImageResource(R.drawable.rain);
                    break;
                case "Snow":
                    // Set image to be snow
                    icon.setImageResource(R.drawable.snow);
                    break;
            }

        }
        catch (Exception e)
        {
            Log.i(TAG, "Error setting image: " + e.getMessage());
        }
    }

}
