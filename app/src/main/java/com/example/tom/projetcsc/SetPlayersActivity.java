package com.example.tom.projetcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class SetPlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_players);

        TextView tvNewPlayer = (TextView)findViewById(R.id.newPlayerEditText);
        Button btnSubmit = (Button)findViewById(R.id.btnSubmitNewPlayer);
        Button btnNoMorePlayer = (Button)findViewById(R.id.btnNoMorePlayers);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvNewPlayer.getText().length()<1) {
                    Toast.makeText(getApplicationContext(), "No name written", Toast.LENGTH_SHORT).show();
                } else if(tvNewPlayer.getText().length()>12) {
                    Toast.makeText(getApplicationContext(), "Shorten your name please", Toast.LENGTH_SHORT).show();
                }
                else{
                    String newPlayerName = tvNewPlayer.getText().toString();

                    //Test if another player already has the same name
                    String[] playerListNames = PlayerList.getPlayerNames();
                    for(int i=0; i<playerListNames.length; i++){
                        if (playerListNames[i].equalsIgnoreCase(newPlayerName)) {
                            Toast.makeText(getApplicationContext(), newPlayerName + " is already playing", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    PlayerList.addPlayer(new Player(newPlayerName));

                    setResult(RESULT_OK, new Intent().putExtra("newPlayerAdded", true));
                    finish();
                }
            }
        });

        btnNoMorePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra("newPlayerAdded", false));
                finish();
            }
        });

    }




}