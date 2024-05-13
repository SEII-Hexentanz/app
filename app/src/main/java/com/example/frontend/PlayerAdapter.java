package com.example.frontend;// UserAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerViewHolder> {
    private final List<Player> playerList;

    public PlayerAdapter(List<Player> playerList) {
        this.playerList = playerList;

    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.userName.setText(player.getUsername());
        holder.userImage.setImageResource(player.getImageResource());
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
