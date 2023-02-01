package com.example.digitalsignmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.databinding.ActivityOldSignatureBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        String DocName = Helper.retriveDocName(this);
        String preferenceURL = Helper.retriveData(this, "url");
        String urlOldSignature = preferenceURL;
        urlOldSignature = urlOldSignature +"/signers/"+ userId +"/documents/"+ docId +"/lastSignature";
        String urlRaw = preferenceURL + "/signers/" + userId + "/documents/" + docId;
        getSupportActionBar().setTitle("Old Signature");
        getOldSignature(urlOldSignature,token);

        submit.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                View content = oldSign;
                bitmap = content.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                String encoded = Base64.encodeToString(
                        byteArray, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
                RequestQueue queue = Volley.newRequestQueue(OldSignature.this);

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
        });
    }


    public void getOldSignature(String url, String token)  {

        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            JSONObject pdfraw = null;
            @Override
            public void onResponse(JSONObject response) {
                pdfraw = response;
                System.out.println("response");
                System.out.println(response);
                String base64 = null;
                byte[] base64b = null;
                try {
                    base64 = pdfraw.getString("signature");
                    System.out.println(base64);
                    base64b = java.util.Base64.getDecoder().decode(base64);
                    System.out.println(Arrays.toString(base64b));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                InputStream inputStream  = new ByteArrayInputStream(base64b);
                bitmap  = BitmapFactory.decodeStream(inputStream);
                oldSign.setImageBitmap(bitmap);
                //setContentView(R.id.Main);
                System.out.println("DasEnde");

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println(error.toString());
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