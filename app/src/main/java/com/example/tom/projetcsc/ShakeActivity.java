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

public class ShakeActivity extends AppCompatActivity {

    public double accLimit = 75;
    public ShakeActivity activity;
    public SensorManager manager;
    public SensorEventListener sel;
    public int countDownLength = 2000;
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShakeActivity.this);
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
        setContentView(R.layout.activity_shake);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        Player narrator = PlayerList.currentNarrator;

        activity=this;

        LinearLayout lyShake = findViewById(R.id.lyShake);
        Button btnShakeNext = (Button)findViewById(R.id.btnShakeNext);
        btnShakeNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });


        TextView tvShakeCountdown = (TextView)findViewById(R.id.tvShakeCountdown);

        DecimalFormat df = new DecimalFormat(("0.00"));
        CountDownTimer cdt = new CountDownTimer(countDownLength, 10){
            public void onTick(long millisUntilFinished){
                String goodFormat = df.format(((float)millisUntilFinished)/1000);
                tvShakeCountdown.setText(goodFormat);
            }
            public void onFinish(){
                //Toast.makeText(getApplicationContext(), "Too slow!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " you lose 2 points", Toast.LENGTH_SHORT).show();
                narrator.score-=2;
                manager.unregisterListener(sel ,manager.getDefaultSensor(Sensor.TYPE_LIGHT) );
                toolbarDisabled=false;
                lyShake.setVisibility(View.GONE);
            }
        }.start();

        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sel = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
                    if(event.values[0]>accLimit || event.values[1]>accLimit || event.values[2]>accLimit){
                        cdt.cancel();
                        //Toast.makeText(getApplicationContext(), "You shake it!!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity, narrator + " you win 2 points", Toast.LENGTH_SHORT).show();
                        narrator.score+=2;
                        manager.unregisterListener(this ,manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) );
                        toolbarDisabled=false;
                        lyShake.setVisibility(View.GONE);
                    }
                }
            }
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i("listener","accuracy changed");
            }
        };

        manager.registerListener(sel, manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_UI );
    }
}