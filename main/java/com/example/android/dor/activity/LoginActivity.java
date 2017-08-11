package com.example.android.dor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.dor.ManagerClass.SessionManager;
import com.example.android.dor.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static final String Login_Request_URL = "https://dor-rubai.c9users.io/userGen/login.php ";
    String email="";
    String password="";
    EditText etEmail, etPassword;
    TextView registerLink;
    Button bLogin;
    JSONObject postDataParams;
    SessionManager session;
    LoginButton fblogin;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());

        etEmail = (EditText) findViewById(R.id.editText);
        etPassword = (EditText) findViewById(R.id.editText2);

        bLogin = (Button) findViewById(R.id.button4);
        registerLink = (TextView) findViewById(R.id.textView8);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Login1Activity.this, "Trying to click", Toast.LENGTH_SHORT).show();
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
                finish();
            }
        });


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password = etPassword.getText().toString();
                final String email = etEmail.getText().toString();

                if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Please enter your credentials completely", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    new AsyncFetch().execute();
                }
            }
        });

        initialiseControls();
        LogInWithFb();
    }

    public void initialiseControls(){
        callbackManager = CallbackManager.Factory.create();
        fblogin = (LoginButton) findViewById(R.id.login_button);
    }

    public void LogInWithFb(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Login Successful with facebook"+ loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error in Login with facebook"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                error.getMessage();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
//        mainActivity.addCategory(Intent.CATEGORY_HOME);
//        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(mainActivity);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLogging...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }


        @Override
        protected String doInBackground(String... strings) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(Login_Request_URL);
                postDataParams = new JSONObject();
                postDataParams.put("email", email);
                postDataParams.put("password", password);
                Log.e("params", postDataParams.toString());

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoOutput to true as we recieve data from json file
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = in.readLine()) != null) {
                        result.append(line);
                    }

                    in.close();
                    return result.toString();

                } else {
                    return new String("false : " + response_code);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            try {
                Log.d("TAG", result);
                JSONObject jsonResponse = new JSONObject(result);
                boolean success = jsonResponse.getBoolean("success");
                String msg = jsonResponse.getString("message");
                if (success) {

                    int id = jsonResponse.getInt("userId");
                    String name = jsonResponse.getString("name");
                    String username = jsonResponse.getString("username");
                    String email = jsonResponse.getString("email");
                    int age = jsonResponse.getInt("age");
                    String phone = jsonResponse.getString("phone");
                    String encodedImg = jsonResponse.getString("encodeimg");
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    session.createLoginSession(id, name, username, email, age, phone, encodedImg);
                    intent.putExtra("success", true);
                    intent.putExtra("userId", id);
                    intent.putExtra("name", name);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("age", age);
                    intent.putExtra("phone", phone);
                    LoginActivity.this.startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(msg)
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}

