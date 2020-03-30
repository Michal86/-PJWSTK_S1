package zad1;

import org.json.JSONObject;
import java.util.Date;


public class Weather {

    private final String country;
    private String city;
    private String countryCode;
    private String currencyCode;
    private String atmosphericPressure;
    private String simpleDesc;
    private String advanceDesc;
    private String date;
    private double temperature;

    public Weather(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    private void setCountryCode(String countryCode) {
        this.countryCode = countryCode.toUpperCase();
    }

    private void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    void initialize(String jsonStrData, String currencyCode) {

        if (jsonStrData != null) {
            JSONObject jsonObject = new JSONObject(jsonStrData);
            setCity(jsonObject.getString("name"));
            setCountryCode(jsonObject.getJSONObject("sys").optString("country"));
            setAdvanceDesc(jsonObject.getJSONArray("weather").getJSONObject(0).optString("description"));
            setSimpleDesc(jsonObject.getJSONArray("weather").getJSONObject(0).optString("main"));
            setAtmosphericPressure(jsonObject.getJSONObject("main").optString("pressure"));
            setTemperature(jsonObject.getJSONObject("main").optDouble("temp"));
            setDate(jsonObject.getLong("dt"));
            setCurrencyCode(currencyCode);
        }


    }

    private void setDate(long dateUnix) {
        Date time = new Date(dateUnix * 1000);
        this.date = time.toString().substring(0, 16);
    }

    public String getDate() {
        return date;
    }

    private void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    private void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc;
    }

    public String getAdvanceDesc() {
        return advanceDesc;
    }

    private void setAdvanceDesc(String advanceDesc) {
        this.advanceDesc = advanceDesc;
    }

    public double getTemperature() {
        return temperature;
    }

    private void setTemperature(double temperature) {
        this.temperature = temperature - 273.15;
    }

    public String getAtmosphericPressure() {
        return atmosphericPressure;
    }

    private void setAtmosphericPressure(String atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }

}
