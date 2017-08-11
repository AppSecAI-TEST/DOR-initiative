package com.example.android.dor.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.android.dor.ManagerClass.SessionManager;
import com.example.android.dor.R;
import com.example.android.dor.requestClasses.UploadRequest;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 2;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static final String PROFILE_URL = "https://dor-rubai.c9users.io/userGen/profileImage.php";
    CircleImageView proPic;
    ImageButton imgbtn, btnUserName, btnName, btnPass;
    String encodeImage;
    Bitmap image=null, image1;
    int userId;
    String name, username, password;
    TextView userName;
    TextView Name;
    TextView Password;
    JSONObject jsonObject;
    SessionManager session;
    HashMap<String, String> userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new SessionManager(getApplicationContext());

        retrieveData();
        imgbtn = (ImageButton) findViewById(R.id.imageButton2);
        proPic = (CircleImageView) findViewById(R.id.imageView3);
        Bitmap bitmap = getIntent().getParcelableExtra("Bitmap");
        proPic.setImageBitmap(bitmap);

        userName = (TextView) findViewById(R.id.textView4);
        Name = (TextView) findViewById(R.id.textView5);
        Password = (TextView) findViewById(R.id.textView6);

        btnUserName = (ImageButton) findViewById(R.id.imageButton);

        btnUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UserNameActivity.class);
                startActivity(intent);
            }
        });

        btnName = (ImageButton) findViewById(R.id.imageButton3);

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, NameActivity.class);
                startActivity(intent);
            }
        });

        btnPass = (ImageButton) findViewById(R.id.imageButton4);

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PasswordActivity.class);
                startActivity(intent);
            }
        });

        userName.setText(username);
        Name.setText(name);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GalIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Bitmap b=proPic.getDrawingCache();
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        intent.putExtra("Bitmap", b);
        setResult(RESULT_OK, intent);
        startActivityForResult(intent, 10);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Intent CropIntent = new Intent("com.android.camera.action.CROP");

                CropIntent.setDataAndType(selectedImage, "image/*");

                CropIntent.putExtra("crop", "true");
                CropIntent.putExtra("outputX", 180);
                CropIntent.putExtra("outputY", 180);
                CropIntent.putExtra("aspectX", 4);
                CropIntent.putExtra("aspectY", 4);
                CropIntent.putExtra("scaleUpIfNeeded", true);
                CropIntent.putExtra("return-data", true);

                startActivityForResult(CropIntent, 1);

            } catch (ActivityNotFoundException e) {
                String errorMessage = "your device doesn't support the crop action!";
                Toast toast = Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            image1 = extras.getParcelable("data");
            encodeImage = encodeTobase64(image1);
            new UploadImage().execute();
        }
    }

    private class UploadImage extends AsyncTask<String, Void, Void> {

        ProgressDialog pdLoading = new ProgressDialog(ProfileActivity.this);
        HttpURLConnection conn;
        URL url = null;

        private String Content;
        private String Error = null;

        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tEditting image...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                url = new URL(PROFILE_URL);
            } catch (MalformedURLException e){
                e.printStackTrace();
            }
            try {
                jsonObject = new JSONObject();
                jsonObject.put("encodedImage", encodeImage);
                jsonObject.put("userId", userId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(jsonObject));

                writer.flush();
                writer.close();
                os.close();


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        result.append(line);
                    }
                    in.close();
                    Content = result.toString();

                }
            } catch (IOException e) {
                e.printStackTrace();
                Error = e.getMessage();
            } finally {
                conn.disconnect();
            }

            return  null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pdLoading.dismiss();
            try {
                if (Content != null) {
                    Log.e("json_response", Content);
                    JSONObject jsonResponse = new JSONObject(Content);
                    boolean success = jsonResponse.getBoolean("success");
                    String msg = jsonResponse.getString("msg");
                    if (success) {
                        Toast.makeText(ProfileActivity.this, "Image uploaded successfully ", Toast.LENGTH_SHORT).show();
                        session.setImage(encodeImage);
                        String img = userDetails.get(SessionManager.KEY_IMAGE);
                        image = decodeBase64(img);
                        proPic.setImageBitmap(image);

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Image upload failed causing error: "+ msg)
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                }else{
                    Log.e("Error","content is null");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }


    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("Image Log", imageEncoded);
        return imageEncoded;
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

    public void retrieveData(){
        userDetails = session.getUserDetails();
        if (session.isLoggedIn()) {
            userId = Integer.parseInt(userDetails.get(SessionManager.KEY_USERID));
            name = userDetails.get(SessionManager.KEY_NAME);
            username = userDetails.get(SessionManager.KEY_USERNAME);
            password = userDetails.get(SessionManager.KEY_PASSWORD);

        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
};


