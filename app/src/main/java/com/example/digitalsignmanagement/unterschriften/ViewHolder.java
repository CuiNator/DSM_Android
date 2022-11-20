package com.example.digitalsignmanagement.unterschriften;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.R;

public static class ViewHolder extends RecyclerView.ViewHolder {
    public final View view;
    public final TextView name;
    public final TextView description;
    public final ImageView image;



    public ViewHolder(View view) {
        super(view);
        this.view = view;
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        image = view.findViewById(R.id.image);
    }
}
