package com.lightning.master.ledbulb.HomeMain;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lightning.master.ledbulb.Add_Project.Existing_Project;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint_Detail;
import com.lightning.master.ledbulb.HomeMain.bottom.About_us;
import com.lightning.master.ledbulb.HomeMain.bottom.Contact_us;
import com.lightning.master.ledbulb.HomeMain.bottom.Earned_Points;
import com.lightning.master.ledbulb.LedChangePassword;
import com.lightning.master.ledbulb.Notification.Notification;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Splash_log_reg.activity.Login_register;
import com.lightning.master.ledbulb.TargetView;
import com.lightning.master.ledbulb.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class Home_Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback, TargetView.Targetview {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private int NUM_PAGES = 4;
    Dialog dialog_progress;


    //qr code scanner object
    private IntentIntegrator qrScan;
    SharedPreferences sharedPref;

    Dialog dialogue_success;
    ImageView iv_scan,iv_notification,logout;
    LinearLayout ll_service, ll_projects;
    TextView username;


    private GoogleMap mMap;
    LocationManager locationManager;
    LatLng myCoordinates;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    NavigationView navigationView;
    public static String latitude="",longatitude="",radioValue="",droppoint_id = "",address="";


    public static final int RUNTIME_PERMISSION_CODE = 1;
    boolean gpsenabled, isNetworkEnabled;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 2000;
    private static final float LOCATION_DISTANCE = 10;
    boolean isnetworkEnabled, loggedin = false, LoginAgain = false;





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("RestrictedApi")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_about:
                    startActivity(new Intent(Home_Drawer.this, About_us.class));
//                    finish();

                    return true;

                case R.id.navigation_Points:
                    startActivity(new Intent(Home_Drawer.this, Earned_Points.class));
//                    finish();
                    return true;

                case R.id.navigation_contact:

                    startActivity(new Intent(Home_Drawer.this, Contact_us.class));
//                    finish();
                    return true;

                case R.id.iv_scan:
//                    Toast.makeText(Home.this, "working", Toast.LENGTH_SHORT).show();
                    Utils.initiateScan(Home_Drawer.this);
//                    return true;
            }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("The Lighting Masters");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Utils.updatedtoken(this,sharedPreferences.getString("user_id",""),
                sharedPreferences.getString("deviceToken",""));

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        username = (TextView)hView.findViewById(R.id.username);
        username.setText(sharedPreferences.getString("user_fullname",""));

        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            ExitConfirm(this,this);
        }
    }
    @Override
    protected void onResume() {
        checkpermission();
        hideItem();
        super.onResume();
    }


    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        MenuItem fbMenu=nav_Menu.findItem(R.id.nav_Facebook);
        MenuItem instaMenu=nav_Menu.findItem(R.id.nav_Instagram);
        MenuItem linkedMenu=nav_Menu.findItem(R.id.nav_LinkedIn);
        MenuItem youtubeMenu=nav_Menu.findItem(R.id.nav_YouTube);
        MenuItem pintrestMenu=nav_Menu.findItem(R.id.nav_Pinterest);
        MenuItem twitterMenu=nav_Menu.findItem(R.id.nav_Twitter);

        if (sharedPreferences.getString("link_facebook","").equals("")){
            fbMenu.setVisible(false);
        }else{
            fbMenu.setVisible(true);
        }
        if (sharedPreferences.getString("link_instagram","").equals("")){
            instaMenu.setVisible(false);
        }else{
            instaMenu.setVisible(true);
        }
        if (sharedPreferences.getString("link_twitter","").equals("")){
            twitterMenu.setVisible(false);
        }else{
            twitterMenu.setVisible(true);
        }
        if (sharedPreferences.getString("link_linkedin","").equals("")){
            linkedMenu.setVisible(false);
        }else{
            linkedMenu.setVisible(true);
        }
        if (sharedPreferences.getString("link_youtube","").equals("")){
            youtubeMenu.setVisible(false);
        }else{
            youtubeMenu.setVisible(true);
        }
        if (sharedPreferences.getString("link_pinterest","").equals("")){
            pintrestMenu.setVisible(false);
        }else{
            pintrestMenu.setVisible(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home__drawer, menu);
//        getMenuInflater().inflate(R.menu.activity_home__drawer_drawer, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_notification) {
            startActivity(new Intent(Home_Drawer.this, Notification.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
                    Intent intent = new Intent(getApplicationContext(),User_Profile.class);
                    startActivity(intent);// Handle the camera action
        } else if (id == R.id.complaints) {

            Complaint.i=1;
            Intent intent = new Intent(getApplicationContext(), Complaint.class);
            startActivity(intent);


        } else if (id == R.id.setting) {
            Intent intent = new Intent(getApplicationContext(), LedChangePassword.class);
            startActivity(intent);

        } else if (id == R.id.logout) {

            new AlertDialog.Builder(Home_Drawer.this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("Logout Confirmation")
                    .setMessage("Are you sure to want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logoutaction(sharedPreferences.getString("deviceToken",""));
                        }

                    })

                    .setNegativeButton("No", null)
                    .show();

        } else if (id == R.id.nav_share) {

            try {
                Intent share = new Intent(Intent.ACTION_SEND);

                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Download LED App !! - " +
                        ":\n https://play.google.com/store/apps/details?id="+getPackageName()+" \n invite code : "+" "+sharedPreferences.getString("user_code",""));
                startActivity(Intent.createChooser(share, "Share using"));
            } catch(Exception e) {
//e.toString();
            }

        } else if (id == R.id.nav_Facebook) {

            openUrl(sharedPreferences.getString("link_facebook",""));

        }
        else if (id == R.id.nav_Instagram) {

            openUrl(sharedPreferences.getString("link_instagram",""));
        }
        else if (id == R.id.nav_Twitter) {

            openUrl(sharedPreferences.getString("link_twitter",""));
        }
        else if (id == R.id.nav_LinkedIn) {

            openUrl(sharedPreferences.getString("link_linkedin",""));
        }
        else if (id == R.id.nav_YouTube) {

            openUrl(sharedPreferences.getString("link_youtube",""));
        }
        else if (id == R.id.nav_Pinterest) {

            openUrl(sharedPreferences.getString("link_pinterest",""));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

              return true;
    }

    void openUrl(String url){
//        String url = "http://www.example.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    void init()
    {
        qrScan = new IntentIntegrator(this);

//        iv_scan = (ImageView) findViewById(R.id.iv_scan);
        logout = (ImageView) findViewById(R.id.logout);

        iv_notification=(ImageView)findViewById(R.id.iv_notification);

// Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/trench.ttf");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        ll_service=(LinearLayout) findViewById(R.id.ll_service);

        ll_projects=(LinearLayout)findViewById(R.id.ll_project);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

// layouts of all welcome sliders
// add few more layouts if you want
        layouts = new int[]{
                R.layout.slide1,
                R.layout.slide2,
                R.layout.slide3};

// adding bottom dots
        addBottomDots(0);

// making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new Home_Drawer.MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        nostatusbar();;
        onClicks();
    }

    void onClicks()
    {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Home_Drawer.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Logout Confirmation")
                        .setMessage("Are you sure to want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               logoutaction(sharedPreferences.getString("deviceToken",""));
                            }

                        })

                        .setNegativeButton("No", null)
                        .show();

            }
        });

        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Drawer.this, Notification.class));
