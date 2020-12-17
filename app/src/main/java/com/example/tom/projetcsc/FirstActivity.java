package com.example.tom.projetcsc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        TextView tvFirstActivityNarrator = (TextView)findViewById(R.id.tvFirstActivityNarrator);
        tvFirstActivityNarrator.setText(PlayerList.currentNarrator.name);

        Button btnFirstActivityConfirm = (Button)findViewById(R.id.btnFirstActivityConfirm);
        btnFirstActivityConfirm.setText("I am "+PlayerList.currentNarrator.name);
        btnFirstActivityConfirm.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });
    }
}