package com.example.tom.projetcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PauseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        Button btnPause = (Button)findViewById(R.id.btnResume);

        btnPause.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v){
                this.activity.finish();
            }
        });
    }
}