package com.example.tom.projetcsc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class TruthDareActivity extends AppCompatActivity {

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
                AlertDialog.Builder builder = new AlertDialog.Builder(TruthDareActivity.this);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truth_dare);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        LinearLayout lyTruthDare = findViewById(R.id.lyTruthDare);
        Button btnTruthDareNext = (Button)findViewById(R.id.btnTruthDareNext);
        btnTruthDareNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });

        Player narrator = PlayerList.currentNarrator;
        Player target = PlayerList.getRandomPlayerDifferentFrom(narrator);

        TextView tvTruthDareNames = (TextView)findViewById(R.id.tvTruthDareNames);
        tvTruthDareNames.setText(narrator + ", you challenge " + target + " with a Truth or Dare");

        Button btnTruthDareAccepted = (Button)findViewById(R.id.btnTruthDareAccepted);
        Button btnTruthDareRefused = (Button)findViewById(R.id.btnTruthDareRefused);

        btnTruthDareAccepted.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Well done!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, target + " you win 2 points & " + narrator + " you lose 1 point", Toast.LENGTH_LONG).show();
                target.score+=2;
                narrator.score-=1;
                lyTruthDare.setVisibility(View.GONE);
            }
        });

        btnTruthDareRefused.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Oh no!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, target + " you lose 2 points", Toast.LENGTH_LONG).show();
                target.score-=2;
                lyTruthDare.setVisibility(View.GONE);
            }
        });
    }
}