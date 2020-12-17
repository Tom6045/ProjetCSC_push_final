package com.example.tom.projetcsc;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GetActivityOnClickListener implements View.OnClickListener {

    public AppCompatActivity activity;

    public GetActivityOnClickListener(AppCompatActivity activity){
        this.activity = activity;
    }


    @Override
    public void onClick(View v) {

    }
}
