package com.example.tom.projetcsc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class WeatherActivity extends AppCompatActivity {

    public String curWeatherName=null;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
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
        setContentView(R.layout.activity_weather);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        Button btnShowWeatherName = (Button)findViewById(R.id.btnShowWeatherName);
        TextView tvWeatherTemp = (TextView)findViewById(R.id.tvWeatherTemp);
        Button btnRightWeather = (Button)findViewById(R.id.btnRightWeather);
        Button btnWrongWeather = (Button)findViewById(R.id.btnWrongWeather);
        LinearLayout lyVerifyWeatherAnswer = (LinearLayout)findViewById(R.id.lyVerifyWeatherAnswer);
        ImageView ivWeather = (ImageView)findViewById(R.id.ivWeather);
        ivWeather.setVisibility(View.INVISIBLE);

        lyVerifyWeatherAnswer.setVisibility(View.GONE);

        Player narrator = PlayerList.currentNarrator;

        LinearLayout lyWeather = findViewById(R.id.lyWeather);
        Button btnWeatherNext = (Button)findViewById(R.id.btnWeatherNext);
        btnWeatherNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });

        btnRightWeather.setOnClickListener(new GetActivityOnClickListener(this){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "Well done!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " you win 3 point", Toast.LENGTH_LONG).show();
                narrator.score+=1;
                lyWeather.setVisibility(View.GONE);
            }
        });

        btnWrongWeather.setOnClickListener(new GetActivityOnClickListener(this){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "Well done!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " you lose 2 point", Toast.LENGTH_LONG).show();
                narrator.score-=1;
                lyWeather.setVisibility(View.GONE);
            }
        });

        btnShowWeatherName.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                if (curWeatherName==null) {
                    Toast.makeText(activity, "Please wait", Toast.LENGTH_LONG).show();
                }else {
                    tvWeatherTemp.setText("Temperature : " + curWeatherName);
                    lyVerifyWeatherAnswer.setVisibility(View.VISIBLE);
                    ivWeather.setVisibility(View.VISIBLE);
                }
            }
        });

        AsyncGetWeatherJSON asyncGetWeatherJSON = new AsyncGetWeatherJSON(this);
        asyncGetWeatherJSON.execute("https://www.metaweather.com/api/location/615702/");

    }
}