package com.example.android.dor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.android.dor.R;
import com.example.android.dor.adapter.CustomAdapter;
import com.example.android.dor.adapter.CustomNotificationAdapter;
import com.example.android.dor.objectClass.NotificationClass;
import com.example.android.dor.objectClass.ReportClass;
import com.example.android.dor.utils.NotificationUtils;

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

public class NotificationActivity extends AppCompatActivity {
//    TextView tvmsg;
    private RecyclerView nRecyclerView;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static final String FETCH_NOTIFICATION_URL = "https://dor-rubai.c9users.io/userGen/FetchNotification.php";
    private RequestQueue requestQueue;
    private CustomNotificationAdapter customNotificationAdapter;
    private Context context;
    private ArrayList<ReportClass> reportDetails = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        if(getIntent().hasExtra("countFromTray")) {
            NotificationUtils.setCountToZero(0);
        }
        new AsyncFetch().execute();

//        tvmsg = (TextView) findViewById(R.id.textView);
//        int count = 0;
//        String msg;
//        if(getIntent().hasExtra("countFromTray")) {
//            NotificationUtils.setCountToZero(0);
//            count = getIntent().getIntExtra("countFromTray", 0);
//        }else if(getIntent().hasExtra("countFromMain")) {
//            count = getIntent().getIntExtra("countFromMain", 0);
//        }
//        if(count == 0){
//            msg = "You have 0 notification";
//        }else if(count == 1){
//            msg = "You have 1 new notification";
//        }else{
//            msg = "You have "+count+" new notifications";
//        }
//
//        tvmsg.setText(msg);
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(NotificationActivity.this);
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
                url = new URL(FETCH_NOTIFICATION_URL);

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
            ArrayList<NotificationClass> notificationList = new ArrayList<>();

            pdLoading.dismiss();
            try {
                JSONObject json_response = new JSONObject(result);
                JSONArray jsonArray = json_response.getJSONArray("notification");

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    NotificationClass notiClass = new NotificationClass();
                    String notiName = json_data.getString("notiName");
                    String notiDetail = json_data.getString("notiDetail");
                    String notiType = json_data.getString("notiType");
                    String notiTimestamp = json_data.getString("notiTimestamp");
//                    String reportUrl = json_data.getString("reportUrl");
                    notiClass.setNotiName(notiName);
                    notiClass.setNotiTimestamp(notiTimestamp);
                    notiClass.setNotiType(notiType);
                    notiClass.setNotiDetails(notiDetail);
//                    notiClass.setReportUrl(reportUrl);
                    notificationList.add(notiClass);
                }

                // Setup and Handover data to recyclerview
                nRecyclerView = (RecyclerView)findViewById(R.id.rv_notification_custom);
                customNotificationAdapter = new CustomNotificationAdapter(NotificationActivity.this, notificationList);
                DividerItemDecoration itemDecoration = new DividerItemDecoration(NotificationActivity.this, new LinearLayoutManager(NotificationActivity.this).getOrientation());
                nRecyclerView.setAdapter(customNotificationAdapter);
                nRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                nRecyclerView.addItemDecoration(itemDecoration);
            } catch (JSONException e) {
                Toast.makeText(NotificationActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
        @Override
    protected void onResume() {
        super.onResume();
        NotificationUtils.clearNotifications(getApplicationContext());
    }
}