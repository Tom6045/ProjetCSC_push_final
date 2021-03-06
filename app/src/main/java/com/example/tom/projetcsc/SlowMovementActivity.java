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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

public class SlowMovementActivity extends AppCompatActivity {

    private boolean ready = false;
    public double accLimit = 1.5;
    public SlowMovementActivity activity;
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(SlowMovementActivity.this);
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
        setContentView(R.layout.activity_slow_movement);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        activity=this;

        LinearLayout lySlowMovement = findViewById(R.id.lySlowMovement);
        Button btnSlowMovementNext = (Button)findViewById(R.id.btnSlowMovementNext);
        btnSlowMovementNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });

        TextView tvSlowMovementTarget = (TextView)findViewById(R.id.tvSlowMovementTarget);

        Player narrator = PlayerList.currentNarrator;
        Player target = PlayerList.getRandomPlayerDifferentFrom(PlayerList.currentNarrator);

        tvSlowMovementTarget.setText("Give me to " + target.toString());

        PlayerList.changeNarrator(PlayerList.currentNarrator, target);

        SensorManager manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        SensorEventListener sel = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && ready){
                    if(event.values[0]>accLimit || event.values[1]>accLimit || event.values[2]>accLimit){
                        //Toast.makeText(getApplicationContext(), "You were too fast!!", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(activity, narrator + " you lose 2 points", Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity, narrator + " & " + target + " you lose 2 points", Toast.LENGTH_SHORT).show();
                        narrator.score-=2;
                        target.score-=2;
                        manager.unregisterListener(this ,manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) );
                        toolbarDisabled=false;
                        lySlowMovement.setVisibility(View.GONE);
                    }
                }
            }
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i("listener","accuracy changed");
            }
        };

        manager.registerListener(sel, manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_UI );


        Button btnSlowMovementFinish = (Button)findViewById(R.id.btnSlowMovementFinish);
        btnSlowMovementFinish.setVisibility(View.INVISIBLE);
        btnSlowMovementFinish.setText("I am "+target);
        btnSlowMovementFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Well done!!", Toast.LENGTH_SHORT).show();
                //Toast.makeText(activity, narrator + " you win 2 points", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " & " + target + " you win 2 points", Toast.LENGTH_SHORT).show();
                narrator.score+=2;
                target.score+=2;
                manager.unregisterListener(sel ,manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) );
                toolbarDisabled=false;
                lySlowMovement.setVisibility(View.GONE);
            }
        });

        TextView tvSlowMovementCountdown = (TextView)findViewById(R.id.tvSlowMovementCountdown);
        TextView tvSlowMovementReady = (TextView)findViewById(R.id.tvSlowMovementReady);

        DecimalFormat df = new DecimalFormat(("0.00"));
        CountDownTimer cdt = new CountDownTimer(2000, 10){
            public void onTick(long millisUntilFinished){
                String goodFormat = df.format(((float)millisUntilFinished)/1000);
                tvSlowMovementCountdown.setText(goodFormat);
            }
            public void onFinish(){
                ready = true;
                tvSlowMovementCountdown.setVisibility(View.GONE);
                tvSlowMovementReady.setVisibility(View.GONE);
                btnSlowMovementFinish.setVisibility(View.VISIBLE);
            }
        }.start();

    }
}