package com.example.frontend;// UserViewHolder.java

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    public ImageView userImage;
    public TextView userName;

    public PlayerViewHolder(View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.user_image);
        userName = itemView.findViewById(R.id.user_name);
    }
}
