package com.example.android.dor.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.android.dor.ManagerClass.SessionManager;
import com.example.android.dor.adapter.ViewPagerAdapter;
import com.example.android.dor.app.Config;
import com.example.android.dor.requestClasses.AvatarRequest;
import com.example.android.dor.R;
import com.example.android.dor.utils.NotificationUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    int userId, age;
    private int count = 0;
    String name, username, email, phone, Avatar;
    CircleImageView cimage;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    RelativeLayout notifcationCount;
    LayoutInflater layoutInflater;
    View view;
    LinearLayout sliderDotsPanel;
    private int dotscount;
    private ImageView [] dots;
    ViewPager viewPager;
    NotificationBadge mBadge;
    SessionManager session;
    public static Activity activity;
    HashMap<String, String> userDetails;
    Button inviteBtn;
    Bitmap image = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        notifcationCount = (RelativeLayout) findViewById(R.id.relative1);
        activity = this;
        session = new SessionManager(getApplicationContext());
//        session.checkLogin();
        retrieveData();

//        layoutInflater = (LayoutInflater) HomeActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        view = layoutInflater.inflate(R.layout.content_home, null);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        sliderDotsPanel = (LinearLayout) findViewById(R.id.sliderDots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(HomeActivity.this);
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];
        for(int i = 0; i<dotscount; i++){
            dots[i] = new ImageView(HomeActivity.this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            sliderDotsPanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i =0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

//        putAvatar();
//        Toast.makeText(HomeActivity.this, Avatar+" "+userId, Toast.LENGTH_SHORT).show();

        inviteBtn = (Button) findViewById(R.id.button3);
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, InviteActivity.class);
                startActivity(intent);
            }
        });


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView1.inflateHeaderView(R.layout.nav_header_home);
        cimage = (CircleImageView) headerView.findViewById(R.id.imageView);
        String img = userDetails.get(SessionManager.KEY_IMAGE);
        image = decodeBase64(img);
        cimage.setImageBitmap(image);

        TextView userName = (TextView) headerView.findViewById(R.id.tvName);
        TextView emailName = (TextView) headerView.findViewById(R.id.tvEmail);

        String Textname = (userDetails.get(SessionManager.KEY_NAME) == null) ? "cant retrieved name" : userDetails.get(SessionManager.KEY_NAME);
        String Textemail = (userDetails.get(SessionManager.KEY_EMAIL) == null) ? "cant retrieved email" : userDetails.get(SessionManager.KEY_EMAIL);
        Log.e("userName", Textname);
        Log.e("email", Textemail);
        userName.setText(Textname);

        emailName.setText(Textemail);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("My Activity", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Avatar = jsonResponse.getString("avatar");

                        File imgFile = new File(Avatar);
                        if (imgFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                            String imageString = encodeTobase64(myBitmap);
//                            session.putImage(imageString);
//                            Bitmap retrievedBitmap = decodeBase64(session.getImg());
                            cimage.setImageBitmap(myBitmap);
                        }
//                        Intent intent = new Intent(HomeActivity.this, Login1Activity.class);
//                        Register2Activity.this.startActivity(intent);
                    } else {
                        Avatar = jsonResponse.getString("avatar");

                        File imgFile = new File(Avatar);
                        if (imgFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                            String imageString = encodeTobase64(myBitmap);
//                            session.putImage(imageString);
//                            Log.e("imagePut", session.getImg());
//                            Bitmap retrievedBitmap = decodeBase64(session.getImg());
                            cimage.setImageBitmap(myBitmap);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        int userid = Integer.parseInt(userDetails.get(SessionManager.KEY_USERID));
        Log.e("Userid", userid + "");
        AvatarRequest avatarRequest = new AvatarRequest(userid, responseListener);
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(avatarRequest);

//        cimage = (CircleImageView) findViewById(R.id.imageView);
        cimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cimage.setDrawingCacheEnabled(true);
                Bitmap b = cimage.getDrawingCache();
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("name", name);
                intent.putExtra("Bitmap", b);
                //startActivity(intent);
                startActivityForResult(intent, 10);
                finish();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, InviteActivity.class);
//                startActivity(intent);
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView1.setNavigationItemSelectedListener(this);


//        Button inc = (Button) findViewById(R.id.button2);
//        Button clr = (Button) findViewById(R.id.button4);
//        inc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBadge.setNumber(++count);
//            }
//        });
//        clr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                count = 0;
//                mBadge.setNumber(count);
//            }
//        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received


                    mBadge.setNumber(++count);
                    Log.e("badgeCount", count + "");
                    String title = intent.getStringExtra("title");
                    String message = intent.getStringExtra("message");
                    String timeStamp = intent.getStringExtra("timestamp");
                    String vid = intent.getStringExtra("vid");
                    String nid = intent.getStringExtra("nid");
                    String nName = intent.getStringExtra("notificationName");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
//                else{
//                    String message = intent.getStringExtra("message");
//                    Toast.makeText(getApplicationContext(), "From Notification Tray: " + message, Toast.LENGTH_SHORT).show();
//                    txtMessage.setText(message);
//                }
            }
        };

        displayFirebaseRegId();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

