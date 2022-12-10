package com.example.digitalsignmanagement.unterschriften;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.R;

import java.util.ArrayList;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.ViewHolder> {

    private ArrayList<Document> signs;

    public DocAdapter(ArrayList<Document> signs) {
        this.signs = signs;
    }

    private static ClickListener clickListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unterschrift, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Document sign = signs.get(position);

        holder.name.setText(sign.getDocumentName());
        holder.ersteller.setText(sign.getStatus());
        holder.datum.setText(sign.getUploadDate());
    }

    @Override
    public int getItemCount() {
        if (signs != null) {
            return signs.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final View view;
        public final TextView name;
        public final TextView ersteller;
        public final TextView datum;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            name = view.findViewById(R.id.name);
            ersteller = view.findViewById(R.id.ersteller);
            datum = view.findViewById(R.id.datum);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            DocAdapter.clickListener = clickListener;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);
            void onItemLongClick(int position, View v);
        }
}

