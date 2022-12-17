package com.example.digitalsignmanagement;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPut;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class activity_sign extends AppCompatActivity {

    DrawableView drawableView;
    DrawableViewConfig config;
    Button increase, decrease, color, undo;
    private String urlRaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        // initialise the value
        initializelayout();

    }

    private void initializelayout() {

        // initialise the layout
        drawableView = findViewById(R.id.paintView);
        increase = findViewById(R.id.increase);
        decrease = findViewById(R.id.decrease);
        color = findViewById(R.id.Save);
        undo = findViewById(R.id.undo);
        config = new DrawableViewConfig();
        String token = Helper.retriveToken(this);
        String userId = Helper.retriveUserId(this);
        String docId = Helper.retriveDocId(this);
        String DocName = Helper.retriveDocName(this);
        String preferenceURL = Helper.retriveData(this, "url");
        urlRaw = preferenceURL+"/signers/"+userId+"/documents/"+docId;

        getSupportActionBar().setTitle(DocName);

        // set stroke color as black initially
        config.setStrokeColor(getResources().getColor(android.R.color.black));

        // If the view is bigger than canvas,
        // with this the user will see the bounds (Recommended)
        config.setShowCanvasBounds(true);

        // set width as 20
        config.setStrokeWidth(20.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // set canvas height
        config.setCanvasHeight(1080);

        // set canvas width
        config.setCanvasWidth(width);
        drawableView.setConfig(config);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // increase the stroke width by 10
                config.setStrokeWidth(config.getStrokeWidth() + 10);

            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // decrease stroke width by 10
                config.setStrokeWidth(config.getStrokeWidth() - 10);
            }
        });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View content = drawableView;
                content.setDrawingCacheEnabled(true);
                content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                Bitmap bitmap = content.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                //String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String encoded = Base64.encodeToString(
                        byteArray, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
                sendRequest(encoded,token);
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // undo the most recent changes
                drawableView.clear();
            }
        });


    }
    private void sendRequest(String encoded,String token) {
        RequestQueue queue = Volley.newRequestQueue(this);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("signature", encoded);
            System.out.println(jsonObject.toString());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }


        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, urlRaw, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization","Bearer "+ token);
                return headers;
            }

            @Override
            public byte[] getBody() {

                try {
                    Log.i("json", jsonObject.toString());
                    return jsonObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        queue.add(putRequest);
    }
}