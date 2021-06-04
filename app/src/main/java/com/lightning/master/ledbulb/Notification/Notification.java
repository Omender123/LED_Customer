package com.lightning.master.ledbulb.Notification;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lightning.master.ledbulb.HomeMain.Home_Drawer;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class Notification extends AppCompatActivity {

    RecyclerView recyclerView_notification;
    SharedPreferences sharedPref;
    Dialog dialog_progress;
    ImageView close_notification,image_booking;
    LinearLayout ll_noresult,recycle_layout,ll_clear_notification;
    Button clear_nptification;

    public static ArrayList<String> ID = new ArrayList<>();
    public static ArrayList<String> NOTIFICATION = new ArrayList<>();
    public static ArrayList<String> USER_ID = new ArrayList<>();
    public static ArrayList<String> EVENT = new ArrayList<>();
    public static ArrayList<String> ADDED = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        close_notification=(ImageView) findViewById(R.id.close_notification);
        clear_nptification=(Button) findViewById(R.id.clear_nptification);

        close_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clear_nptification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear_Notification(sharedPref.getString("user_id",""));
            }
        });
        ll_noresult=(LinearLayout) findViewById(R.id.ll_noresult);
        recycle_layout=(LinearLayout) findViewById(R.id.recycle_layout);
        ll_clear_notification=(LinearLayout) findViewById(R.id.ll_clear_notification);

        recyclerView_notification=(RecyclerView)findViewById(R.id.recyclerView_notification);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(Notification.this);

        Get_Notfication_Api(sharedPref.getString("user_id",""));
    }


    public  void Get_Notfication_Api(String user_id)
    {
        Log.i("user_id ", user_id + "");

        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.getnotification(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
//                        Toast.makeText(getActivity().getApplication(), "this is working"+obj, Toast.LENGTH_SHORT).show();
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
                            JSONArray arr_response = obj.optJSONArray("data");
                            if (arr_response.length() > 0) {

                                ID.clear();
                                NOTIFICATION.clear();
                                USER_ID.clear();
                                EVENT.clear();
                                ADDED.clear();

                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);
                                    //////////////    Adding to Datamodel   ////////////////
                                    ID.add(jsonObject.optString("id"));
                                    NOTIFICATION.add(jsonObject.optString("text"));
                                    //   Toast.makeText(Notification_Activity.this, ""+NOTIFICATION, Toast.LENGTH_SHORT).show();
                                    USER_ID.add(jsonObject.optString("USER_ID"));
                                    EVENT.add(jsonObject.optString("EVENT"));
                                    ADDED.add(jsonObject.optString("date_added"));
                                    //     Toast.makeText(Notification_Activity.this, ""+ADDED, Toast.LENGTH_SHORT).show();
                                }

//                                Toast.makeText(getActivity(), "Size: "+COMMENTS_arylst, Toast.LENGTH_SHORT).show();
                                Setup_Notification();


                                     hideloader();

                            } else {
                                      hideloader();
                                ll_noresult.setVisibility(View.VISIBLE);
                                ll_clear_notification.setVisibility(View.GONE);
//                                recycle_layout.setVisibility(View.GONE);
//                                clear_nptification.setVisibility(View.GONE);
//                                Toast.makeText(getApplicationContext(), "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();
                            }
                        }
//                        ll_clear_notification.setVisibility(View.GONE);
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

    private void Setup_Notification() {
        ll_noresult.setVisibility(View.GONE);
//                                recycle_layout.setVisibility(View.VISIBLE);
        ll_clear_notification.setVisibility(View.VISIBLE);
        recyclerView_notification.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_notification.setVisibility(View.VISIBLE);
        int numberOfColumns = 1;
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_notification.setLayoutManager(verticalLayoutManager);

        recyclerView_notification.setAdapter(new Notification_Adapter(getApplicationContext()));
    }

    public void Clear_Notification(String user_id)
    {
        Log.i("user_id ", user_id + "");

        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.clear_notification(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
//                        Toast.makeText(getApplicationContext(), "this is working"+obj, Toast.LENGTH_SHORT).show();
                        Log.i("Resp success: ", obj + "");


                        if (obj.has("error")) {
                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                                 hideloader();
                            Toast.makeText(getApplicationContext(), " error" + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                              hideloader();
                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " warning" + message, Toast.LENGTH_LONG).show();

                        } else {
                            hideloader();
                            String message = obj.getString("message");
                            if (message.equals("All notifications removed.")) {
                                Toast.makeText(Notification.this, " "+message, Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(getApplicationContext(), Home_Drawer.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(Notification.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                        hideloader();

                    } catch (Exception e) {
                        e.printStackTrace();
                          hideloader();
                        Log.i("Catch response: ", e.getMessage() + "");
                        Toast.makeText(getApplicationContext(), "catch" + e.getMessage(), Toast.LENGTH_LONG).show();
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
        dialog_progress = new Dialog(this);
        dialog_progress.setContentView(R.layout.dialog_progress);
        dialog_progress.getWindow().setBackgroundDrawableResource(R.drawable.backkkk);
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
            @FormUrlEncoded
            @POST("getnotifications")
            @Headers({"Authkey:APPLEDBDMPL"})
            Call<ResponseBody> getnotification(@Field("user_id") String user_id);

            @FormUrlEncoded
            @POST("clearnotifications")
            @Headers({"Authkey:APPLEDBDMPL"})
            Call<ResponseBody> clear_notification(@Field("user_id") String user_id);
    }
    @Override
    public void onBackPressed() {
// super.onBackPressed();
//        startActivity(new Intent(Notification.this, Home.class));
        finish();
    }
}
