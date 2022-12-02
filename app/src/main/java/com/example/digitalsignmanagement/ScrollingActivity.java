package com.example.digitalsignmanagement;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.ui.login.LoginActivity;
import com.example.digitalsignmanagement.unterschriften.JacksonRequest;
import com.example.digitalsignmanagement.unterschriften.Sign;
import com.example.digitalsignmanagement.unterschriften.SignAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalsignmanagement.databinding.ActivityScrollingBinding;

import org.json.JSONObject;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private RecyclerView sign;
    private RecyclerView.Adapter adapter;
    private String preferenceURL;
    private Class Sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPreferences();
        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //AndroidNetworking.initialize(getApplicationContext());

        ArrayList<Sign> signs = initSigns();
        System.out.println(preferenceURL);
        String url = preferenceURL+ "/document";

        this.sign = (RecyclerView)findViewById(R.id.unterschrifen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.sign.setLayoutManager(mLayoutManager);

        adapter = new SignAdapter(signs);
        this.sign.setAdapter(adapter);


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        Context context = getBaseContext();

        SignAdapter mAdapter = new SignAdapter(signs);

        RecyclerView recyclerView = findViewById(R.id.unterschrifen);

        // Request a string response from the provided URL.
        System.out.println("Hier");

        // Request a string response from the provided URL.
        System.out.println("Hier");

        long id = 1;
        RequestQueue queue = Volley.newRequestQueue(this);

        //String api = Helper.getConfigValue(this, "api_url");
        System.out.println("Hierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        System.out.println(url);



        System.out.println("Hier");

        JacksonRequest<Sign> request = new JacksonRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject personInfo = null;
                System.out.println(response);

                Toast.makeText(getApplicationContext(), "Yes" + response.toString(), Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                    }

                });
        queue.add(request);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent1 = new Intent(ScrollingActivity.this, activity_sign.class);
                        startActivity(intent1);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }



    private ArrayList<Sign> initSigns() {
        ArrayList<Sign> list = new ArrayList<>();
        list.add(new Sign("Urlaubsantrag", "20.11.22", "Yanik", false));
        list.add(new Sign("Urlaubsantrag", "20.11.22", "Yanik1", true));
        list.add(new Sign("Urlaubsantrag", "20.11.22", "Yanik2", false));
        list.add(new Sign("Urlaubsantrag", "20.11.22", "Yanik3", true));
        return list;}

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyKey",MODE_PRIVATE);

//        if (sharedPreferences.getString("url", "") == "") {
//            preferenceURL = "http://10.0.2.2:8080/person";
//        } else {
            preferenceURL = sharedPreferences.getString("url", "");
        }


    }
