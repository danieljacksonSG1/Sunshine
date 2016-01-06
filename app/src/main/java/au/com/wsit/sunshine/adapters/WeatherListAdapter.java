package au.com.wsit.sunshine.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import au.com.wsit.sunshine.R;
import au.com.wsit.sunshine.utils.WeatherItems;

/**
 * Created by guyb on 5/01/16.
 */
public class WeatherListAdapter extends BaseAdapter
{
    Context mContext;
    WeatherItems[] mWeatherItems;

    // Constructor
    public WeatherListAdapter(Context context, WeatherItems[] items)
    {
        mContext = context;
        mWeatherItems = items;
    }


    @Override
    public int getCount() {
        return mWeatherItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mWeatherItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder
    {
        ImageView forecastIcon;
        TextView Date;
        TextView HighTemp;
        TextView LowTemp;
        TextView weatherCondition;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {

            // Use a different layout for "Today"
            if (position == 0)
            {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_forecast_today, null);
                holder = new ViewHolder();

                holder.forecastIcon = (ImageView) convertView.findViewById(R.id.list_item_icon);
                holder.Date = (TextView) convertView.findViewById(R.id.list_item_date_textview);
                holder.HighTemp = (TextView) convertView.findViewById(R.id.list_item_high_textview);
                holder.LowTemp = (TextView) convertView.findViewById(R.id.list_item_low_textview);
                holder.weatherCondition = (TextView) convertView.findViewById(R.id.list_item_forecast_textview);

                convertView.setTag(holder);
            }
            else
            {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_forecast, null);

                holder = new ViewHolder();

                holder.forecastIcon = (ImageView) convertView.findViewById(R.id.list_item_icon);
                holder.Date = (TextView) convertView.findViewById(R.id.list_item_date_textview);
                holder.HighTemp = (TextView) convertView.findViewById(R.id.list_item_high_textview);
                holder.LowTemp = (TextView) convertView.findViewById(R.id.list_item_low_textview);
                holder.weatherCondition = (TextView) convertView.findViewById(R.id.list_item_forecast_textview);

                convertView.setTag(holder);
            }


        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the string data at the specific postion
        WeatherItems Items = mWeatherItems[position];

        if (position == 0)
        {
            holder.Date.setText("TODAY");
        }
        else
        {
            holder.Date.setText(Items.getDate());
        }

        holder.HighTemp.setText(Items.getHighTemp() + "" );
        holder.LowTemp.setText(Items.getLowTemp() + "");

        holder.weatherCondition.setText(Items.getCondition());

        // Set the image in the list
        setImage(holder, Items);





        return convertView;
    }

    private boolean checkifToday()
    {
            // Check if it's today

        return true;
    }

    private void setImage(ViewHolder holder, WeatherItems Items)
    {
        try
        {
            switch (Items.getCondition())
            {

                case "Clear":
                    // Set image to be sun
                    holder.forecastIcon.setImageResource(R.drawable.sun);
                    break;
                case "Rain":
                    // Set image to be rain
                    holder.forecastIcon.setImageResource(R.drawable.rain);
                    break;
                case "Snow":
                    // Set image to be snow
                    holder.forecastIcon.setImageResource(R.drawable.snow);
                    break;
            }

        }
        catch (OutOfMemoryError e)
        {
            Log.i("DEBUG", "Out of memory");
        }
    }
}
