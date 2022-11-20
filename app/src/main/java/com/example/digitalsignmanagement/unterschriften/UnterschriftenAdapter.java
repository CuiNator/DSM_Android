package com.example.digitalsignmanagement.unterschriften;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UnterschriftenAdapter extends RecyclerView.Adapter {

    private ArrayList<Unterschriften> unterschriften;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (unterschriften != null){
            return unterschriften.size();}
        else{
        return 0;}
    }
}

