package com.example.android.dor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.dor.ManagerClass.SessionManager;
import com.example.android.dor.R;

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
import java.util.HashMap;
import java.util.Iterator;

public class PasswordActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static final String Change_Password_URL = "https://dor-rubai.c9users.io/saveChanges/resetPassword.php ";
    EditText oldPass, newPass;
    Button resetbtn, cancelbtn;
    String oldpassword, newpassword;
    int userId;
    JSONObject postDataParams;
    SessionManager session;
    HashMap<String, String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        session = new SessionManager(getApplicationContext());
        userDetails = session.getUserDetails();

        oldPass = (EditText) findViewById(R.id.etoldpassword);
        newPass = (EditText) findViewById(R.id.etnewpswrd);
        resetbtn = (Button) findViewById(R.id.btn_reset);
        cancelbtn = (Button) findViewById(R.id.btn_pswrd_cancel);

        userId = Integer.parseInt(userDetails.get(SessionManager.KEY_USERID));


        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpassword = oldPass.getText().toString();
                newpassword = newPass.getText().toString();
                new ChangePassword().execute();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class ChangePassword extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PasswordActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tResettin Password...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }


        @Override
        protected String doInBackground(String... strings) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(Change_Password_URL);
                postDataParams = new JSONObject();
                postDataParams.put("userId", userId);
                postDataParams.put("oldPassword", oldpassword);
                postDataParams.put("newPassword", newpassword);
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
                String msg = jsonResponse.getString("msg");
                if (success) {
                    Toast.makeText(PasswordActivity.this, msg , Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PasswordActivity.this, ProfileActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
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
