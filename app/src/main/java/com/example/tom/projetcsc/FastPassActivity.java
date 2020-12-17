package com.example.tom.projetcsc;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

public class FastPassActivity extends AppCompatActivity {

    public FastPassActivity activity;
    public int countDownLength = 4000;
    public SensorManager manager;
    public SensorEventListener sel;
    private boolean toolbarDisabled = true;


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
                    AlertDialog.Builder builder = new AlertDialog.Builder(FastPassActivity.this);
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
        setContentView(R.layout.activity_fast_pass);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        activity=this;
        TextView tvFastPassTarget = (TextView)findViewById(R.id.tvFastPassTarget);
        TextView tvFastPassCountdown = (TextView)findViewById(R.id.tvFastPassCountdown);

        Player narrator = PlayerList.currentNarrator;
        Player target = PlayerList.getRandomPlayerDifferentFrom(PlayerList.currentNarrator);

        tvFastPassTarget.setText("Give me to " + target.toString());

        PlayerList.changeNarrator(PlayerList.currentNarrator, target);

        LinearLayout lyFastPassLayout = findViewById(R.id.lyFastPass);
        Button btnFastPassNext = (Button)findViewById(R.id.btnFastPassNext);
        btnFastPassNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        DecimalFormat df = new DecimalFormat(("0.00"));
        CountDownTimer cdt = new CountDownTimer(countDownLength, 10){
            public void onTick(long millisUntilFinished){
                String goodFormat = df.format(((float)millisUntilFinished)/1000);
                tvFastPassCountdown.setText(goodFormat);
            }
            public void onFinish(){
                //Toast.makeText(getApplicationContext(), "Too slow!!", Toast.LENGTH_SHORT).show();
                //Toast.makeText(activity, narrator + " you lose 2 points", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " & " + target + " you lose 2 points", Toast.LENGTH_SHORT).show();
                narrator.score-=2;
                target.score-=2;
                toolbarDisabled=false;
                lyFastPassLayout.setVisibility(View.GONE);
            }
        }.start();

        Button btnFastPassFinish = (Button)findViewById(R.id.btnFastPassFinish);
        btnFastPassFinish.setText("I am "+target);
        btnFastPassFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Well done!!", Toast.LENGTH_SHORT).show();
                //Toast.makeText(activity, narrator + " you win 2 points", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " & " + target + " you win 2 points", Toast.LENGTH_SHORT).show();
                target.score+=2;
                narrator.score+=2;
                cdt.cancel();
                toolbarDisabled=false;
                lyFastPassLayout.setVisibility(View.GONE);
            }
        });

    }
}