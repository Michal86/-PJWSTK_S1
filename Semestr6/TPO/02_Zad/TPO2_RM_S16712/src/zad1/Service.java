/**
 *
 *  @author Radzewicz Michał S16712
 *
 */

package zad1;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Service {

    String country;
    String currency;
    String countryCode;
    String city;
    String customCurrency;

    public Service(String country) {
        this.country = country;
        setCurrencyAndCountryCode();
    }

    public String getWeather(String city) {
        this.city = city;
        String jsonString = "http://api.openweathermap.org/data/2.5/weather?q="+city+","+countryCode+"&APPID=be0df4f383b20e4b68f308f715f96720";
        try {
            return getResponse(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getRateFor(String currency) {
        this.customCurrency = currency;
        String url = "https://api.exchangeratesapi.io/latest?base="+this.currency;
        Double rate = 0.0;

        if (currency.equals(this.currency))
            rate = 1.0;
        else {
            try {
                String json = getResponse(url);
                rate = new JSONObject(json).getJSONObject("rates").getDouble(currency);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rate;
    }

    public Double getNBPRate() {
        String urlA = "http://www.nbp.pl/kursy/xml/a051z200313.xml";
        String urlB = "http://www.nbp.pl/kursy/xml/b010z200311.xml";

        try {
            JSONObject jsonA = XML.toJSONObject(getResponse(urlA));
            JSONObject jsonB = XML.toJSONObject(getResponse(urlB));
            JSONArray tabA = jsonA.getJSONObject("tabela_kursow").getJSONArray("pozycja");
            JSONArray tabB = jsonB.getJSONObject("tabela_kursow").getJSONArray("pozycja");
            JSONArray resultJSON = joinJSONArrays(tabA, tabB);
            if(currency.equals("PLN"))
                return 1.0d;
            for(int i = 0; i<resultJSON.length(); i++) {
                if(resultJSON.getJSONObject(i).getString("kod_waluty").equals(currency)) {
                    return new Double(resultJSON.getJSONObject(i).getString("kurs_sredni").replace(",", "."));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    private JSONArray joinJSONArrays(JSONArray ... arrays) {
        ArrayList<String> jsonArray = new ArrayList();

        for (JSONArray array : arrays) {
            jsonArray.add(array.toString().substring(1, array.toString().length() - 1));
        }

        String newJSONArray = String.join(",", jsonArray);

        return new JSONArray("[" + newJSONArray + "]");
    }

    private String getResponse(String url) throws IOException {
        URL urlConn = new URL(url);
        HttpURLConnection httpClient = (HttpURLConnection) urlConn.openConnection();
        httpClient.setRequestMethod("GET");
        InputStream stream = httpClient.getInputStream();

        return IOUtils.toString(stream, "UTF-8");
    }

    private void setCurrencyAndCountryCode() {
        String url = "https://restcountries.eu/rest/v2/name/"+this.country;
        try {
            String json = getResponse(url);
            this.currency = new JSONArray(json).getJSONObject(0).getJSONArray("currencies").getJSONObject(0).getString("code");
            this.countryCode = new JSONArray(json).getJSONObject(0).getString("alpha2Code");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCustomCurrency() {
        return customCurrency;
    }
}