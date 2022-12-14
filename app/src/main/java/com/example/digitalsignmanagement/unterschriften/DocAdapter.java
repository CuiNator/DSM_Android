package com.example.digitalsignmanagement.unterschriften;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.ScrollingActivity;
import com.example.digitalsignmanagement.activity_sign;

import java.util.ArrayList;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Document> documents;
    private int checkedPosition = 0;

    public DocAdapter(Context  context, ArrayList<Document> documents) {
        this.context = context;
        this.documents = documents;
    }



    public void setDocument(ArrayList<Document> documents){
        this.documents = new ArrayList<>();
        this.documents = documents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_unterschrift, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("Click");
        //Document sign = documents.get(position);
        holder.bind(documents.get(position));
//        holder.name.setText(sign.getDocumentName());
//        holder.ersteller.setText(sign.getCreator().getName());
//        holder.datum.setText(sign.getUploadDate());
//
//        if(checkedPosition == position){
//            System.out.println(sign.getCreator().getName());
//        }

    }


    @Override
    public int getItemCount() {
        if (documents != null) {
            return documents.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final TextView name;
        public final TextView ersteller;
        public final TextView datum;
        public final TextView id;
        public Button btn;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            ersteller = view.findViewById(R.id.ersteller);
            datum = view.findViewById(R.id.datum);
            btn = view.findViewById(R.id.itemSign);
            btn.setOnClickListener(this);
            id = view.findViewById(R.id.docId);
        }

        void bind(final Document document){
            name.setText(document.getDocumentName());
            ersteller.setText(document.getCreator().getName());
            datum.setText(document.getUploadDate());
            String docId = String.valueOf(document.getDocumentId());
            id.setText(docId);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //name.setVisibility(View.VISIBLE);
                    System.out.println("clicki");
                    if(checkedPosition != getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
        @Override
        public void onClick(View v){
            if(v.getId() == btn.getId()){
                System.out.println("Button pressed "+this.name.getText().toString());
                Context context = view.getContext();
                Helper.insertDocData(context,this.name.getText().toString(),this.id.getText().toString());
                Intent intent1 = new Intent(v.getContext(), activity_sign.class);
                context.startActivity(intent1);

            }
        }
    }






    public Document getSelected(){
        if(checkedPosition != -1){
            System.out.println(checkedPosition);
            return documents.get(checkedPosition);
        }
        return null;
    }
}

