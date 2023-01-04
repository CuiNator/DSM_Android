package com.example.digitalsignmanagement.scrollingActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.activity_sign;
import com.example.digitalsignmanagement.ui.login.LoginActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {
    //private ActivityScrollingBinding binding;
    private RecyclerView sign;
    private String preferenceURL;
    TextView loggedUser;
    ArrayList<Document> documentList = new ArrayList<>();
    private DocAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceURL = Helper.retriveData(this, "url");
        String name = Helper.retriveUserName(this);
        String token = Helper.retriveToken(this);
        String id = Helper.retriveUserId(this);
        System.out.println("HierInScrolling");
        System.out.println(name + token + id);
        String url = preferenceURL + "/signers/"+id+"/documents";
        ArrayList<Document> documents = getDocument(url,token);
        adapter= new DocAdapter(ScrollingActivity.this, documentList);

        setContentView(R.layout.activity_scrolling);
        loggedUser = findViewById(R.id.User);
        loggedUser.setText("Current user: " + name);

        System.out.println(preferenceURL);



        this.sign = (RecyclerView) findViewById(R.id.unterschrifen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.sign.setLayoutManager(mLayoutManager);
    }


    private ArrayList<Document> getDocument(String url,String token){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray  response) {
                System.out.println("response");
                System.out.println(response);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ArrayList<Document> docList = objectMapper.readValue(String.valueOf(response), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Document.class));
                    System.out.println(docList);

                    documentList = docList;
                    adapter.setDocument(documentList);
                    ScrollingActivity.this.sign.setAdapter(adapter);
                    sign.addItemDecoration(new DividerItemDecoration(sign.getContext(), DividerItemDecoration.VERTICAL));


                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Yes" + response.toString(), Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println(error.toString());
                        Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                    }

                })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+ token);
                System.out.println(headers.toString());
                return headers;
            }
        };
        System.out.println("Ausgabe ist: "+ documentList);
        queue.add(request);
        return documentList;

    }

}
