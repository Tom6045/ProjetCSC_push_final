package com.example.tom.projetcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.ThreadLocalRandom;

public class MovementActivity extends AppCompatActivity {

    public float maxX=0;
    public float maxY=0;
    public float maxZ=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement);


        TextView tvXAcc = (TextView)findViewById(R.id.tvXAcc);
        TextView tvYAcc = (TextView)findViewById(R.id.tvYAcc);
        TextView tvZAcc = (TextView)findViewById(R.id.tvZAcc);

        TextView tvMaxXAcc = (TextView)findViewById(R.id.tvMaxXAcc);
        TextView tvMaxYAcc = (TextView)findViewById(R.id.tvMaxYAcc);
        TextView tvMaxZAcc = (TextView)findViewById(R.id.tvMaxZAcc);


        SensorManager manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        SensorEventListener sel = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR){
                    float x,y,z;
                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];

                    tvXAcc.setText(x + "");
                    tvYAcc.setText(y + "");
                    tvZAcc.setText(z + "");

                    if(x>Math.abs(maxX)){
                        maxX = x;
                        tvMaxXAcc.setText("max x "+x);
                    }
                    if(y>Math.abs(maxY)){
                        maxY = y;
                        tvMaxYAcc.setText("max y "+y);
                    }
                    if(z>Math.abs(maxZ)){
                        maxZ = z;
                        tvMaxZAcc.setText("max z "+z);
                    }

                }
            }
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i("listener","accuracy changed");
            }
        };

        manager.registerListener(sel, manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_UI );


        Button btnStopAccelerometer = (Button)findViewById(R.id.btnStopAccelerometer);

        btnStopAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.unregisterListener(sel ,manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) );
            }
        });
    }
}