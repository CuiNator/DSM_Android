package com.example.digitalsignmanagement.scrollingActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// Window which shows our RecyclerView
public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView sign;
    private String preferenceURL;
    TextView loggedUser;
    ArrayList<Document> documentList = new ArrayList<>();
    private DocAdapter adapter;
    private String url;
    private Button filter;
    private String token;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            getDocument(url,token,"active");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceURL = Helper.retrieveConnectionData(this, "url");
        String name = Helper.retrieveUserName(this);
        token = Helper.retrieveToken(this);
        String id = Helper.retrieveUserId(this);
        url = preferenceURL + "/signers/" + id + "/documents";
        filter = (Button) findViewById(R.id.filter);


        //On activity start get only active documents
        try {
            getDocument(url, token, "active");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        adapter = new DocAdapter(ScrollingActivity.this, documentList);

//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeContainer.setRefreshing(false);
//                try {
//                    getDocument(url, token, "active");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


            setContentView(R.layout.activity_scrolling);
            loggedUser = findViewById(R.id.user);
            loggedUser.setText(name+"      ");

            this.sign = (RecyclerView) findViewById(R.id.documents);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            this.sign.setLayoutManager(mLayoutManager);

        }




    private void getDocument(String url,String token, String status) throws JSONException {
        //If "all" is selected the call uses the vanilla url which returns all documents
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
        builderSingle.setTitle(ScrollingActivity.this.getString(R.string.filterText));
        builderSingle.show();
    }

}
