package com.example.digitalsignmanagement.unterschriften;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.R;

import java.util.ArrayList;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.ViewHolder> {

    private ArrayList<Sign> signs;

    public SignAdapter(ArrayList<Sign> signs) {
        this.signs = signs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unterschrift, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sign sign = signs.get(position);

        holder.name.setText(sign.getName());
        holder.ersteller.setText(sign.getErsteller());
        holder.datum.setText(sign.getDatum());
    }

    @Override
    public int getItemCount() {
        if (signs != null) {
            return signs.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView ersteller;
        public final TextView datum;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            ersteller = view.findViewById(R.id.ersteller);
            datum = view.findViewById(R.id.datum);
        }
    }
}


