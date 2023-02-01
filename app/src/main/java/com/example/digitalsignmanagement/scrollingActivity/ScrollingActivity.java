package com.example.digitalsignmanagement.scrollingActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.ui.login.LoginActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView sign;
    private String preferenceURL;
    TextView loggedUser;
    ArrayList<Document> documentList = new ArrayList<>();
    private DocAdapter adapter;
    private String url;
    private Button filter;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        preferenceURL = Helper.retriveData(this, "url");
        String name = Helper.retriveUserName(this);
        String token = Helper.retriveToken(this);
        String id = Helper.retriveUserId(this);
//        System.out.println("HierInScrolling");
//        System.out.println(name + token + id);
        url = preferenceURL + "/signers/"+id+"/documents";
//        filter = findViewById(R.id.filter);
//        filter.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
//            final MenuItem item = menu.add("item-text");
//            item.setOnMenuItemClickListener(i -> {
//                doWorkOnItemClick();
//                return true; // Signifies you have consumed this event, so propogation can stop.
//            });
//            final MenuItem anotherItem = menu.add("another-item");
//            anotherItem.setOnMenuItemClickListener(i -> doOtherWorkOnItemClick());
//        });
        //filter.setOnClickListener(View::showContextMenu);



        //filter.setOnClickListener((View.OnClickListener) this);
        try {
            ArrayList<Document> documents = getDocument(url,token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter= new DocAdapter(ScrollingActivity.this, documentList);

        setContentView(R.layout.activity_scrolling);
        loggedUser = findViewById(R.id.User);
        loggedUser.setText("Current user: " + name);

        System.out.println(preferenceURL);



        this.sign = (RecyclerView) findViewById(R.id.unterschrifen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.sign.setLayoutManager(mLayoutManager);
        //registerForContextMenu(this.filter);
    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, v,menuInfo);
//
//        getMenuInflater().inflate(R.menu.context_menu,menu);
//
//    }
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.active:
//                Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.all:
//                Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

    private ArrayList<Document> getDocument(String url,String token) throws JSONException {

        JSONObject ka= new JSONObject();
        ka.put("status","active");
        JSONArray body = new JSONArray();
        body.put(ka);
        System.out.println(body.toString());
        System.out.println("Breakpoint");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray  response) {
                System.out.println("response Signer");
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
            public Map<String, String> getParams() {
                System.out.println("DaRein");
                Map<String,String> params = new HashMap<String, String>();
                params.put("status","active");
                return params;
            }
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

    @Override
    public void onClick(View v) {

    }
    public void doWorkOnItemClick(){}

    public boolean doOtherWorkOnItemClick(){
        return true;
    }
}
