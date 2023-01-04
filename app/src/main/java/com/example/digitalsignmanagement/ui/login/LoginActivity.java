package com.example.digitalsignmanagement.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.digitalsignmanagement.databinding.ActivityLoginBinding;
import com.example.digitalsignmanagement.scrollingActivity.ScrollingActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    private ActivityLoginBinding binding;
    String preferenceURL;
    String savedInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("LIRA DSM");


        final EditText eMailEditText = binding.email;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        preferenceURL=Helper.retriveData(this,"url");
        eMailEditText.setText("email@email.de");
        passwordEditText.setText("Password");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = eMailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String loginURL = Helper.retriveData(LoginActivity.this, "url") + "/login";
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                JSONObject data = null;
                try {
                    data = new JSONObject();
                    data.put("email", email);
                    data.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(data);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginURL, data, new Response.Listener<JSONObject>() {
                    JSONObject personInfo = null;

                    @Override
                    public void onResponse(JSONObject response) {
                        personInfo = response;
                        System.out.println(response);
                        System.out.println(personInfo.toString());

                        //Toast.makeText(getApplicationContext(), "Yes" + response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            String name = personInfo.getString("name");
                            String token = personInfo.getString("token");
                            String id = personInfo.getString("id");
                            System.out.println(name +" "+ token +" "+id);
                            Helper.insertUserData(LoginActivity.this,name,token,id);
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                        Intent intent1 = new Intent(LoginActivity.this, ScrollingActivity.class);
                        startActivity(intent1);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Login Fehlgeschlagen", Toast.LENGTH_SHORT).show();
                            }

                        });
                queue.add(request);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_url, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.url:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("URL Einrichten");

// Set up the input
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(preferenceURL);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Hier?");
                        savedInput = input.getText().toString();
                        System.out.println(savedInput);
                        Helper.insertData(LoginActivity.this, "url", savedInput);
                        System.out.println("Hier!");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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


