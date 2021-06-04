
package com.lightning.master.ledbulb.HomeMain;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
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

public class Edit_Profile extends AppCompatActivity {

    EditText update_name, update_email, update_phone, et_gstno;
    TextView btn_Update;
    Dialog dialog_progress;
    SharedPreferences sharedPref;
    Dialog dialog;
    LinearLayout gst_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        update_name = (EditText) findViewById(R.id.update_name);
        update_email = (EditText) findViewById(R.id.update_email);
        update_phone = (EditText) findViewById(R.id.update_phone);
        et_gstno = (EditText) findViewById(R.id.et_gstno);
        btn_Update = (TextView) findViewById(R.id.btn_Update);

        gst_layout = (LinearLayout) findViewById(R.id.gst_layout);


        update_name.setText(getIntent().getExtras().getString("fullname"));
        update_email.setText(getIntent().getExtras().getString("email"));
        update_phone.setText(getIntent().getExtras().getString("phone"));

        if (!getIntent().getExtras().getString("user_type").equals("personal")) {
            gst_layout.setVisibility(View.VISIBLE);
            et_gstno.setText(getIntent().getExtras().getString("gstno"));
        } else {
            gst_layout.setVisibility(View.GONE);
        }


        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (update_name.getText().toString().isEmpty()
                        || update_email.getText().toString().isEmpty()
                        || update_phone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill All Coloums", Toast.LENGTH_SHORT).show();
                }  else if(!update_email.getText().toString().matches(emailPattern))
                {
                    update_email.setError("Please Enter valid Email Address!");
                }
                else if (update_phone.getText().toString().length()<9)
                {
                    update_phone.setError("Please Enter Valid Mobile No.!");
                }

                else {
                    Update_Profile(sharedPref.getString("user_id", "")
                            , update_name.getText().toString()
                            , update_email.getText().toString(),
                            update_phone.getText().toString());
                }

            }
        });


    }

    public void Update_Profile(String user_id, final String fullname, String email, final String mobile) {
        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.update_profile(user_id, fullname, email, mobile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        Log.i("Resp success: ", obj + "");
//                        Toast.makeText(getActivity().getApplication(), ""+obj, Toast.LENGTH_SHORT).show();


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

                            if (obj.has("message")) {
                                String message = obj.getString("message");
                                if (message.equals("Profile updated successfully.")) {

                                    SharedPreferences.Editor editor=sharedPref.edit();
                                    editor.putString("user_fullname",fullname);
                                    editor.commit();
                                    Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Home_Drawer.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }



                            else  if (obj.has("otp")) {
                                String otp = obj.getString("otp");
                                if (otp.equals("true")) {

                                    otpVerificationDialog();

                                } else {

                                    Toast.makeText(getApplicationContext(), "Error !! Profile Not Updated ", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }
                        hideloader();

                    } catch (Exception e) {
                        e.printStackTrace();
                        hideloader();

                        //       Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
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
        dialog_progress = new Dialog(Edit_Profile.this);
        dialog_progress.setContentView(R.layout.dialog_progress);
        dialog_progress.show();
        dialog_progress.setCancelable(false);

        TextView tv_text = (TextView) dialog_progress.findViewById(R.id.tv_text);
        tv_text.setText(message);
    }

    public void hideloader() {
        dialog_progress.hide();
    }

    public void vibrate_alert(int duration) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(duration);
    }

    void otpVerificationDialog() {

        dialog = new Dialog(Edit_Profile.this);
        dialog.setContentView(R.layout.otp_verification_layout);
        dialog.setCanceledOnTouchOutside(false);

        Pinview pinview = (Pinview) dialog.findViewById(R.id.pinview);
        ImageView img_cancel = (ImageView) dialog.findViewById(R.id.img_cancel);
        TextView tv_resend = (TextView) dialog.findViewById(R.id.tv_resend);
        tv_resend.setVisibility(View.GONE);
        dialog.show();
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                resendotp();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                Toast.makeText(getApplicationContext(), pinview.getValue(), Toast.LENGTH_SHORT).show();

                verifyUpdates(sharedPref.getString("user_id", "")
                        , update_name.getText().toString()
                        , update_email.getText().toString()
                        , update_phone.getText().toString(), et_gstno.getText().toString(), pinview.getValue());

            }
        });
    }

    public void resendotp(String mobile) {
        final ProgressDialog progressDialog = new ProgressDialog(Edit_Profile.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.resendotp(mobile);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");

                        if (success.equals("true")) {
//                            otpVerificationDialog();
                            Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                            startActivity(new Intent(getActivity(),Home.class));
//                            getActivity().finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void verifyUpdates(String user_id, String fullname, String email, String mobile, String gst, String otp) {
        final ProgressDialog progressDialog = new ProgressDialog(Edit_Profile.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.verifyupdates(user_id, fullname, email, mobile, gst, otp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
//                        String success = obj.optString("success");
                        String message = obj.optString("message");

                        if (message.equals("Profile updated successfully.")) {


                            Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();

                            if (dialog.isShowing()) {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), Home_Drawer.class));
                                finish();
                            }
//                            startActivity(new Intent(getActivity(),Home.class));
//                            getActivity().finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("updateprofile/{user_id}")
        @FormUrlEncoded
        Call<ResponseBody> update_profile(@Path("user_id") String user_id,
                                          @Field("fullname") String fullname,
                                          @Field("email") String email,
                                          @Field("mobile") String mobile);

        @Headers("Authkey:APPLEDBDMPL")
        @POST("resendotp")
        @FormUrlEncoded
        Call<ResponseBody> resendotp(@Field("mobile") String mobile);

        @Headers("Authkey:APPLEDBDMPL")
        @POST("updateprofile/{user_id}")
        @FormUrlEncoded
        Call<ResponseBody> verifyupdates(@Path("user_id") String user_id,
                                         @Field("fullname") String fullname,
                                         @Field("email") String email,
                                         @Field("mobile") String mobile,
                                         @Field("gst_number") String gst_number,
                                         @Field("otp") String otp);

    }
}
