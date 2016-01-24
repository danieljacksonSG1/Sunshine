package au.com.wsit.sunshine.utils;

/**
 * Created by guyb on 5/01/16.
 */
public class WeatherItems
{

    String HighTemp;
    String LowTemp;
    String date;
    String condition;

    String mHumidity;
    String mPressue;
    String mWind;

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }

    public String getPressue() {
        return mPressue;
    }

    public void setPressue(String pressue) {
        mPressue = pressue;
    }

    public String getWind() {
        return mWind;
    }

    public void setWind(String wind) {
        mWind = wind;
    }




    public String getCondition()
    {
        return condition;
    }

    public void setCondition(String forecast)
    {
        condition = forecast;
    }

    public String getHighTemp() {
        return HighTemp;
    }

    public void setHighTemp(String highTemp) {
        HighTemp = highTemp;
    }

    public String getLowTemp() {
        return LowTemp;
    }

    public void setLowTemp(String lowTemp) {
        LowTemp = lowTemp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }




}
