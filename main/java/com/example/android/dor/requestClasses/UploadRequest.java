package com.example.android.dor.requestClasses;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by darip on 30-05-2017.
 */

public class UploadRequest extends StringRequest {
    private static final String Upload_Request_URL = "https://dor-rubai.c9users.io/userGen/profileImage.php";
    private Map<String, String> params;

    public UploadRequest(int userId, String selectedPhoto, Response.Listener<String> listener){
        super(Method.POST, Upload_Request_URL, listener, null);
        params = new HashMap<>();

        params.put("userId", userId + "");
        params.put("path", selectedPhoto );

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
