package com.example.android.dor.requestClasses;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by darip on 02-06-2017.
 */

public class AvatarRequest extends StringRequest {

    private static final String Avatar_Fetch_URL = "https://dor-rubai.c9users.io/userGen/fetchAvatar.php ";
    private Map<String, String> params;

    public AvatarRequest(int userid, Response.Listener<String> listener){
        super(Request.Method.POST, Avatar_Fetch_URL, listener, null);
        params = new HashMap<>();
        params.put("userId", userid + "");

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