//    private void displayFirebaseRegId() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//
//        Log.e(TAG, "Firebase reg id: " + regId);
//
//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = getIntent().getParcelableExtra("Bitmap");
                cimage.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened*****

        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem noti = menu.findItem(R.id.action_noti);
        MenuItemCompat.setActionView(noti, R.layout.notification_bar);
        notifcationCount = (RelativeLayout) MenuItemCompat.getActionView(noti);
        mBadge = (NotificationBadge) MenuItemCompat.getActionView(noti).findViewById(R.id.badge);
        ImageView img = (ImageView) MenuItemCompat.getActionView(noti).findViewById(R.id.icon);

//        int count = getIntent().getIntExtra("count", 0);
//        Log.e("badgeCount", count+"");
//        mBadge.setNumber(count);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBadge.setNumber(0);
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                intent.putExtra("countFromMain", count);
                setCount(0);
                HomeActivity.this.startActivity(intent);
            }


        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
//            Toast.makeText(this, "action logout", Toast.LENGTH_SHORT).show();
            session.logoutUser();
            return true;
        } else if (id == R.id.action_noti) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_volunteer) {
            Intent intent = new Intent(HomeActivity.this, VolunteerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_coupons) {
            Intent intent = new Intent(HomeActivity.this, CouponsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_partner) {
            Intent intent = new Intent(HomeActivity.this, PartnerActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_reports) {
            Intent intent = new Intent(HomeActivity.this, ReportsActivity.class);
            startActivity(intent);
        }
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void retrieveData() {
        userDetails = session.getUserDetails();
        if (session.isLoggedIn()) {
            userId = Integer.parseInt(userDetails.get(SessionManager.KEY_USERID));
            name = userDetails.get(SessionManager.KEY_NAME);
            username = userDetails.get(SessionManager.KEY_USERNAME);
            email = userDetails.get(SessionManager.KEY_EMAIL);
            age = Integer.parseInt(userDetails.get(SessionManager.KEY_AGE));
            phone = userDetails.get(SessionManager.KEY_PHONE);


            Log.e("userid:", userId + " ");
            Log.e("username:", name);
            Log.e("name:", username);
            Log.e("age:", age + " ");
            Log.e("phone:", phone);
            Log.e("email:", email);
        }

    }

    public void setCount(int c) {
        this.count = c;
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


//    public void putAvatar(){
//        Toast.makeText(HomeActivity.this, "putAvatar started", Toast.LENGTH_SHORT).show();
//        Response.Listener<String> responseListener = new Response.Listener<String>(){
//            @Override
//            public void onResponse(String response) {
//                Log.i("My Activity", response);
//                try {
//                    JSONObject jsonResponse = new JSONObject(response);
//                    boolean success = jsonResponse.getBoolean("success");
//                    if(success){
//                        Avatar = jsonResponse.getString("avatar");
//                        Toast.makeText(HomeActivity.this, Avatar, Toast.LENGTH_LONG).show();
////                        Intent intent = new Intent(HomeActivity.this, Login1Activity.class);
////                        Register2Activity.this.startActivity(intent);
//                    }else{
//                        Avatar= jsonResponse.getString("avatar");
//                        Toast.makeText(HomeActivity.this, Avatar, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//
//        AvatarRequest avatarRequest = new AvatarRequest(userId, responseListener);
//        Toast.makeText(HomeActivity.this, "AvatarRequest"+userId, Toast.LENGTH_SHORT).show();
//        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
//        queue.add(avatarRequest);
//    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            HomeActivity.this.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    }else{
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

}
