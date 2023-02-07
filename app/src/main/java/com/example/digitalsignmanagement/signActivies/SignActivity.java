package com.example.digitalsignmanagement.signActivies;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

//Class to draw a signature and send it to backend
public class SignActivity extends AppCompatActivity {

    DrawableView drawableView;
    DrawableViewConfig config;
    Button showOld, save, undo;
    private String urlSaveSignature;

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
        showOld = findViewById(R.id.showOld);
        save = findViewById(R.id.Save);
        undo = findViewById(R.id.clear);
        config = new DrawableViewConfig();
        String token = Helper.retriveToken(this);
        String userId = Helper.retriveUserId(this);
        String docId = Helper.retriveDocId(this);
        String DocName = Helper.retriveDocName(this);
        //Preparation of 2 urls, one for getting the old Signature, the other for sending the current signature
        String preferenceURL = Helper.retriveConnectionData(this, "url");
        String urlOldSignature = preferenceURL;
        urlSaveSignature = preferenceURL+"/signers/"+userId+"/documents/"+docId;
        urlOldSignature = urlOldSignature +"/signers/"+ userId +"/documents/"+ docId +"/lastSignature";
        checkForOldSignature(urlOldSignature,token);

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
        showOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(), OldSignature.class);
                startActivity(intent1);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View content = drawableView;
                content.setDrawingCacheEnabled(true);
                content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                Bitmap bitmap = content.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(
                        byteArray, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
                sendRequest(encoded,token);
                finish();
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clears the drawableView
                drawableView.clear();
            }
        });

    }
    private void sendRequest(String encoded,String token) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("signature", encoded);
        } catch (JSONException e) {
        }
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, urlSaveSignature, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Toast.makeText(getApplicationContext(), "Signature saved", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Signature already set", Toast.LENGTH_SHORT).show();
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

    //send a call for an old signature. If the call fails, the button gets disabled
    public void checkForOldSignature(String url, String token)  {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            JSONObject pdfraw = null;
            @Override
            public void onResponse(JSONObject response) {
                pdfraw = response;
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showOld.setClickable(false);
                        showOld.setAlpha(.5f);
                        }


                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }
}





