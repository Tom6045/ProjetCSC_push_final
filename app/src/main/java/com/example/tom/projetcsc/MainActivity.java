package com.example.tom.projetcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public TextView tvPlayerList;
    public ListView lvPlayers;
    public Button btnAddPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartGame = (Button)findViewById(R.id.btnStartGame);
        btnAddPlayers = (Button)findViewById(R.id.btnAddPlayers);
        tvPlayerList = (TextView)findViewById(R.id.tvPlayerList);
        lvPlayers = (ListView)findViewById(R.id.lvPlayers);
        Button btnClearPlayers = (Button)findViewById(R.id.btnClearPlayers);

        fillListWithPlayers();

        // Passer à l'activité des préférences
        Button btnGamePreferences = findViewById(R.id.btnGamePreferences);
        btnGamePreferences.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToPrefs = new Intent(getApplicationContext(), GamePreferences.class);
                startActivity(goToPrefs);
            }
        });

        btnAddPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setPlayers = new Intent(getApplicationContext(), SetPlayersActivity.class);
                startActivityForResult(setPlayers, 2);
                }
        });

        btnClearPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerList.clearPlayerList();
                Toast.makeText(getApplicationContext(), "List of player cleared", Toast.LENGTH_LONG).show();
                fillListWithPlayers();
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayerList.getPlayerList().size()<3){
                    Toast.makeText(getApplicationContext(), "A minimum of 3 players is required", Toast.LENGTH_SHORT).show();
                }else{
                    Intent launchGame = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(launchGame);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(data!=null && data.getExtras().getBoolean("newPlayerAdded")) {
                if (PlayerList.getPlayerList().size()>7){
                    btnAddPlayers.setVisibility(View.INVISIBLE);
                    fillListWithPlayers();
                    Log.i("1",PlayerList.getPlayerList().toString());
                }else{
                    Intent setPlayers = new Intent(getApplicationContext(), SetPlayersActivity.class);
                    startActivityForResult(setPlayers, 2);
                }
            }
            else{
                fillListWithPlayers();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        PlayerList.clearScores();
    }

    public void fillListWithPlayers(){
        ArrayAdapter<String> tableau = new ArrayAdapter<>(lvPlayers.getContext(), R.layout.players_layout);
        if (PlayerList.getPlayerList().size()>0) {
            for (int i = 0; i<PlayerList.getPlayerNames().length; i++) {
                tableau.add(PlayerList.getPlayerNames()[i]);
            }
        }
        lvPlayers.setAdapter(tableau);
    }


}