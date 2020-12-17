package com.example.tom.projetcsc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity {

    public ArrayList<Player> players;
    public Boolean contactGameAllowed;
    public Boolean textContactGameAllowed;
    public Boolean callContactGameAllowed;
    public Boolean devModeStatus;
    public SharedPreferences prefs;
    private boolean firstGame = true;
    private int roundsAsNarratorLimit = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        PlayerList.currentNarrator = PlayerList.getRandomPlayer();

        Button btnPokemonGame = (Button)findViewById(R.id.btnPokemonGame);
        Button btnCallContactGame = (Button)findViewById(R.id.btnCallContactGame);
        Button btnTextContactGame = (Button)findViewById(R.id.btnTextContactGame);
        Button btnSlowMovementGame = (Button)findViewById(R.id.btnSlowMovementGame);
        Button btnShakeGame = (Button)findViewById(R.id.btnShakeGame);
        Button btnLightGame = (Button)findViewById(R.id.btnLightGame);
        Button btnFastPassGame = (Button)findViewById(R.id.btnFastPassGame);
        Button btnChooseGame = (Button)findViewById(R.id.btnChooseGame);
        Button btnTruthOrDareGame = (Button)findViewById(R.id.btnTruthOrDareGame);
        Button btnOrientationGame = (Button)findViewById(R.id.btnOrientationGame);
        Button btnWeatherGame = (Button)findViewById(R.id.btnWeatherGame);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //with few players, each narrator keeps the phone longer
        if (PlayerList.getPlayerList().size()<5){
            roundsAsNarratorLimit=4;
        } else if (PlayerList.getPlayerList().size()>4 && PlayerList.getPlayerList().size()<7 ){
            roundsAsNarratorLimit=3;
        }else{
            roundsAsNarratorLimit=2;
        }

        btnSlowMovementGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSlowMovementGame = new Intent(getApplicationContext(), SlowMovementActivity.class);
                startActivity(launchSlowMovementGame);
            }
        });

        btnFastPassGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchFastPassGame = new Intent(getApplicationContext(), FastPassActivity.class);
                startActivity(launchFastPassGame);
            }
        });

        btnPokemonGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchPokemonGame = new Intent(getApplicationContext(), PokemonActivity.class);
                startActivity(launchPokemonGame);
            }
        });

        btnCallContactGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactGameAllowed = prefs.getBoolean("playWithContactGame", false);
                callContactGameAllowed = prefs.getBoolean("playWithCallContactGame", false);
                if(callContactGameAllowed && contactGameAllowed) {
                    Intent launchCallContactGame = new Intent(getApplicationContext(), CallContactActivity.class);
                    startActivity(launchCallContactGame);
                }else{
                    Toast.makeText(getApplicationContext(), "Call contact game is not allowed", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnTextContactGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactGameAllowed = prefs.getBoolean("playWithContactGame", false);
                textContactGameAllowed = prefs.getBoolean("playWithTextContactGame", false);
                if(textContactGameAllowed && contactGameAllowed) {
                    Intent launchTextContactGame = new Intent(getApplicationContext(), TextContactActivity.class);
                    startActivity(launchTextContactGame);
                }else{
                    Toast.makeText(getApplicationContext(), "Text contact game is not allowed", Toast.LENGTH_LONG).show();
                }
            }
        });



        btnShakeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchShakeGame = new Intent(getApplicationContext(), ShakeActivity.class);
                startActivity(launchShakeGame);
            }
        });

        btnLightGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchLightGame = new Intent(getApplicationContext(), LightActivity.class);
                startActivity(launchLightGame);
            }
        });

        btnChooseGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchChooseGame = new Intent(getApplicationContext(), ChooseActivity.class);
                startActivity(launchChooseGame);
            }
        });

        btnTruthOrDareGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchTruthOrDareGame = new Intent(getApplicationContext(), TruthDareActivity.class);
                startActivity(launchTruthOrDareGame);
            }
        });

        btnOrientationGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchOrientationGame = new Intent(getApplicationContext(), OrientationActivity.class);
                startActivity(launchOrientationGame);
            }
        });

        btnWeatherGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchWeatherGame = new Intent(getApplicationContext(),  WeatherActivity.class);
                startActivity(launchWeatherGame);
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        devModeStatus = prefs.getBoolean("devModeStatus", true);
        if(!devModeStatus) {
            PlayerList.currentNarrator.roundsAsNarrator += 1;
            Log.i("the narrator has been ", "narrator for " + PlayerList.currentNarrator.roundsAsNarrator + " rounds");
            if (firstGame) {
                Intent launchFirstGame = new Intent(getApplicationContext(), FirstActivity.class);
                startActivity(launchFirstGame);
                firstGame = false;
            } else {
                if (PlayerList.currentNarrator.roundsAsNarrator < roundsAsNarratorLimit) {
                    launchRandomActivity(getApplicationContext());
                } else {
                    launchChangeNarratorActivity(getApplicationContext());
                }
            }
        }
    }


    public void launchRandomActivity(Context context){
        int rdActivity = ThreadLocalRandom.current().nextInt(1,10);
        Log.i("rd activity",rdActivity+"");
        switch(rdActivity){
            case 1: //Pokemon
                Intent launchPokemonGame = new Intent(context, PokemonActivity.class);
                startActivity(launchPokemonGame);
                break;
            case 2: //Call contact game
                contactGameAllowed = prefs.getBoolean("playWithContactGame", false);
                callContactGameAllowed = prefs.getBoolean("playWithCallContactGame", false);
                if(callContactGameAllowed && contactGameAllowed) {
                    Intent launchCallContactGame = new Intent(context, CallContactActivity.class);
                    startActivity(launchCallContactGame);
                }else{
                    launchRandomActivity(context);
                }
                break;
            case 3: //Text contact game
                contactGameAllowed = prefs.getBoolean("playWithContactGame", false);
                textContactGameAllowed = prefs.getBoolean("playWithTextContactGame", false);
                if(textContactGameAllowed && contactGameAllowed) {
                    Intent launchTextContactGame = new Intent(context, TextContactActivity.class);
                    startActivity(launchTextContactGame);
                }else{
                    launchRandomActivity(context);
                }
                break;
            case 4: //Shake Game
                Intent launchShakeGame = new Intent(context, ShakeActivity.class);
                startActivity(launchShakeGame);
                break;
            case 5: //Light game
                Intent launchLightGame = new Intent(context, LightActivity.class);
                startActivity(launchLightGame);
                break;
            case 6: //Truth or Dare
                Intent launchTruthDareGame = new Intent(context, TruthDareActivity.class);
                startActivity(launchTruthDareGame);
                break;
            case 7: //Find the direction
                Intent launchOrientationGame = new Intent(context, OrientationActivity.class);
                startActivity(launchOrientationGame);
                break;
            case 8: //Guess the Weather
                Intent launchWeatherGame = new Intent(context, WeatherActivity.class);
                startActivity(launchWeatherGame);
                break;
            case 9: //We give a slight chance to change narrator here
                launchChangeNarratorActivity(context);
                break;
        }
    }

    public void launchChangeNarratorActivity(Context context){
        int rdChange = ThreadLocalRandom.current().nextInt(1,6);
        switch(rdChange){
            case 1: //Slow movement
                Intent launchSlowMovementGame = new Intent(context, SlowMovementActivity.class);
                startActivity(launchSlowMovementGame);
                break;
            case 2: //Fast pass
                Intent launchFastPassGame = new Intent(context, FastPassActivity.class);
                startActivity(launchFastPassGame);
                break;
            case 3: //Launch another activity
                launchRandomActivity(context);
                break;
            case 4: //Choose next narrator
            case 5: //just to give it more chance to be launched
                Intent launchChooseGame = new Intent(context, ChooseActivity.class);
                startActivity(launchChooseGame);
                break;
        }
    }
}