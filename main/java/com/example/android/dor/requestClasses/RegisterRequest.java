package com.example.android.dor.requestClasses;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by darip on 25-05-2017.
 */

public class RegisterRequest extends StringRequest {
    private static final String Register_Request_URL = "https://dor-rubai.c9users.io/userGen/signup.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String username, String email, int age, String password, String phone, Response.Listener<String> listener){
        super(Method.POST, Register_Request_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("email", email);
        params.put("age", age + "");
        params.put("password", password);
        params.put("phone", phone);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

