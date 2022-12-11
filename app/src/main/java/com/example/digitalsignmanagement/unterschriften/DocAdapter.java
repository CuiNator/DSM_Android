package com.example.digitalsignmanagement.unterschriften;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.ScrollingActivity;

import java.util.ArrayList;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Document> documents;
    private static int checkedPosition = 0;

    public DocAdapter(ArrayList<Document> signs) {
        this.documents = signs;
    }

    private static ClickListener clickListener;

    public DocAdapter(ScrollingActivity scrollingActivity, ArrayList<Document> documents) {
        this.context = scrollingActivity;
        this.documents = documents;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unterschrift, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Document sign = documents.get(position);
        holder.bind(documents.get(position));
        holder.name.setText(sign.getDocumentName());
        holder.ersteller.setText(sign.getCreator().getName());
        holder.datum.setText(sign.getUploadDate());

        if(checkedPosition == position){
            System.out.println(sign.getCreator().getName());
        }

        System.out.println(holder.name.toString() + holder.ersteller + holder.datum);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Springt er hier rein ?");

            }
        });
    }

    public void setDocument(ArrayList<Document> documents){
        this.documents = new ArrayList<>();
        this.documents = documents;
    }
    @Override
    public int getItemCount() {
        if (documents != null) {
            return documents.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final View view;
        public final TextView name;
        public final TextView ersteller;
        public final TextView datum;
        private ConstraintLayout rowItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            name = view.findViewById(R.id.name);
            ersteller = view.findViewById(R.id.ersteller);
            datum = view.findViewById(R.id.datum);
//            this.rowItem = view.findViewById(R.id.linearLayout2);
//            rowItem.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    setSingleSelection(getAdapterPosition());
//                }
//            });
        }
//        private void setSingleSelection(int adapterPosition){
//            if(adapterPosition == RecyclerView.NO_POSITION) return;
//            notifyItemChanged(checkedPosition);
//            checkedPosition = adapterPosition;
//            notifyItemChanged(checkedPosition);
//        }

        void bind(final Document document){
            if(checkedPosition == -2){
                //name.setVisibility(View.GONE);
            }
            else{
                if(checkedPosition == getAdapterPosition()){
                    //name.setVisibility(View.VISIBLE);
                }
                else{
                    //name.setVisibility(View.GONE);
                }
            }
            datum.setText(document.getCreator().getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //name.setVisibility(View.VISIBLE);
                    if(checkedPosition != getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
            System.out.println("onClick");
            System.out.println("onClick");
            System.out.println(this.ersteller.toString());
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            System.out.println("onLongClick");
            System.out.println("onLongClick");
            System.out.println(this.ersteller.toString());
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

    public Document getSelected(){
        if(checkedPosition != -1){
            System.out.println(checkedPosition);
            return documents.get(checkedPosition);
        }
        return null;
    }
}

