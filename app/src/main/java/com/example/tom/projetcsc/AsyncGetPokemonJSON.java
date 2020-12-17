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

public class AsyncGetPokemonJSON extends AsyncTask<String, Void, String[]> {

    public PokemonActivity activity;

    public AsyncGetPokemonJSON(PokemonActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        String rd = strings[0];
        URL urlName = null;
        try{
            String frontSprite = "https://pokeres.bastionbot.org/images/pokemon/"+rd+".png";

            urlName =new URL("https://pokeapi.co/api/v2/pokemon-species/"+rd+"/");
            HttpURLConnection urlConnectionName = (HttpURLConnection) urlName.openConnection();
            try{
                InputStream inName =new BufferedInputStream(urlConnectionName.getInputStream());
                BufferedReader rdName = new BufferedReader(new InputStreamReader(inName, Charset.forName("UTF-8")));
                JSONObject jsonName = new JSONObject(readAll(rdName));
                String name = jsonName.getJSONArray("names").getJSONObject(4).getString("name");

                return new String[]{frontSprite, name};
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
        String frontSprite = data[0];
        String name = data[1];

        activity.curPokemonName = name;

        AsyncBitmapDownloader  asyncBD = new AsyncBitmapDownloader((ImageView)activity.findViewById(R.id.ivPokemon));
        asyncBD.execute(frontSprite);
    }
}
