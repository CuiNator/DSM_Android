package com.example.digitalsignmanagement.scrollingActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Browser;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.activity_sign;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Document> documents;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final TextView name;
        public final TextView ersteller;
        public final TextView datum;
        public final TextView id;
        public final TextView status;
        public final TextView gezeichnet;

        public Button intern;
        public Button extern;
        public Button pdf;
        public ExternalSigners externalSigners[];
        public Button filter;
        private String url;



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
            pdf = view.findViewById(R.id.pdf);
            pdf.setOnClickListener(this);
            id = view.findViewById(R.id.docId);
            gezeichnet = view.findViewById(R.id.gezeichnet);
            filter = view.findViewById(R.id.filter);

        }

        void bind(final Document document) {
            name.setText(document.getName());
            ersteller.setText(document.getCreatorName());

            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd/MM/yyyy");
            datum.setText(formatter.format(document.getCreationDate()));
            status.setText(document.getStatus());

            String anzahl = document.getReceivedSignatures() + "/" + document.getMaxSigns();
            gezeichnet.setText(anzahl);
            if (document.getMaxSigns() == document.getReceivedSignatures()) {
                intern.setClickable(false);
                intern.setAlpha(.5f);
                extern.setClickable(false);
                extern.setAlpha(.5f);
            }

            String docId = String.valueOf(document.getDocumentId());
            id.setText(docId);
            externalSigners = document.getExternalSigners();
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == pdf.getId()) {
                try {
                    getPDF();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            if (v.getId() == intern.getId()) {
                System.out.println("Button pressed " + this.name.getText().toString());
                Context context = view.getContext();
                Helper.insertDocData(context, this.name.getText().toString(), this.id.getText().toString());
                Intent intent1 = new Intent(v.getContext(), activity_sign.class);
                context.startActivity(intent1);
            }

            if (v.getId() == extern.getId()) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(v.getContext());
                builderSingle.setTitle("Select external signer:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.select_dialog_singlechoice);
                for (int i = 0; i < externalSigners.length; i++) {
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
                        String persId = null;
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(v.getContext());
                        for (int i = 0; i < externalSigners.length; i++) {
                            if (strName == externalSigners[i].getName()) {
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

        //PDF anzeigen lassen
        private void getPDF() throws JSONException {

            url = Helper.retriveData(this.view.getContext(), "url");
            url = url + "/signers/" + Helper.retriveUserId(this.view.getContext()) + "/documents/" + Helper.retriveDocId(this.view.getContext());
            String token = Helper.retriveToken(this.view.getContext());

            RequestQueue queue = Volley.newRequestQueue(this.view.getContext());
            System.out.println(url);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                JSONObject pdfraw = null;
                @Override
                public void onResponse(JSONObject response) {
                    pdfraw = response;
                    System.out.println("response");
                    System.out.println(response);
                    String base64 = null;
                    byte[] base64b = null;
                    try {
                        base64 = pdfraw.getString("pdf");
                        System.out.println(base64);
                        base64b = Base64.getDecoder().decode(base64);
//                        pdfb = base64.getBytes();
//                        System.out.println(Arrays.toString(pdfb));
//                        System.out.println(pdfb);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    String filename = "sdcard/documents";
//                    FileOutputStream output = new FileOutputStream(filename);
//                    output.write();

                    OutputStream out = null;
                    try {
                        out = new FileOutputStream("sdcard/Documents/test2.pdf");
                        //out = new FileOutputStream(context.getFilesDir()+"temp.pdf");
                        out.write(base64b);
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    File file = new File("sdcard/Documents/test2.pdf");
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(Uri.fromFile(file),"application/pdf");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Instruct the user to install a PDF reader here, or something
                        System.out.println("Downloade nen PDF Reader");
                    }

//                    File file = new File("sdcard/Documents/test2.pdf");
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    context.startActivity(intent);




//                    File mFile = new File("sdcard/documents/document3.txt");
//                    try {
//                        FileUtils.writeByteArrayToFile(mFile, pdfb);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            System.out.println(error.toString());
                            Toast.makeText(context.getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                        }

                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();

                    //headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    System.out.println(headers.toString());
                    return headers;
                }
            };
            queue.add(request);

        }

    }}




