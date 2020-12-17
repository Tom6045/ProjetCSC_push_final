package com.example.tom.projetcsc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ScoresActivity extends AppCompatActivity {

    private RecyclerView rv;
    private int[] range = {R.drawable.ic_first, R.drawable.ic_second, R.drawable.ic_third,
            R.drawable.ic_fourth, R.drawable.ic_fifth, R.drawable.ic_sixth, R.drawable.ic_seventh, R.drawable.ic_eighth};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new RVAdapter(range));
    }
}