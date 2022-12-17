package com.example.digitalsignmanagement.unterschriften;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.ScrollingActivity;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public final View view;
        public final TextView name;
        public final TextView ersteller;
        public final TextView datum;
        public final TextView id;
        public final TextView status;
        public Button intern;
        public Button extern;

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
        }

        void bind(final Document document){
            name.setText(document.getName());
            ersteller.setText(document.getCreatorName());

            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy");
            datum.setText(formatter.format(document.getCreationDate()));
            status.setText(document.getStatus());

            String docId = String.valueOf(document.getDocumentId());
            id.setText(docId);

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
//                System.out.println("Button pressed Extern ");
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setTitle("URL Einrichten");
//                final Drop input = new EditText(v.getContext());
//// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
//                //input.setText(preferenceURL);
//                builder.setView(input);
//                builder.show();
//
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(v.getContext());
                //builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Select One Name:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Hardik");
                arrayAdapter.add("Archit");
                arrayAdapter.add("Jignesh");
                arrayAdapter.add("Umang");
                arrayAdapter.add("Gatti");

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
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(v.getContext());
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();


            }
        }







    }

//    public String parseCreationDate(String creationDate){
//        String string = creationDate;
//        String[] parts = string.split("T");
//        String part1 = parts[0];
//        String part2 = parts[1];
//        System.out.println(datestr+" "+part2);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
//        String datestr = formatter.format(part1);
//        System.out.println(datestr+" "+part2);
//        return datestr+" "+part2;
//    }

}

