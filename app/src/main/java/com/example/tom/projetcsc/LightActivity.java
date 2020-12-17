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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class LightActivity extends AppCompatActivity {

    public int highBrightness = 1800;
    public float lowBrightness = (float)1.1;
    public LightActivity activity;
    public int countDownLength;
    private SensorManager manager;
    private SensorEventListener sel;
    private Toolbar toolbar;
    private boolean toolbarDisabled = true;

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(LightActivity.this);
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
        setContentView(R.layout.activity_light);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        LinearLayout lyLight = findViewById(R.id.lyLight);
        Button btnLightNext = (Button)findViewById(R.id.btnLightNext);
        btnLightNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });

        activity=this;

        TextView tvLightOrDark = (TextView)findViewById(R.id.tvLightOrDark);

        int rd = ThreadLocalRandom.current().nextInt(0,2);

        if (rd==0){
            tvLightOrDark.setText("Put me in the light!");
            countDownLength = 5000;
        }else{
            tvLightOrDark.setText("Put me in the dark!");
            countDownLength = 3000;
        }


        TextView tvLightCountdown = (TextView)findViewById(R.id.tvLightCountdown);

        DecimalFormat df = new DecimalFormat(("0.00"));
        CountDownTimer cdt = new CountDownTimer(countDownLength, 10){
            public void onTick(long millisUntilFinished){
                String goodFormat = df.format(((float)millisUntilFinished)/1000);
                tvLightCountdown.setText(goodFormat);
            }
            public void onFinish(){
                PlayerList.currentNarrator.score-=2;
                //Toast.makeText(getApplicationContext(), "Too slow!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, PlayerList.currentNarrator + " you lose 2 points", Toast.LENGTH_LONG).show();
                manager.unregisterListener(sel ,manager.getDefaultSensor(Sensor.TYPE_LIGHT) );
                toolbarDisabled=false;
                lyLight.setVisibility(View.GONE);
            }
        }.start();

        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sel = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_LIGHT){
                    float lightLevel = event.values[0];
                    if(lightLevel>highBrightness && rd==0){
                        cdt.cancel();
                        //Toast.makeText(getApplicationContext(), "Well that was bright!!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity, PlayerList.currentNarrator + " you win 2 points", Toast.LENGTH_LONG).show();
                        PlayerList.currentNarrator.score+=2;
                        manager.unregisterListener(this ,manager.getDefaultSensor(Sensor.TYPE_LIGHT) );
                        toolbarDisabled=false;
                        lyLight.setVisibility(View.GONE);
                    }else if(lightLevel<lowBrightness && rd==1){
                        cdt.cancel();
                        //Toast.makeText(getApplicationContext(), "Well that was dark!!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity, PlayerList.currentNarrator + " you win 2 points", Toast.LENGTH_LONG).show();
                        PlayerList.currentNarrator.score+=2;
                        manager.unregisterListener(this ,manager.getDefaultSensor(Sensor.TYPE_LIGHT) );
                        toolbarDisabled=false;
                        lyLight.setVisibility(View.GONE);
                    }
                }
            }
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i("listener","accuracy changed");
            }
        };
        manager.registerListener(sel, manager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_UI );
    }
}