package com.lightning.master.ledbulb.HomeMain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Utils;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class User_Profile extends AppCompatActivity {
    TextView my_name,my_email,my_phone_no,my_type,my_gstcode,my_refercode,et_edit_profile;
    Button edit_btn;
    SharedPreferences sharedPref;
    Dialog dialog_progress;
    LinearLayout ll_gst;
    String user_type="",fullname="",email="",phone="",gstno="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        my_name=(TextView)findViewById(R.id.my_name);
        my_email=(TextView)findViewById(R.id.my_email);
        my_phone_no=(TextView)findViewById(R.id.my_phone_no);
        my_type=(TextView)findViewById(R.id.my_type);
        my_gstcode=(TextView)findViewById(R.id.my_gstcode);
        my_refercode=(TextView)findViewById(R.id.my_refercode);
        et_edit_profile=(TextView)findViewById(R.id.et_edit_profile);
        ll_gst=(LinearLayout)findViewById(R.id.ll_gst);


        et_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Edit_Profile.class)
                        .putExtra("user_type",user_type)
                        .putExtra("fullname",fullname)
                        .putExtra("gstno",gstno)
                        .putExtra("email",email)
                        .putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });

        User_Profile(sharedPref.getString("user_id",""));
//        Toast.makeText(this, "work"+sharedPref.getString("user_id",""), Toast.LENGTH_SHORT).show();


    }
    public void User_Profile(String user_id)
    {
        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.user_profile("",user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
//                        Toast.makeText(getActivity().getApplication(), "this is working"+obj, Toast.LENGTH_SHORT).show();
                        Log.i("Respsuccess: ", obj + "");


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

                            String user_id= obj.getString("user_id");

                            if (!obj.isNull("user_fullname")) {
                                my_name.setText(obj.getString("user_fullname"));
                                fullname=obj.getString("user_fullname");
                            } else {
                                my_name.setText("");
                            }

                            if (!obj.isNull("user_mobile")) {
                                my_phone_no.setText(obj.getString("user_mobile"));
                                phone=obj.getString("user_mobile");
                            } else {
                                my_phone_no.setText("");
                            }
                            if (!obj.isNull("user_email")) {
                                my_email.setText(obj.getString("user_email"));
                                email=obj.getString("user_email");
                            } else {
                                my_email.setText("");
                            }

                            if (!obj.isNull("user_type")) {

                                user_type=obj.getString("user_type");
                                my_type.setText(obj.getString("user_type"));
                            } else {
                                my_type.setText("");

                            }
                            if(obj.getString("user_type").equals("personal"))
                            {
                                ll_gst.setVisibility(View.GONE);
                            }
                            else
                            {
                                if (!obj.isNull("user_gst_number")) {
                                    gstno=obj.getString("user_gst_number");
                                    my_gstcode.setText(obj.getString("user_gst_number"));
                                } else {
                                    my_gstcode.setText("");
                                }
                            }

                            if (!obj.isNull("user_code")) {
                                my_refercode.setText(obj.getString("user_code"));
                            } else {
                                my_refercode.setText("");
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


    public void showloader(String message) {
        dialog_progress = new Dialog(this);
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

        @Headers({"Authkey:APPLEDBDMPL"})
        @FormUrlEncoded
        @POST("customerprofile/{user_id}")
        Call<ResponseBody> user_profile(@Field("id") String id, @Path("user_id") String user_id);
    }
}
