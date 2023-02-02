package com.example.digitalsignmanagement;



import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class activity_sign extends AppCompatActivity {

    DrawableView drawableView;
    DrawableViewConfig config;
    Button showOld, decrease, save, undo;
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
        showOld = findViewById(R.id.showOld);
        decrease = findViewById(R.id.decrease);
        save = findViewById(R.id.Save);
        undo = findViewById(R.id.undo);
        config = new DrawableViewConfig();
        String token = Helper.retriveToken(this);
        String userId = Helper.retriveUserId(this);
        String docId = Helper.retriveDocId(this);
        String DocName = Helper.retriveDocName(this);
        String preferenceURL = Helper.retriveData(this, "url");
        String urlOldSignature = preferenceURL;
        urlOldSignature = urlOldSignature +"/signers/"+ userId +"/documents/"+ docId +"/lastSignature";
        urlRaw = preferenceURL+"/signers/"+userId+"/documents/"+docId;
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
        String finalUrlOldSignature = urlOldSignature;
        showOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(), OldSignature.class);
                startActivity(intent1);

            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // decrease stroke width by 10
                config.setStrokeWidth(config.getStrokeWidth() - 10);
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
                System.out.println(urlRaw);

                //String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                File file = new File("sdcard/Pictures/image.png");
//                FileOutputStream ostream;
//                try {
//                    file.createNewFile();
//                    ostream = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
//                    ostream.flush();
//                    ostream.close();
//                    Toast.makeText(getApplicationContext(), "image saved", 5000).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "error", 5000).show();
//                }

                String encoded = Base64.encodeToString(
                        byteArray, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
                sendRequest(encoded,token);
                finish();
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

    public void checkForOldSignature(String url, String token)  {

        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            JSONObject pdfraw = null;
            @Override
            public void onResponse(JSONObject response) {
                pdfraw = response;
                System.out.println("response");
                System.out.println(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.printStackTrace();

                        showOld.setClickable(false);
                        showOld.setAlpha(.5f);
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
}





