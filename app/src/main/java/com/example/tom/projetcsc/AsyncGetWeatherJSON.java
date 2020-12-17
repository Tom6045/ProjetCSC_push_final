package com.example.tom.projetcsc;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class AsyncGetWeatherJSON extends AsyncTask<String, Void, String[]> {

    public WeatherActivity activity;

    public AsyncGetWeatherJSON(WeatherActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        String url = strings[0];
        URL urlName = null;
        try{
            urlName =new URL(url); //location for Paris
            HttpURLConnection urlConnectionName = (HttpURLConnection) urlName.openConnection();
            try{



                InputStream inName =new BufferedInputStream(urlConnectionName.getInputStream());
                BufferedReader rdName = new BufferedReader(new InputStreamReader(inName, Charset.forName("UTF-8")));
                JSONObject jsonName = new JSONObject(readAll(rdName));

                String temp = jsonName.getJSONArray("consolidated_weather").getJSONObject(1).getString("the_temp");
                String state = jsonName.getJSONArray("consolidated_weather").getJSONObject(1).getString("weather_state_abbr");
                String weatherIcon = "https://www.metaweather.com/static/img/weather/png/"+state+".png";
                return new String[]{weatherIcon, temp};
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                urlConnectionName.disconnect();
            }
        }catch(MalformedURLException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String[] data){
        String weatherIcon = data[0];
        String temp = data[1];

        activity.curWeatherName = temp;

        AsyncBitmapDownloader  asyncBD = new AsyncBitmapDownloader((ImageView)activity.findViewById(R.id.ivWeather));
        asyncBD.execute(weatherIcon);
    }
}
