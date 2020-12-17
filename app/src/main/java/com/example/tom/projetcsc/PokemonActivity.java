package com.example.tom.projetcsc;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class PokemonActivity extends AppCompatActivity {

    public String curPokemonName;
    public int pokemonGuessed = 1;

    private Toolbar toolbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.pause:
                Intent pause = new Intent(getApplicationContext(), PauseActivity.class);
                startActivity(pause);
                break;
            case R.id.scores:
                Intent launchScores = new Intent(getApplicationContext(), ScoresActivity.class);
                startActivity(launchScores);
                break;
            case R.id.options:
                Intent goToPrefs = new Intent(getApplicationContext(), GamePreferences.class);
                startActivity(goToPrefs);
                break;
            case R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(PokemonActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to quit?");
                builder.setMessage("That will erase every score");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent backHome = new Intent(getApplicationContext(), MainActivity.class);
                                backHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(backHome);
                                Toast.makeText(getApplicationContext(), "Let's start another game!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        Button btnShowPokemonName = (Button)findViewById(R.id.btnShowPokemonName);
        TextView tvName = (TextView)findViewById(R.id.tvPokemonName);
        Button btnRightPokemon = (Button)findViewById(R.id.btnRightPokemon);
        Button btnWrongPokemon = (Button)findViewById(R.id.btnWrongPokemon);
        LinearLayout lyVerifyPokemonAnswer = (LinearLayout)findViewById(R.id.lyVerifyPokemonAnswer);

        lyVerifyPokemonAnswer.setVisibility(View.GONE);

        Player narrator = PlayerList.currentNarrator;

        LinearLayout lyPokemon = findViewById(R.id.lyPokemon);
        Button btnPokemonNext = (Button)findViewById(R.id.btnPokemonNext);
        btnPokemonNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });

        btnRightPokemon.setOnClickListener(new GetActivityOnClickListener(this){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "Well done!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " you win 1 point", Toast.LENGTH_LONG).show();
                narrator.score+=1;

                tvName.setText("");
                AsyncGetPokemonJSON asyncGPJSON = new AsyncGetPokemonJSON((PokemonActivity)this.activity);
                int rd = ThreadLocalRandom.current().nextInt(1,152);
                asyncGPJSON.execute(Integer.toString(rd));
                pokemonGuessed+=1;
                if (pokemonGuessed == 5){
                    lyPokemon.setVisibility(View.GONE);
                }
                lyVerifyPokemonAnswer.setVisibility(View.GONE);
            }
        });

        btnWrongPokemon.setOnClickListener(new GetActivityOnClickListener(this){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "Well done!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " you lose 1 point", Toast.LENGTH_LONG).show();
                narrator.score-=1;

                tvName.setText("");
                AsyncGetPokemonJSON asyncGPJSON = new AsyncGetPokemonJSON((PokemonActivity)this.activity);
                int rd = ThreadLocalRandom.current().nextInt(1,152);
                asyncGPJSON.execute(Integer.toString(rd));
                pokemonGuessed+=1;
                if (pokemonGuessed == 5){
                    lyPokemon.setVisibility(View.GONE);
                }
                lyVerifyPokemonAnswer.setVisibility(View.GONE);
            }
        });

        btnShowPokemonName.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                if (curPokemonName==null) {
                    Toast.makeText(activity, "Please wait", Toast.LENGTH_LONG).show();
                }else {
                    tvName.setText("Answer : " + curPokemonName);
                    lyVerifyPokemonAnswer.setVisibility(View.VISIBLE);
                }
            }
        });

        AsyncGetPokemonJSON asyncGPJSON = new AsyncGetPokemonJSON(this);
        int rd = ThreadLocalRandom.current().nextInt(1,152);
        asyncGPJSON.execute(Integer.toString(rd));

    }
}