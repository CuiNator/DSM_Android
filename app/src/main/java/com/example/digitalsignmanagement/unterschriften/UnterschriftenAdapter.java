package com.example.digitalsignmanagement.unterschriften;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.R;

import java.util.ArrayList;

public class UnterschriftenAdapter extends RecyclerView.Adapter<UnterschriftenAdapter.ViewHolder> {

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView description;
        public final ImageView image;



        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.textView);
            description = view.findViewById(R.id.textView2);
            image = view.findViewById(R.id.imageView2);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

            return new ViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Unterschriften unterschriften = unterschriften.get(position);

            holder.name.setText(unterschriften.getName());
            holder.description.setText(unterschriften.getErsteller());
        }
}

