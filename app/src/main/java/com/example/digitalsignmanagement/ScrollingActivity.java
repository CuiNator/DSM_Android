package com.example.digitalsignmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.digitalsignmanagement.databinding.ActivityScrollingBinding;
import com.example.digitalsignmanagement.unterschriften.Sign;
import com.example.digitalsignmanagement.unterschriften.SignAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private RecyclerView sign;
    private RecyclerView.Adapter adapter;
    private String preferenceURL;
    private Class Sign;
    TextView loggedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceURL = Helper.retriveData(this, "url");
        String name = Helper.retriveName(this);
        String token = Helper.retriveToken(this);
        String id = Helper.retriveId(this);
        System.out.println("HierInScrolling");
        System.out.println(name + token + id);
        //loadPreferences();
        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loggedUser = findViewById(R.id.User);
        loggedUser.setText("Current user: " + name);

        //AndroidNetworking.initialize(getApplicationContext());

        ArrayList<Sign> signs = initSigns();
        System.out.println(preferenceURL);
        String url = preferenceURL + "/document";

        this.sign = (RecyclerView) findViewById(R.id.unterschrifen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.sign.setLayoutManager(mLayoutManager);

        adapter = new SignAdapter(signs);
        this.sign.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        this.sign.addItemDecoration(itemDecoration);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        Context context = getBaseContext();

        SignAdapter mAdapter = new SignAdapter(signs);

        RecyclerView recyclerView = findViewById(R.id.unterschrifen);

        RequestQueue queue = Volley.newRequestQueue(this);

        //String api = Helper.getConfigValue(this, "api_url");

//        JacksonRequest request = new JacksonRequest(Request.Method.GET, url, Sign, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                JSONObject personInfo = null;
//                System.out.println(response);
//
//                Toast.makeText(getApplicationContext(), "Yes" + response.toString(), Toast.LENGTH_LONG).show();
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                });
//        queue.add(request);

        JsonArrayRequest request = new JsonArrayRequest (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray  response) {
                JSONObject personInfo = null;
                System.out.println("response");
                System.out.println(response);

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
}
