package com.example.tom.projetcsc;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class RVAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private int[] images;

    public RVAdapter(int[] images) {
        this.images = images;
    }


    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View view = lf.inflate(R.layout.item_layout, parent, false);
        ImageViewHolder ivh = new ImageViewHolder(view);
        return ivh;
    }

    public void onBindViewHolder(ImageViewHolder holder, int position) {
        int image_id = images[position];
        holder.ivPlayerRange.setImageResource(image_id);
        holder.tvPlayerName.setText(PlayerList.getPlayerScoresOrdered()[position][0]);
        holder.tvPlayerScore.setText(PlayerList.getPlayerScoresOrdered()[position][1]);
    }

    public int getItemCount() {
        return PlayerList.getPlayerList().size();
    }
}
