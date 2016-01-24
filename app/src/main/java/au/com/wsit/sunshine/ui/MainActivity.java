package au.com.wsit.sunshine.ui;

import android.content.ActivityNotFoundException;

import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import au.com.wsit.sunshine.R;
import au.com.wsit.sunshine.adapters.WeatherListAdapter;
import au.com.wsit.sunshine.service.SunshineService;
import au.com.wsit.sunshine.ui.fragments.ForecastFragment;
import au.com.wsit.sunshine.utils.SunshineConstants;
import au.com.wsit.sunshine.utils.Utility;
import au.com.wsit.sunshine.utils.WeatherItems;


public class MainActivity extends ActionBarActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    WeatherListAdapter mWeatherListAdapter;
    WeatherItems[] mItems;
    String[] sevenDayForecast;
    ListView weatherItems;

    public static boolean twoPaneMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate called");

        if (findViewById(R.id.weather_detail_container) != null)
        {
            twoPaneMode = true;
            Log.i(TAG, "Two pane mode true");
        }
        else
        {
            twoPaneMode = false;
            Log.i(TAG, "Two pane mode false");

            ForecastFragment forecastFragment = new ForecastFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(forecastFragment, null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        }




    }

    public void updateWeather()
    {

        Log.i(TAG, "Updating weather");
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String location = mSharedPreferences.getString(getString(R.string.key_pref_location), getString(R.string.location_default));
        String temperatureUnit = mSharedPreferences.getString(getString(R.string.temperature_unit), getString(R.string.metric));


        Log.i(TAG, "Location is: " + location);
        Log.i(TAG, "Temperature unit is: " + temperatureUnit);

//        Refresh the weather data
//        FetchWeatherTask weatherTask = new FetchWeatherTask();
//        weatherTask.execute(location, temperatureUnit);


        Intent intent = new Intent(MainActivity.this, SunshineService.class);
        intent.putExtra("lqe", location);
        startService(intent);


    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]>
    {

        @Override
        protected String[] doInBackground(String... params)
        {
            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = params[1];
            String apikey = "1eafedae6d18bbb6549e29e8dae357ec";
            int numDays = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid=1eafedae6d18bbb6549e29e8dae357ec");

                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String API_KEY = "appid";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(API_KEY, apikey)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(TAG, "Built URL: " + builtUri.toString());


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            Log.i(TAG, forecastJsonStr);
            // Call method

            try
            {
                getWeatherDataFromJson(forecastJsonStr, 7);
                sevenDayForecast = getWeatherDataFromJson(forecastJsonStr, 7);
                return getWeatherDataFromJson(forecastJsonStr, 7);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                return null;
            }


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_settings:
                // Open the settings activity
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_refresh:

                break;
            case R.id.action_location:
                openPreferredLocationInMap();
                break;



        }

        return super.onOptionsItemSelected(item);
    }

    // Opens the users location from the zip code set
    private void openPreferredLocationInMap() {

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = mSharedPreferences.getString(getString(R.string.key_pref_location), getString(R.string.location_default));
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q", location).build();


        Intent locationIntent = new Intent(Intent.ACTION_VIEW);
        locationIntent.setData(geoLocation);
        // Try to start the intent, catch if we don't have a map app installed
        try
        {
            startActivity(locationIntent);
        }
        catch(ActivityNotFoundException e)
        {
            Log.i(TAG, e.getMessage());
            Toast.makeText(MainActivity.this, "No Map App installed", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    /* The date/time conversion code is going to be moved outside the asynctask later,
* so for convenience we're breaking it out into its own method now.
*/
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays) throws JSONException
    {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_SPEED = "speed";

        boolean isMetric = true;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[numDays];

        // Init an array of weather items to store the data

        mItems = new WeatherItems[weatherArray.length()];

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getString(getString(R.string.temperature_unit), "Metric").equals("Metric"))
        {
            Log.i(TAG, "Temperature unit is set to metric");
            isMetric = true;
        }
        else
        {
            isMetric = false;
        }

        for(int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;
            String pressure;
            String speed;
            String humidity;


            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);



            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);


            description = weatherObject.getString(OWM_DESCRIPTION);
            pressure = dayForecast.getString(OWM_PRESSURE);
            speed = dayForecast.getString(OWM_SPEED);
            humidity = dayForecast.getString(OWM_HUMIDITY);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;

            // Set the data for the day in the setter
            WeatherItems items = new WeatherItems();
            items.setDate(day);
            items.setCondition(description);
            items.setHighTemp(Utility.formatTemperature(this, high, isMetric));
            items.setLowTemp(Utility.formatTemperature(this, low, isMetric));
            items.setPressue(pressure);
            items.setHumidity(humidity);
            items.setWind(speed);
            // Set the data into the array
            mItems[i] = items;

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWeatherListAdapter = new WeatherListAdapter(MainActivity.this, mItems);
                weatherItems.setAdapter(mWeatherListAdapter);
            }
        });





        return resultStrs;

    }

}
