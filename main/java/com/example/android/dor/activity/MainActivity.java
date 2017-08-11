package com.example.android.dor.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.dor.ManagerClass.SessionManager;
import com.example.android.dor.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;




public class MainActivity extends AppCompatActivity {
//    CallbackManager callbackManager;
//    Button loginbtn;
//    LoginButton fblogin;
//    CallbackManager callbackManager;
        private static int SPLASH_TIME_OUT = 2000;
        SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(!session.isLoggedIn()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
//        loginbtn = (Button) findViewById(R.id.btn1);


//        callbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = (LoginButton) view.findViewById(R.id.usersettings_fragment_login_button);

//        loginbtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                MainActivity.this.startActivity(intent);
//                finish();
//            }
//        });
//
//        initialiseControls();
//        LogInWithFb();
//
    }

//    @Override
//    public void onBackPressed() {
////        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
////        mainActivity.addCategory(Intent.CATEGORY_HOME);
////        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        startActivity(mainActivity);
//        super.onBackPressed();
//    }
//
//    public void initialiseControls(){
//        callbackManager = CallbackManager.Factory.create();
//        fblogin = (LoginButton) findViewById(R.id.login_button);
//    }
//
//    public void LogInWithFb(){
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(MainActivity.this, "Login Successful with facebook"+ loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(MainActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(MainActivity.this, "Error in Login with facebook"+ error.getMessage(), Toast.LENGTH_SHORT).show();
//                error.getMessage();
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
}
