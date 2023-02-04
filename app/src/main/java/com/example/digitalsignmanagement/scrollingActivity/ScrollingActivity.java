package com.example.digitalsignmanagement.scrollingActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView sign;
    private String preferenceURL;
    TextView loggedUser;
    ArrayList<Document> documentList = new ArrayList<>();
    private DocAdapter adapter;
    private String url;
    private Button filter;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        preferenceURL = Helper.retriveData(this, "url");
        String name = Helper.retriveUserName(this);
        token = Helper.retriveToken(this);
        String id = Helper.retriveUserId(this);
        url = preferenceURL + "/signers/"+id+"/documents";
        filter = (Button) findViewById(R.id.filter);
        //On activity start get only active documents
        try {
            getDocument(url,token,"active");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter= new DocAdapter(ScrollingActivity.this, documentList);

        setContentView(R.layout.activity_scrolling);
        loggedUser = findViewById(R.id.user);
        loggedUser.setText("Current user: " + name);

        this.sign = (RecyclerView) findViewById(R.id.documents);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.sign.setLayoutManager(mLayoutManager);
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

    private void getDocument(String url,String token, String status) throws JSONException {

//        JSONObject ka= new JSONObject();
//        ka.put("status","active");
//        JSONArray body = new JSONArray();
//        body.put(ka);
//        System.out.println(body.toString());
//        System.out.println("Breakpoint");
        if (!Objects.equals(status, "all")) {
            url = url + "?status=" + status;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray  response) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ArrayList<Document> docList = objectMapper.readValue(String.valueOf(response), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Document.class));
                    documentList = docList;
                    adapter.setDocument(documentList);
                    ScrollingActivity.this.sign.setAdapter(adapter);
                    sign.addItemDecoration(new DividerItemDecoration(sign.getContext(), DividerItemDecoration.VERTICAL));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error retrieving Documents", Toast.LENGTH_SHORT).show();
                    }

                })
        {
//            @Override
//            public Map<String, String> getParams() {
//                System.out.println("DaRein");
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("status","active");
//                return params;
//            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ token);
                return headers;
            }
        };
        queue.add(request);

    }
    //Logic for the filter button
    @Override
    public void onClick(View v) {

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Active");
        arrayAdapter.add("Archived");
        arrayAdapter.add("Full");
        arrayAdapter.add("Completed");
        arrayAdapter.add("Canceled");
        arrayAdapter.add("All");

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(v.getContext());
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

                    strName = strName.toLowerCase();
                    try {
                        getDocument(url, token, strName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                Toast.makeText(getApplicationContext(), strName, Toast.LENGTH_LONG).show();
            }
        });
        builderSingle.setTitle("Select documents to show");
        builderSingle.show();
    }
}
