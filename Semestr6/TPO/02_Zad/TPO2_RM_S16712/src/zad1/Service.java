/**
 *
 *  @author Radzewicz Michał S16712
 *
 */

package zad1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Service {
    private final Weather weather;
    private double dRate;
    private String strRateName;
    private JSONObject jsonNbpRate;

    public Service(String kraj) {
        weather = new Weather(kraj);
    }

    static public String getContentFromUrl(String strUrl) {
        URL url;
        StringBuffer sbData = null;
        URLConnection urlConnection;

        try {
            url = new URL(strUrl);
            urlConnection = url.openConnection();
            urlConnection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            sbData = new StringBuffer();
            String line;

            while ((line = br.readLine()) != null) {
                sbData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (sbData != null) return sbData.toString();
        return "";
    }

    String getWeather(String city) {
        //api.openweathermap.org/data/2.5/weather?q={city name},{state},{country code}&appid={your api key}
        String jsonData = getContentFromUrl("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + weather.getCountry() + "&appid=be0df4f383b20e4b68f308f715f96720");

        if (!jsonData.contains("\"cod\":\"404\""))
            weather.initialize(jsonData);

        return jsonData;
    }

    Double getRate(String currCode) {
        String jsonStrData = getContentFromUrl("https://api.exchangeratesapi.io/latest?base=" + weather.getCountryCurrencyCode() );
        double rate = 0;

        if (jsonStrData != null) {
            JSONObject jsonObject = new JSONObject(jsonStrData);
            rate = jsonObject.getJSONObject("rates").optDouble(currCode);
            dRate = rate;
            strRateName = currCode;
        }

        return rate;
    }

    //xml
    Double getNBPRate() {
        String xmlNbpA = getContentFromUrl("http://www.nbp.pl/kursy/xml/a061z200327.xml");
        String xmlNbpB = getContentFromUrl("http://www.nbp.pl/kursy/xml/b012z200325.xml");

        if (!xmlNbpA.isEmpty() && !xmlNbpB.isEmpty()) {
            JSONObject jsonObjectA = XML.toJSONObject(xmlNbpA);
            JSONObject jsonObjectB = XML.toJSONObject(xmlNbpB);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObjectA);
            jsonArray.put(jsonObjectB);
            double rateToPLN = 0.0;

            JSONObject objCurrency = getCurrencyRateNBP(jsonArray);
            if (objCurrency != null) {
                rateToPLN = objCurrency.optDouble("kurs_sredni");
                jsonNbpRate = objCurrency;
            }
            return rateToPLN;
        }

        return null;
    }

    private JSONObject getCurrencyRateNBP(JSONArray jsonArray) {
        JSONObject jsonCurTmp;

        if (weather.getCountryCurrencyCode().equals("PLN")) {
            return new JSONObject("{\"kurs_sredni\":\"1\",\"kod_waluty\":\"PLN\",\"nazwa_waluty\":\"polski zloty\",\"przelicznik\":1}");
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            for (int j = 0; j < jsonArray.getJSONObject(i).optJSONObject("tabela_kursow").getJSONArray("pozycja").length(); j++) {
                jsonCurTmp = jsonArray.getJSONObject(i).optJSONObject("tabela_kursow").getJSONArray("pozycja").getJSONObject(j);
                if (jsonCurTmp.optString("kod_waluty").equals(weather.getCountryCurrencyCode())) {
                    return jsonCurTmp;
                }
            }
        }

        return null;
    }

    public Weather getWeather() {
        return weather;
    }

    public JSONObject getJsonNBPRate() {
        return jsonNbpRate;
    }

    public double getdRate() {
        return dRate;
    }

    public String getStrRateName() {
        return strRateName;
    }
}
  
