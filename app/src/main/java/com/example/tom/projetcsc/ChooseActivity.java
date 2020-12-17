package com.example.tom.projetcsc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseActivity extends AppCompatActivity {

    private CheckBox first;
    private CheckBox second;
    private CheckBox third;
    private CheckBox fourth;
    private CheckBox fifth;
    private CheckBox sixth;
    private CheckBox seventh;
    private CheckBox eighth;
    private Player chosenPlayer = null;
    private ArrayList<Player> playerList;
    private Player narrator;
    private LinearLayout lyChoose;

    private boolean toolbarDisabled = false;
    private Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toolbarDisabled){
            Toast.makeText(getApplicationContext(), "Toolbar disabled for now", Toast.LENGTH_SHORT).show();
        }else {
            switch (item.getItemId()) {
                case R.id.pause:
                    Intent launchPause = new Intent(getApplicationContext(), PauseActivity.class);
                    startActivity(launchPause);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseActivity.this);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        narrator=PlayerList.currentNarrator;
        playerList = PlayerList.getPlayerListExcept(narrator);

        lyChoose = findViewById(R.id.lyChoose);
        Button btnPokemonNext = (Button)findViewById(R.id.btnChooseNext);
        btnPokemonNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });

        TextView tvChooseCriteria = (TextView)findViewById(R.id.tvChooseCriteria);

        int question = ThreadLocalRandom.current().nextInt(0,5);

        switch (question){
            case 0:
                tvChooseCriteria.setText("Choose the player you like the most as the next narrator");
                break;
            case 1:
                tvChooseCriteria.setText("Choose the player that smiles the most as the next narrator");
                break;
            case 2:
                tvChooseCriteria.setText("Choose the player you secretly like the as the next narrator");
                break;
            case 3:
                tvChooseCriteria.setText("Choose the player that never makes you laugh as the next narrator");
                break;
            case 4:
                tvChooseCriteria.setText("Choose the younger player as the next narrator");
                break;
        }


        for(int i=0; i<playerList.size(); i++){
            switch(i) {
                case 0:
                    first = (CheckBox)findViewById(R.id.checkbox1);
                    first.setText(playerList.get(0).name);
                    break;
                case 1:
                    second = (CheckBox)findViewById(R.id.checkbox2);
                    second.setText(playerList.get(1).name);
                    break;
                case 2:
                    third = (CheckBox)findViewById(R.id.checkbox3);
                    third.setText(playerList.get(2).name);
                    break;
                case 3:
                    fourth = (CheckBox)findViewById(R.id.checkbox4);
                    fourth.setText(playerList.get(3).name);
                    break;
                case 4:
                    fifth = (CheckBox)findViewById(R.id.checkbox5);
                    fifth.setText(playerList.get(4).name);
                    break;
                case 5:
                    sixth = (CheckBox)findViewById(R.id.checkbox6);
                    sixth.setText(playerList.get(5).name);
                    break;
                case 6:
                    seventh = (CheckBox)findViewById(R.id.checkbox7);
                    seventh.setText(playerList.get(6).name);
                    break;
            }
        }
        for(int i=playerList.size();i<8;i++){
            switch(i) {
                case 2:
                    third = (CheckBox)findViewById(R.id.checkbox3);
                    third.setVisibility(View.GONE);
                    break;
                case 3:
                    fourth = (CheckBox)findViewById(R.id.checkbox4);
                    fourth.setVisibility(View.GONE);
                    break;
                case 4:
                    fifth = (CheckBox)findViewById(R.id.checkbox5);
                    fifth.setVisibility(View.GONE);
                    break;
                case 5:
                    sixth = (CheckBox)findViewById(R.id.checkbox6);
                    sixth.setVisibility(View.GONE);
                    break;
                case 6:
                    seventh = (CheckBox)findViewById(R.id.checkbox7);
                    seventh.setVisibility(View.GONE);
                    break;
            }
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox)view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox1:
                if (checked) {
                    chosenPlayer=playerList.get(0);
                    endGame();
                }
                break;
            case R.id.checkbox2:
                if (checked) {
                    chosenPlayer=playerList.get(1);
                    endGame();
                }
                break;
            case R.id.checkbox3:
                if (checked) {
                    chosenPlayer=playerList.get(2);
                    endGame();
                }
                break;
            case R.id.checkbox4:
                if (checked) {
                    chosenPlayer=playerList.get(3);
                    endGame();
                }
                break;
            case R.id.checkbox5:
                if (checked) {
                    chosenPlayer=playerList.get(4);
                    endGame();
                }
                break;
            case R.id.checkbox6:
                if (checked) {
                    chosenPlayer=playerList.get(5);
                    endGame();
                }
                break;
            case R.id.checkbox7:
                if (checked) {
                    chosenPlayer=playerList.get(6);
                }
                break;
        }
    }

    public void endGame(){
        Toast.makeText(this, chosenPlayer + " win 3 points", Toast.LENGTH_SHORT).show();
        chosenPlayer.score +=3;
        PlayerList.changeNarrator(narrator, chosenPlayer);
        lyChoose.setVisibility(View.GONE);
    }
}