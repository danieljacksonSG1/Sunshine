package au.com.wsit.sunshine.utils;

/**
 * Created by guyb on 5/01/16.
 */
public class WeatherItems
{

    double HighTemp;
    double LowTemp;
    String date;
    String condition;

    public String getCondition()
    {
        return condition;
    }

    public void setCondition(String forecast)
    {
        condition = forecast;
    }

    public double getHighTemp() {
        return HighTemp;
    }

    public void setHighTemp(double highTemp) {
        HighTemp = highTemp;
    }

    public double getLowTemp() {
        return LowTemp;
    }

    public void setLowTemp(double lowTemp) {
        LowTemp = lowTemp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }




}
