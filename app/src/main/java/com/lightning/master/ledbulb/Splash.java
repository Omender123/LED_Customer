package com.lightning.master.ledbulb;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lightning.master.ledbulb.HomeMain.Home_Drawer;
import com.lightning.master.ledbulb.Splash_log_reg.activity.Login_register;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public class Splash extends AppCompatActivity {

    Handler handler;
    SharedPreferences sharedPref;
    int delay_splash = 1300;
    int final_count = 0;
    //  JSONObject appinfo;
    String splash_stages = "2", ad_id_banner = "", ad_id_intersticial = "", priority_version_title, priority_version_description;
    JSONObject data_obj;
    public static  String google_map_api="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
// Get new Instance ID token
                        String token = task.getResult().getToken();
                        SharedPreferences.Editor editor=sharedPref.edit();
                        editor.putString("deviceToken",token);
                        editor.commit();
                        Log.d("token", token);
                    }
                });
        nostatusbar();

        if (!Utils.isNetworkConnected(getApplicationContext())) {
            // Util.dialogue_nointernet(this);
            DialogueCustom.dialogue_custom(this, "Network Error!",
                    "Uh-oh!",
                    "Slow or No Internet Connection.",
                    " Check Internet Settings ", "", false, R.drawable.nointernet,
                    "gotointernetsettings", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));

        } else {
            get_appinfo();
            init_handler();
            // dialogue_howto();
        }
    }


    private void init_handler() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Add_1();
                    }
                }, delay_splash);

    }

    public void Next_Activity() {

        if (sharedPref.getString("LoggediN", "").equals("true")) {

            Intent intent = new Intent(Splash.this, Home_Drawer.class);
            Bundle bundle = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                bundle = ActivityOptions.makeCustomAnimation(Splash.this, R.anim.enter, R.anim.exit).toBundle();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, bundle);
            }
            finish();

        } else {

            if (sharedPref.getString("tut_played", "").equals("true")) {
                Intent intent = new Intent(Splash.this, Login_register.class);
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bundle = ActivityOptions.makeCustomAnimation(Splash.this, R.anim.enter, R.anim.exit).toBundle();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, bundle);
                }
                finish();
            }else {

                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
            }
        }
    }


    public void get_appinfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call = service.getappinfo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);

                        String success = obj.getString("success");
                        String message = obj.getString("message");
                       google_map_api = obj.getString("google_map_api");

                        if (success.trim().equals("true")) {
                            data_obj = obj.getJSONObject("data");
                            //   appinfo = data_obj.optJSONObject("appinfo");
                            if (data_obj.length() > 0) {
                                sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editorlog = sharedPref.edit(); // Editor of shared
                                editorlog.putString("current_version", data_obj.optString("current_version"));
                                editorlog.putString("priority_version", data_obj.optString("priority_version"));
                                editorlog.putString("priority_version_title", data_obj.optString("priority_version_title"));
                                editorlog.putString("priority_version_description", data_obj.optString("priority_version_description"));
                                editorlog.putString("privacy_policy", data_obj.optString("privacy_policy"));
                                editorlog.putString("paid_enabled", data_obj.optString("paid_enabled"));
                                editorlog.putString("under_development", data_obj.optString("under_development"));
                                editorlog.putString("ad_enabled", data_obj.optString("ad_enabled"));
                                editorlog.putString("app_url", data_obj.optString("app_url"));
                                editorlog.putString("app_package_name", data_obj.optString("app_package_name"));
                                editorlog.putString("register_enabled", data_obj.optString("register_enabled"));
                                editorlog.putString("ad_id_intersticial", data_obj.optString("ad_id_intersticial"));
                                editorlog.putString("ad_id_banner", data_obj.optString("ad_id_banner"));
                                editorlog.putString("splash_stages", data_obj.optString("splash_stages"));
                                editorlog.putString("google_map_api", obj.optString("google_map_api"));


                                if (!obj.isNull("contactus_phone")){
                                    editorlog.putString("contactus_phone", obj.optString("contactus_phone"));
                                }else{
                                    editorlog.putString("contactus_phone", "");
                                }

                                if (!obj.isNull("link_facebook")){
                                    editorlog.putString("link_facebook", obj.optString("link_facebook"));
                                }else{
                                    editorlog.putString("link_facebook", "");
                                }
                                if (!obj.isNull("link_instagram")){
                                    editorlog.putString("link_instagram", obj.optString("link_instagram"));
                                }else{
                                    editorlog.putString("link_instagram", "");
                                }
                                if (!obj.isNull("link_twitter")){
                                    editorlog.putString("link_twitter", obj.optString("link_twitter"));
                                }else{
                                    editorlog.putString("link_twitter", "");
                                }
                                if (!obj.isNull("link_linkedin")){
                                    editorlog.putString("link_linkedin", obj.optString("link_linkedin"));
                                }else{
                                    editorlog.putString("link_linkedin", "");
                                }
                                if (!obj.isNull("link_youtube")){
                                    editorlog.putString("link_youtube", obj.optString("link_youtube"));
                                }else{
                                    editorlog.putString("link_youtube", "");
                                }

                                if (!obj.isNull("link_pinterest")){
                                    editorlog.putString("link_pinterest", obj.optString("link_pinterest"));
                                }else{
                                    editorlog.putString("link_pinterest", "");
                                }

                                if (!obj.isNull("term_conditions")){
                                    editorlog.putString("term_conditions", obj.optString("term_conditions"));
                                }else{
                                    editorlog.putString("term_conditions", "");
                                }
                                editorlog.commit();


                                priority_version_title = sharedPref.getString("priority_version_title", "");
                                priority_version_description = sharedPref.getString("priority_version_description", "");
                                ad_id_intersticial = sharedPref.getString("ad_id_intersticial", "");
                                ad_id_banner = sharedPref.getString("ad_id_banner", "");
                                splash_stages = data_obj.optString("splash_stages");

                                if (data_obj.optString("ad_enabled").equals("true")) {
                                    // init_ad();
                                    //  Toast.makeText(getApplicationContext(), ad_id_banner+"  "+ad_id_intersticial+ "  enabled ", Toast.LENGTH_SHORT).show();
                                }
                                compare_appversion(data_obj.optString("priority_version"));

                            } else {
                                DialogueCustom.dialogue_custom(Splash.this, "Something went Wrong!",
                                        "Aw Snap!",
                                        "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                                        "GOT IT", "", false, R.drawable.nointernet,
                                        "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                            }

                        } else {
                            DialogueCustom.dialogue_custom(Splash.this, "Something went Wrong!",
                                    "Aw Snap!!",
                                    "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                                    "GOT IT", "", false, R.drawable.nointernet,
                                    "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        DialogueCustom.dialogue_custom(Splash.this, "Something went Wrong!",
                                "Aw Snap!!!",
                                "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                                "GOT IT", "", false, R.drawable.nointernet,
                                "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                        Toast.makeText(Splash.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {

                } else if (response.code() == 404) {

                    SharedPreferences.Editor editorlog = sharedPref.edit();
                    editorlog.putString("app_url", "https://play.google.com/store/apps/details?id=com.lightning.master.ledbulb");
                    editorlog.commit();

                    DialogueCustom.dialogue_custom(Splash.this, "Update to Continue",
                            "An Important Update is Available",
                            "To Improve your Experience and Compatibilty, We have updated our app, Please Update to Continue",
                            "Update", "Exit", true, R.drawable.ic_notification,
                            "Update", "Exit", Color.parseColor("#1EBEA5"),
                            Color.parseColor("#FFA1A1A1"));

                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());

                    DialogueCustom.dialogue_custom(Splash.this, "Something went Wrong!",
                            "Aw Snap! Error code:" + response.code(),
                            "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                            "GOT IT", "", false, R.drawable.nointernet,
                            "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DialogueCustom.dialogue_custom(Splash.this, "Something went Wrong!",
                        "Aw Snap!!!!",
                        "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later " + t.getMessage(),
                        "GOT IT", "", false, R.drawable.nointernet,
                        "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));

            }
        });
    }

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @GET("appinfo")
        Call<ResponseBody> getappinfo();
    }

    public void compare_appversion(String priorversion) {
        try {
            android.content.pm.PackageInfo pInfo = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            String device_version = verCode + "" + version;
            device_version = device_version.replace(".", "");
            priorversion = priorversion.replace(".", "");
            //  Toast.makeText(this, priorversion+" "+device_version, Toast.LENGTH_SHORT).show();

            if (Integer.parseInt(priorversion) <= Integer.parseInt(device_version)) {
                if (data_obj.optString("under_development").toString().equals("false")) {
                    Add_1();
                } else {
                    DialogueCustom.dialogue_custom(this, "Important Alert",
                            "The App is under Maintenance",
                            "We are currently performing server maintenance. We'll be back shortly. Sorry for inconvenience, Please Try Again later",
                            "GOT IT", "", false, R.drawable.ic_maintenance,
                            "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                }
            } else {

                DialogueCustom.dialogue_custom(this, "Update to Continue",
                        "" + priority_version_title,
                        "" + priority_version_description,
                        "Update", "Exit", true, R.drawable.ic_notification,
                        "Update", "Exit", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));

            }

        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Add_1() {
        final_count = final_count + 1;
        //  Toast.makeText(this, "count "+final_count, Toast.LENGTH_SHORT).show();
        if (final_count == Integer.parseInt(splash_stages)) {
            //  Next Activity to be called here
            Next_Activity();
        }
    }

    private void nostatusbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}

