package com.example.tom.projetcsc;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivPlayerRange;
    public TextView tvPlayerName;
    public TextView tvPlayerScore;

    public ImageViewHolder( View itemView) {
        super(itemView);
        ivPlayerRange = itemView.findViewById(R.id.playerRange);
        tvPlayerName = itemView.findViewById(R.id.playerName);
        tvPlayerScore = itemView.findViewById(R.id.playerScore);
    }

}