//                finish();
            }
        });


        ll_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Drawer.this, Complaint.class));
//                finish();
            }
        });

        ll_projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue_success();
            }

            public void dialogue_success() {
// Create custom dialog object
                dialogue_success = new Dialog(Home_Drawer.this);
                dialogue_success.setContentView(R.layout.dialogue_choice_project);
                dialogue_success.show();
                dialogue_success.setCancelable(false);

                ImageView cross = (ImageView) dialogue_success.findViewById(R.id.img_cross);
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogue_success.dismiss();
// startActivity(new Intent(getApplicationContext(), Home.class));
                    }
                });

                TextView tv_newProject = (TextView) dialogue_success.findViewById(R.id.tv_catalog);
                tv_newProject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogue_success.dismiss();
                        startActivity(new Intent(getApplicationContext(), Existing_Project.class)
                                .putExtra("ProjectType","New"));
//                        finish();
                    }
                });
                TextView tv_Existing_Project = (TextView) dialogue_success.findViewById(R.id.tv_price);
                tv_Existing_Project.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogue_success.dismiss();
                        startActivity(new Intent(getApplicationContext(), Existing_Project.class)
                                .putExtra("ProjectType","Existing"));
//                        finish();
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
//if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "No Result Found", Toast.LENGTH_LONG).show();
            } else {
                Check_Booking_QR(sharedPreferences.getString("user_id",""),result.getContents());
//

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);

    }

    // viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            currentPage = position;
// Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_LONG).show();
// changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
// last page. make button text to GOT IT

            } else {

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {


        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
//currentPage=arg0-1;
// Toast.makeText(getApplicationContext()," "+arg0,Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }



    void getLatLong() {
        isLocationEnabled();
        if (!isLocationEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert")
                    .setMessage("GPS is Not Available..!!! Please Turn On Location To Get Nearest Service.")
                    .setPositiveButton("Turn on GPS ",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {

        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mylog", "Not granted");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
                checkpermission();
        } else
            checkpermission();
    }

    protected boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }


    public void checkpermission() {

        if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION))
                + (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{

                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,

                    },
                    1);
            // Check Permissions Now
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            // getting GPS status
            gpsenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gpsenabled && !isNetworkEnabled) {
                // no GPS Provider and no network provider is enabled
                Toast.makeText(this, "Please Enable Location To High Accuracy", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } else {
                getLocation();
            }
        }

    }


    public void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsenabled = locationManager.isProviderEnabled(GPS_PROVIDER);
        isnetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER);

        LocationListener[] mLocationListeners = new LocationListener[]{
                new com.lightning.master.ledbulb.LocationListener(LocationManager.GPS_PROVIDER, this)
                , new com.lightning.master.ledbulb.LocationListener(NETWORK_PROVIDER, this)
        };
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            mLocationManager.requestLocationUpdates(
                    NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.d("Home Container", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("Container", "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.d("Container", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {

            Log.d("Container", "gps provider does not exist " + ex.getMessage());
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RUNTIME_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkpermission();
                } else {
                    finish();
                }
                return;
            }

        }
    }


    @Override
    public void locationchange(android.location.Location location) {
        latitude = location.getLatitude() + "";
        longatitude = location.getLongitude() + "";


    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    private void nostatusbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void Check_Booking_QR(String user_id, String booking_id)
    {
// Log.i("user_id ", user_id + "");
        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Home_Drawer.Service service = retrofit.create(Home_Drawer.Service.class);
        Call<ResponseBody> call;
        call = service.check_booking_qr(user_id,booking_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
// Toast.makeText(getActivity().getApplication(), "this is working"+obj, Toast.LENGTH_SHORT).show();
                        Log.i("Resp success: ", obj + "");

                        hideloader();

                        if (obj.has("error")) {
                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            hideloader();
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            hideloader();
                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else {
// JSONArray arr_response = obj.optJSONArray("id");
                            String id = obj.getString("id");
                            // Toast.makeText(Home.this, "Working "+id, Toast.LENGTH_SHORT).show();

                            if (dialog_progress.isShowing()){
                                dialog_progress.dismiss();
                                startActivity(new Intent(getApplicationContext(), Complaint_Detail.class)
                                        .putExtra("booking_id",id));

                            }else{

                                startActivity(new Intent(getApplicationContext(), Complaint_Detail.class)
                                        .putExtra("booking_id",id));

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        hideloader();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    hideloader();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                    hideloader();
/* DialogueCustom.dialogue_custom(SplashActivity.this,"Something went Wrong!",
"Aw Snap! Error code:"+response.code(),
"An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
"GOT IT","",false, R.drawable.error,
"exit","", Color.parseColor("#1EBEA5"),Color.parseColor("#FFA1A1A1"));*/

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideloader();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showloader(String message) {
        dialog_progress = new Dialog(Home_Drawer.this);
        dialog_progress.setContentView(R.layout.dialog_progress);
        dialog_progress.setCancelable(false);
        dialog_progress.show();

        TextView tv_text = (TextView) dialog_progress.findViewById(R.id.tv_text);
        tv_text.setText(message);
    }

    public void hideloader() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        dialog_progress.dismiss();
                    }
                }, 1200);
    }

    public void vibrate_alert(int duration) {
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        v.vibrate(duration);
    }

    public void logoutaction(String token)
    {
// Log.i("user_id ", user_id + "");
        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.logoutaction(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
// Toast.makeText(getActivity().getApplication(), "this is working"+obj, Toast.LENGTH_SHORT).show();
                        Log.i("Resp success: ", obj + "");


                        if (obj.has("error")) {
                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            hideloader();
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            hideloader();
                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Logout Successfully!", Toast.LENGTH_LONG).show();

                            if (dialog_progress.isShowing()){
                                dialog_progress.dismiss();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), Login_register.class);
                                startActivity(intent);
                                finish();
                            }else{
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), Login_register.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                        hideloader();

                    } catch (Exception e) {
                        e.printStackTrace();
                        hideloader();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    hideloader();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                    hideloader();
/* DialogueCustom.dialogue_custom(SplashActivity.this,"Something went Wrong!",
"Aw Snap! Error code:"+response.code(),
"An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
"GOT IT","",false, R.drawable.error,
"exit","", Color.parseColor("#1EBEA5"),Color.parseColor("#FFA1A1A1"));*/

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideloader();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface Service {

        @Headers("Authkey:APPLEDBDMPL")
        @POST("checkbookingqr")
        @FormUrlEncoded
        Call<ResponseBody> check_booking_qr(@Field("user_id") String user_id,
                                            @Field("booking_id") String booking_id);


        @Headers("Authkey:APPLEDBDMPL")
        @POST("logoutaction")
        @FormUrlEncoded
        Call<ResponseBody> logoutaction(@Field("token") String token);

    }


//    void getLATLONG(){
//        if (Utils.isNetworkConnected(this)) {
////            tvDetectLocation.getRootView().setBackgroundColor(Color.WHITE);
//            isLocationEnabled();
//            if (!isLocationEnabled()) {
//                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
//                builder.setTitle("GPS not available.")
//                        .setMessage("Turn on device GPS and set to high accuracy.")
//                        .setPositiveButton("Turn on GPS ",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                androidx.appcompat.app.AlertDialog alert = builder.create();
//                alert.show();
//            }else {
//
//                if (Build.VERSION.SDK_INT >= 23) {
//                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        Log.d("mylog", "Not granted");
//                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//                    } else
//                        requestLocation();
//                } else
//                    requestLocation();
//            }
//
//        }else {
//            Snackbar snackbar = Snackbar
//                    .make(iv_notification, "Slow or No Connection", Snackbar.LENGTH_LONG).setDuration(4000);
//            snackbar.show();
//        }
//    }

//    private void requestLocation() {
//
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                Location mCurrentLocation = locationResult.getLastLocation();
//
//                double lat=mCurrentLocation.getLatitude();
//                double longa=mCurrentLocation.getLongitude();
//                latitude=String.valueOf(lat);
//                longatitude=String.valueOf(longa);
//
////                Intent intent = new Intent(this, MapsActivity.class);
////                intent.putExtra("lat",latitude);
////                intent.putExtra("long",longatitude);
////                startActivity(intent);
//
//                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//                String cityName = getCityName(myCoordinates);
//
//            }
//        };
//
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
//        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
//        String provider = locationManager.getBestProvider(criteria, true);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        Location location = locationManager.getLastKnownLocation(provider);
//        Log.d("mylog", "In Requesting Location");
//        if (location != null && (System.currentTimeMillis() - location.getTime()) <= 1000 * 2) {
//            myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            try {
//                List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
//                String address = addresses.get(0).getAddressLine(0);
//
////                myCity = addresses.get(0).getLocality();
//                Log.d("mylog", "Complete Address: " + addresses.toString());
//                Log.d("mylog", "Address: " + address);
//
////                Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            String cityName = getCityName(myCoordinates);
//            //  Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
//        } else {
//            LocationRequest locationRequest = new LocationRequest();
//            locationRequest.setNumUpdates(1);
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            Log.d("mylog", "Last location too old getting new location!");
//            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//            mFusedLocationClient.requestLocationUpdates(locationRequest,
//                    mLocationCallback, Looper.myLooper());
//        }
//    }
    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    public void ExitConfirm(final Context c,final Activity activity1) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(c);

        alert.setTitle("Alert");
        alert.setMessage("Are Your Sure To Want To Exit");
        alert.setIcon(R.drawable.ic_info);
        alert.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        activity1.finish();
                    }
                });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

}
