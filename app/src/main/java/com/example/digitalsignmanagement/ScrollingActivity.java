package com.example.digitalsignmanagement;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.digitalsignmanagement.unterschriften.Document;
import com.example.digitalsignmanagement.unterschriften.DocAdapter;
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
        //loadPreferences();
        //binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_scrolling);
        loggedUser = findViewById(R.id.User);
        loggedUser.setText("Current user: " + name);

        //AndroidNetworking.initialize(getApplicationContext());
        System.out.println(preferenceURL);

        //adapter = new DocAdapter(this, documents);
        //this.sign.setAdapter(adapter);

        this.sign = (RecyclerView) findViewById(R.id.unterschrifen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.sign.setLayoutManager(mLayoutManager);

        //adapter= new DocAdapter(documentList);
        //this.sign.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        this.sign.addItemDecoration(itemDecoration);

        //Toolbar toolbar = binding.toolbar;
        //setSupportActionBar(toolbar);
        //CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        //toolBarLayout.setTitle(getTitle());

//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        System.out.println("onClick");
////                        int itemPosition = recyclerView.indexOfChild(v);
////                        //    Toast.makeText(MainActivity.this,"Selected item position is---"+ itemPosition,Toast.LENGTH_SHORT).show();
////                        textView = (TextView)v.findViewById(R.id.number_textview);
////                        Toast.makeText(MainActivity.this,"Selected val of clicked position is---"+ textView.getText().toString(),Toast.LENGTH_SHORT).show();
//
////                        Intent intent1 = new Intent(ScrollingActivity.this, activity_sign.class);
////                        startActivity(intent1);
//                        if(adapter.getSelected() != null){
//                            Toast.makeText(getApplicationContext(), adapter.getSelected().getCreator().toString(),Toast.LENGTH_SHORT).show();
//                            System.out.println(adapter.getSelected().getCreator().getName());
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(), adapter.getSelected().getCreator().toString(),Toast.LENGTH_SHORT).show();
//                            System.out.println("ze");
//                        }
//                    }
//
//                    @Override public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );
    }



//    private ArrayList<Document> initSigns() {
//        ArrayList<Document> list = new ArrayList<>();
//        list.add(new Document("Urlaubsantrag", "20.11.22", "Yanik", false));
//        list.add(new Document("Urlaubsantrag", "20.11.22", "Yanik1", true));
//        list.add(new Document("Urlaubsantrag", "20.11.22", "Yanik2", false));
//        list.add(new Document("Urlaubsantrag", "20.11.22", "Yanik3", true));
//        return list;}



    private ArrayList<Document> getDocument(String url,String token){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray  response) {
                //callback.callback();
                //callback.calledFromMain(response);
                JSONObject personInfo = null;
                System.out.println("response");
                System.out.println(response);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ArrayList<Document> docList = objectMapper.readValue(String.valueOf(response), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Document.class));
                    System.out.println(docList);
//                    setDocumentList(docList);


                    documentList = docList;
                    adapter.setDocument(documentList);
                    ScrollingActivity.this.sign.setAdapter(adapter);

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
            public Map<String, String> getHeaders() throws AuthFailureError {
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
    public void nextSlide()
    {
        Intent intent1 = new Intent(ScrollingActivity.this, activity_sign.class);
        startActivity(intent1);
    }


}
