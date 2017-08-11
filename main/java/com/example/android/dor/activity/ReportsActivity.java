package com.example.android.dor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.android.dor.adapter.CustomAdapter;
import com.example.android.dor.R;
import com.example.android.dor.objectClass.ReportClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ReportsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static final String Upload_Request_URL = "https://dor-rubai.c9users.io/userGen/FetchReportFiles.php";
    private RequestQueue requestQueue;
    private CustomAdapter customAdapter;
    private  Context context;
    private ArrayList<ReportClass> reportDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_reports);
        new AsyncFetch().execute();
    }



    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ReportsActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }


        @Override
        protected String doInBackground(String... strings) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(Upload_Request_URL);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
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
            ArrayList<ReportClass> reportList = new ArrayList<>();

            pdLoading.dismiss();
            try {
                JSONObject json_response = new JSONObject(result);
                JSONArray jsonArray = json_response.getJSONArray("report");

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    ReportClass reportClass = new ReportClass();
                    String name = json_data.getString("reportName");
                    String fileType = json_data.getString("reportType");
                    String date = json_data.getString("reportTimestamp");
                    String path = json_data.getString("reportPath");
                    String reportUrl = json_data.getString("reportUrl");
                    reportClass.setName(name);
                    reportClass.setDate(date);
                    reportClass.setReportType(fileType);
                    reportClass.setPath(path);
                    reportClass.setReportUrl(reportUrl);
                    reportList.add(reportClass);
                }

                // Setup and Handover data to recyclerview
                mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_custom);
                customAdapter = new CustomAdapter(ReportsActivity.this, reportList);
                DividerItemDecoration itemDecoration = new DividerItemDecoration(ReportsActivity.this, new LinearLayoutManager(ReportsActivity.this).getOrientation());
                mRecyclerView.setAdapter(customAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ReportsActivity.this));
                mRecyclerView.addItemDecoration(itemDecoration);

            } catch (JSONException e) {
                Toast.makeText(ReportsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}





