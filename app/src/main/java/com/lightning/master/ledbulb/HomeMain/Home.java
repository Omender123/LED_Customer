package com.lightning.master.ledbulb.HomeMain;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lightning.master.ledbulb.Add_Project.Existing_Project;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint_Detail;
import com.lightning.master.ledbulb.HomeMain.bottom.About_us;
import com.lightning.master.ledbulb.HomeMain.bottom.Contact_us;
import com.lightning.master.ledbulb.HomeMain.bottom.Earned_Points;
import com.lightning.master.ledbulb.Notification.Notification;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Splash_log_reg.activity.Login_register;
import com.lightning.master.ledbulb.Utils;

import org.json.JSONObject;

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

public class Home extends AppCompatActivity implements View.OnClickListener {


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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("RestrictedApi")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_about:
                    startActivity(new Intent(Home.this, About_us.class));
//                    finish();

                    return true;

                case R.id.navigation_Points:
                    startActivity(new Intent(Home.this, Earned_Points.class));
//                    finish();
                    return true;

                case R.id.navigation_contact:

                    startActivity(new Intent(Home.this, Contact_us.class));
//                    finish();
                    return true;

                case R.id.iv_scan:
//                    Toast.makeText(Home.this, "working", Toast.LENGTH_SHORT).show();
                    Utils.initiateScan(Home.this);
//                    return true;
            }
            return true;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

// SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
// String name = pref.getString("regId","");
// Toast.makeText(Home.this, "new token "+name, Toast.LENGTH_SHORT).show();

        Utils.updatedtoken(this,
                sharedPreferences.getString("user_id",""),
                sharedPreferences.getString("deviceToken",""));

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        init();
    }



    void init()
    {
        qrScan = new IntentIntegrator(this);

        logout = (ImageView) findViewById(R.id.logout);

        iv_notification=(ImageView)findViewById(R.id.iv_notification);

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

        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
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
                new AlertDialog.Builder(Home.this)
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
                startActivity(new Intent(Home.this, Notification.class));
//                finish();
            }
        });


        ll_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Complaint.class));
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
                dialogue_success = new Dialog(Home.this);
                dialogue_success.setContentView(R.layout.dialogue_choice_project);
                dialogue_success.show();
                dialogue_success.setCancelable(false);

                ImageView cross = (ImageView) dialogue_success.findViewById(R.id.img_cross);
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogue_success.dismiss();
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
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
//initiating the qr code scan
// qrScan.initiateScan();
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

    public void logoutaction(String token)
    {
// Log.i("user_id ", user_id + "");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Home_Drawer.Service service = retrofit.create(Home_Drawer.Service.class);
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
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            progressDialog.dismiss();
                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Logout Successfully!", Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), Login_register.class);
                            startActivity(intent);
                            finish();
                        }
                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
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
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.check_booking_qr(user_id,booking_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
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
                            String id = obj.getString("id");
                            startActivity(new Intent(getApplicationContext(), Complaint_Detail.class)
                                    .putExtra("booking_id",id));
                            finish();
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
        dialog_progress = new Dialog(Home.this);
        dialog_progress.setContentView(R.layout.dialog_progress);
        dialog_progress.show();
        dialog_progress.setCancelable(false);

        TextView tv_text = (TextView) dialog_progress.findViewById(R.id.tv_text);
        tv_text.setText(message);
    }

    public void hideloader() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        dialog_progress.hide();
                    }
                }, 1200);
    }

    public void vibrate_alert(int duration) {
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        v.vibrate(duration);
    }
    public interface Service {

        @Headers("Authkey:APPLEDBDMPL")
        @POST("checkbookingqr")
        @FormUrlEncoded
        Call<ResponseBody> check_booking_qr(@Field("user_id") String user_id,
                                            @Field("booking_id") String booking_id);

    }

}