package com.example.digitalsignmanagement.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalsignmanagement.Helper;
import com.example.digitalsignmanagement.R;
import com.example.digitalsignmanagement.ScrollingActivity;
import com.example.digitalsignmanagement.databinding.ActivityLoginBinding;
import com.example.digitalsignmanagement.unterschriften.Sign;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    String preferenceURL;
    String savedInput;
    List<Sign> unterschriften = new ArrayList<Sign>();

    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText eMailEditText = binding.email;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        preferenceURL=Helper.retriveData(this,"url");
        //loadPreferences();
        eMailEditText.setText("email@email.de");
        passwordEditText.setText("Password");

// Request a string response from the provided URL.
        System.out.println("Hier");

        long id = 1;
        RequestQueue queue = Volley.newRequestQueue(this);
        String api = preferenceURL;
        //String api = Helper.getConfigValue(this, "api_url");
        String url = api + "/person/" + id;
        System.out.println(url);

        System.out.println("Hier");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject personInfo = null;
                System.out.println(response);

                Toast.makeText(getApplicationContext(), "Yes" + response.toString(), Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                    }

                });
        queue.add(request);


        //connection.setText(Helper.getConfigValue(this,"api_url"));

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    eMailEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(eMailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        eMailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(eMailEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loadingProgressBar.setVisibility(View.VISIBLE);
                // loginViewModel.login(usernameEditText.getText().toString(),
                //        passwordEditText.getText().toString());
                String email = eMailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String loginURL = Helper.retriveData(LoginActivity.this, "url") + "/user";

                //Call<ResponseBody>
                JSONObject personInfo = null;
                JSONObject jsonObject = null;
                JSONObject data = null;
                try {
                    data = new JSONObject();
                    data.put("email", email);
                    data.put("password", password);
                    //jsonObject = new JSONObject("{\"email\":"+ email +",\"password\":"+password+"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(data);
                //new SendDeviceDetails().execute("http://10.0.2.2:8080/user/login", jsonObject.toString());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginURL, data, new Response.Listener<JSONObject>() {
                    JSONObject personInfo = null;

                    @Override
                    public void onResponse(JSONObject response) {
                        personInfo = response;
                        System.out.println(response);
                        System.out.println(personInfo.toString());

                        Toast.makeText(getApplicationContext(), "Yes" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                            }

                        });
                queue.add(request);
//                Intent intent1 = new Intent(LoginActivity.this, ScrollingActivity.class);
//                startActivity(intent1);
            }
        });
    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

//    public void loadPreferences() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyKey",MODE_PRIVATE);
//
//        if (sharedPreferences.getString("url", "") == "") {
//            preferenceURL = "http://10.0.2.2:8080";
//        } else {
//            preferenceURL = sharedPreferences.getString("url", "");
//        }
//    }
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
                        //SavePreferences("url", savedInput);
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
        private class SendDeviceDetails extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String data = "";

                HttpURLConnection httpURLConnection = null;
                try {

                    httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                    httpURLConnection.setRequestMethod("POST");

                    httpURLConnection.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                    wr.writeBytes("PostData=" + params[0]);
                    wr.flush();
                    wr.close();

                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }

                return data;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            }
        }
    }


