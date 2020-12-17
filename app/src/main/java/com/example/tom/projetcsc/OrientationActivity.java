package com.example.tom.projetcsc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class OrientationActivity extends AppCompatActivity {

    // SensorEvent.values[2] : +0.5 west    0 north    -0.5 east    -1/1 south
    // the phone should be well calibrated

    private boolean toolbarDisabled = true;
    private Toolbar toolbar;
    private OrientationActivity activity;
    private float currentValue = 0;
    private boolean takeAbsValue;
    private float valueToApproach;
    private int countDownLength = 8000;
    private float lastTimeNearValue;
    private float tolerance = (float)0.2;

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrientationActivity.this);
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
        setContentView(R.layout.activity_orientation);

        Player narrator = PlayerList.currentNarrator;


        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(narrator.name);
        setSupportActionBar(toolbar);


        activity=this;

        lastTimeNearValue = 0;

        TextView tvOrientationDirection = (TextView)findViewById(R.id.tvOrientationDirection);

        int orientation = ThreadLocalRandom.current().nextInt(0,4);
        takeAbsValue = false;
        switch(orientation){
            case 0:
                tvOrientationDirection.setText("Direct me to the West!");
                valueToApproach = (float)0.5;
                break;
            case 1:
                tvOrientationDirection.setText("Direct me to the North!");
                valueToApproach = 0;
                break;
            case 2:
                tvOrientationDirection.setText("Direct me to the East!");
                valueToApproach = (float)-0.5;
                break;
            case 3:
                tvOrientationDirection.setText("Direct me to the South!");
                takeAbsValue = true;
                valueToApproach = (float)1;
                break;
        }

        LinearLayout lyOrientation = findViewById(R.id.lyOrientation);
        Button btnOrientationNext = (Button)findViewById(R.id.btnOrientationNext);
        btnOrientationNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });


        SensorManager manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        SensorEventListener sel = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR){
                    currentValue = event.values[2];
                }
            }
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i("listener","accuracy changed");
            }
        };

        manager.registerListener(sel, manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_UI );

        TextView tvOrientationCountdown = (TextView)findViewById(R.id.tvOrientationCountdown);

        DecimalFormat df = new DecimalFormat(("0.00"));
        CountDownTimer cdt = new CountDownTimer(countDownLength, 10){
            public void onTick(long millisUntilFinished){
                String goodFormat = df.format(((float)millisUntilFinished)/1000);
                tvOrientationCountdown.setText(goodFormat);

                if(takeAbsValue){ //it is the cas for south
                    if(Math.abs(Math.abs(currentValue) - valueToApproach)<tolerance) { //the user is in the right direction
                        if (lastTimeNearValue == 0) {
                            Toast.makeText(activity, "good direction!!", Toast.LENGTH_SHORT).show();
                            lastTimeNearValue = (float) millisUntilFinished;
                        } else if (lastTimeNearValue - (float) millisUntilFinished > 2000) { //if the user was in the right direction for 2sec
                            Toast.makeText(activity, narrator + " you win 2 points", Toast.LENGTH_SHORT).show();
                            narrator.score+=2;
                            this.cancel();
                            manager.unregisterListener(sel ,manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR));
                            toolbarDisabled=false;
                            lyOrientation.setVisibility(View.GONE);
                        }
                    }else if (lastTimeNearValue != 0) { //the user is not in the right direction
                        Toast.makeText(activity, "wrong direction...", Toast.LENGTH_SHORT).show();
                        lastTimeNearValue = 0;  //reset
                    }

                }else{ //no need to take the abs value
                    if(Math.abs(currentValue - valueToApproach)<tolerance){
                        if (lastTimeNearValue == 0) {
                            Toast.makeText(activity, "good direction!!", Toast.LENGTH_SHORT).show();
                            lastTimeNearValue = (float) millisUntilFinished;
                        } else if (lastTimeNearValue - (float) millisUntilFinished > 2000) { //if the user was in the right direction for 2sec
                            Toast.makeText(activity, narrator + " you win 2 points", Toast.LENGTH_SHORT).show();
                            narrator.score+=2;
                            this.cancel();
                            manager.unregisterListener(sel ,manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR));
                            toolbarDisabled=false;
                            lyOrientation.setVisibility(View.GONE);
                        }
                    }else if (lastTimeNearValue != 0){
                        Toast.makeText(activity, "wrong direction...", Toast.LENGTH_SHORT).show();
                        lastTimeNearValue = 0;
                    }
                }
            }
            public void onFinish(){
                //Toast.makeText(getApplicationContext(), "Too slow!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " you lose 2 points", Toast.LENGTH_SHORT).show();
                narrator.score-=2;
                manager.unregisterListener(sel ,manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) );
                toolbarDisabled=false;
                lyOrientation.setVisibility(View.GONE);
            }
        }.start();

    }
}