package com.example.digitalsignmanagement.scrollingActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.activity_sign;

import java.text.SimpleDateFormat;
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
        holder.bind(documents.get(position));

    }


    @Override
    public int getItemCount() {
        if (documents != null) {
            return documents.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public final View view;
        public final TextView name;
        public final TextView ersteller;
        public final TextView datum;
        public final TextView id;
        public final TextView status;
        public final TextView gezeichnet;
        public Button intern;
        public Button extern;
        public ExternalSigners externalSigners[];


        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            ersteller = view.findViewById(R.id.ersteller);
            datum = view.findViewById(R.id.datum);
            status = view.findViewById(R.id.status);
            intern = view.findViewById(R.id.itemSign);
            intern.setOnClickListener(this);
            extern = view.findViewById(R.id.signExtern);
            extern.setOnClickListener(this);
            id = view.findViewById(R.id.docId);
            gezeichnet = view.findViewById(R.id.gezeichnet);

            
        }

        void bind(final Document document){
            name.setText(document.getName());
            ersteller.setText(document.getCreatorName());

            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy");
            datum.setText(formatter.format(document.getCreationDate()));
            status.setText(document.getStatus());

            String anzahl = document.getReceivedSignatures()+"/"+document.getMaxSigns();
            gezeichnet.setText(anzahl);
            if (document.getMaxSigns() == document.getReceivedSignatures()){
                intern.setActivated(false);
                extern.setActivated(false);
            }
            
            String docId = String.valueOf(document.getDocumentId());
            id.setText(docId);
            externalSigners = document.getExternalSigners();
        }
        @Override
        public void onClick(View v){
            if(v.getId() == intern.getId()) {
                System.out.println("Button pressed " + this.name.getText().toString());
                Context context = view.getContext();
                Helper.insertDocData(context, this.name.getText().toString(), this.id.getText().toString());
                Intent intent1 = new Intent(v.getContext(), activity_sign.class);
                context.startActivity(intent1);
            }

            if(v.getId() == extern.getId()){

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(v.getContext());
                builderSingle.setTitle("Select external signer:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.select_dialog_singlechoice);
                for(int i = 0; i < externalSigners.length; i++){
                    arrayAdapter.add(externalSigners[i].getName());
                }
//                arrayAdapter.add("Hardik");
//                arrayAdapter.add("Archit");
//                arrayAdapter.add("Jignesh");
//                arrayAdapter.add("Umang");
//                arrayAdapter.add("Gatti");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        String persId =null;
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(v.getContext());
                        for(int i = 0; i < externalSigners.length; i++){
                            if (strName == externalSigners[i].getName())
                                {
                                    persId = String.valueOf(externalSigners[i].getPersonId());
                                }
                        }
                        Helper.insertDocData(context, strName, persId);
                        Intent intent1 = new Intent(v.getContext(), activity_sign.class);
                        context.startActivity(intent1);

//                        builderInner.setMessage(strName);
//                        builderInner.setTitle("Your Selected Item is");
//                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        builderInner.show();
                    }
                });
                builderSingle.show();


            }
        }
    }

}

