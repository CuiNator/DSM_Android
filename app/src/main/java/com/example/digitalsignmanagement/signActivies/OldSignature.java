package com.example.digitalsignmanagement.signActivies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.scrollingActivity.ScrollingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
//Class which shows the old signature, if one exists
public class OldSignature extends AppCompatActivity {

    Button submit;
    ImageView oldSign;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_signature);
        submit = findViewById(R.id.submit);
        oldSign = findViewById(R.id.oldSignImage);
        String token = Helper.retriveToken(this);
        String userId = Helper.retriveUserId(this);
        String docId = Helper.retriveDocId(this);
        String preferenceURL = Helper.retriveConnectionData(this, "url");
        String urlOldSignature = preferenceURL;
        urlOldSignature = urlOldSignature +"/signers/"+ userId +"/documents/"+ docId +"/lastSignature";
        String urlSendSignature = preferenceURL + "/signers/" + userId + "/documents/" + docId;
        getSupportActionBar().setTitle("Old Signature");
        getOldSignature(urlOldSignature,token);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String oldBitmap = Helper.retriveBitmap(OldSignature.this);
                byte[] base64b = java.util.Base64.getDecoder().decode(oldBitmap);
                String encoded = Base64.encodeToString(
                        base64b, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
                RequestQueue queue = Volley.newRequestQueue(OldSignature.this);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("signature", encoded);
                } catch (JSONException e) {
                }

                JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, urlSendSignature, jsonObject,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response
                                Log.d("Response", response.toString()+"yes");
                                finish();
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
                Intent intent1 = new Intent(OldSignature.this, ScrollingActivity.class);
                startActivity(intent1);
            }
        });

    }

    public void getOldSignature(String url, String token)  {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            JSONObject pdfraw = null;
            @Override
            public void onResponse(JSONObject response) {
                pdfraw = response;
                String base64 = null;
                byte[] base64b = null;
                try {
                    base64 = pdfraw.getString("signature");
                    base64b = java.util.Base64.getDecoder().decode(base64);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Helper.insertBitmap(OldSignature.this,base64);
                InputStream inputStream  = new ByteArrayInputStream(base64b);
                bitmap  = BitmapFactory.decodeStream(inputStream);
                oldSign.setImageBitmap(bitmap);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
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