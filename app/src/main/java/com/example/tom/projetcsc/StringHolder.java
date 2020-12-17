package com.example.tom.projetcsc;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class StringHolder extends RecyclerView.ViewHolder {
    private TextView v_;
    public StringHolder(TextView v) {
        super(v);
        v_ = v;
    }
    public void setText(String text) {
        v_.setText(text);
    }
}