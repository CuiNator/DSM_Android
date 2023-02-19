package com.example.digitalsignmanagement.ui.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.databinding.ActivityLoginBinding;
import com.example.digitalsignmanagement.scrollingActivity.ScrollingActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    private ActivityLoginBinding binding;
    private String preferenceURL;
    private String savedInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("LIRA DSM");


        final EditText eMailEditText = binding.email;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        preferenceURL=Helper.retrieveConnectionData(this,"url");
        eMailEditText.setText(Helper.retrieveLastUser(this));
        passwordEditText.setText("Password");

        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                        PackageManager.PERMISSION_GRANTED);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = eMailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String loginURL = Helper.retrieveConnectionData(LoginActivity.this, "url") + "/login";
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                JSONObject data = null;
                try {
                    data = new JSONObject();
                    data.put("email", email);
                    data.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginURL, data, new Response.Listener<JSONObject>() {
                    JSONObject personInfo = null;
                    @Override
                    public void onResponse(JSONObject response) {
                        personInfo = response;
                        try {
                            String name = personInfo.getString("name");
                            String token = personInfo.getString("token");
                            String id = personInfo.getString("id");
                            Helper.insertUserData(LoginActivity.this,name,token,id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Helper.insertLastUser(LoginActivity.this, email);
                        Intent intent1 = new Intent(LoginActivity.this, ScrollingActivity.class);
                        startActivity(intent1);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Login failed, check connection and login data", Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(request);
            }
        });
    }
    //Creates menu for changing the connection URL
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_url, menu);
        return true;
    }
    //Reads in the connection URL and safes it with the Helper-class
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.url:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LoginActivity.this.getString(R.string.url));
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(preferenceURL);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savedInput = input.getText().toString();
                        Helper.insertConnectionData(LoginActivity.this, "url", savedInput);
                        preferenceURL = savedInput;
                    }
                });
                builder.setNegativeButton(LoginActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    }


