package au.com.wsit.sunshine;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity
{

    public static final String TAG = DetailActivity.class.getSimpleName();
    public static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    public String mForecastStr;

    TextView weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        weatherData = (TextView) findViewById(R.id.weather_data_textView);

        Intent intent = getIntent();
        mForecastStr = intent.getStringExtra(SunshineConstants.KEY_FORECAST_DATA);
        weatherData.setText(mForecastStr);


    }

    private Intent createShareForecastIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);

        return shareIntent;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null)
        {
            mShareActionProvider.setShareIntent(createShareForecastIntent());

        }
        else
        {
            Log.d(TAG, "Share Action Provider is null");
        }


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
                Intent settingsIntent = new Intent(DetailActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_share:

        }

        return super.onOptionsItemSelected(item);
    }
}
