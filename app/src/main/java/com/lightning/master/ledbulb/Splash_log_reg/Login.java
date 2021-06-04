package com.lightning.master.ledbulb.Splash_log_reg;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.lightning.master.ledbulb.HomeMain.Home_Drawer;
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

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    Dialog dialog,dialog_progress;
    TextView tv_sign_up;
    TextInputEditText et_loginUsername,et_loginPassword;
    LinearLayout ll_edittextLayout,ll_OtpContent;
    EditText phoneEdittext,et_newPAssword;
    boolean OTPget=false;
    SharedPreferences sharedPreferences;
     SharedPreferences.Editor editor;
     Button btn_login;
    View root;

    String pnviewValue="",enteredMobile="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_login, container, false);
        init();

        return root;
    }

    void init()
    {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        tv_sign_up=(TextView) root.findViewById(R.id.tv_sign_up);
        et_loginUsername=(TextInputEditText) root.findViewById(R.id.et_loginUsername);
        et_loginPassword=(TextInputEditText) root.findViewById(R.id.et_loginPassword);
        btn_login=(Button)root.findViewById(R.id.btn_login);

        dialog=new Dialog(getActivity());
        onClicks();
    }
    void onClicks()
    {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitDetailsLogin();
//                startActivity(new Intent(getActivity(), Home.class));
            }
        });


        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTPget=false;
                pnviewValue="";
                enteredMobile="";
                ForgotPasswordDialog();
            }
        });
    }

    void submitDetailsLogin()
    {
         if (et_loginPassword.getText().toString().equals(""))
        {
            et_loginPassword.setError("Password Is Require To Login");
        }else{
             Bundle b=getArguments();

             login(b.getString("mobile"),et_loginPassword.getText().toString());
        }
    }


    public void login(String mobile,String password ) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.login(mobile,password);

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

                        if (success.equals("true"))
                        {

                            if (obj.has("data"))
                            {
                                editor=sharedPreferences.edit();
                                JSONObject dataObject=obj.getJSONObject("data");

                                if (!dataObject.isNull("user_id"))
                                {
                                    editor.putString("user_id",dataObject.getString("user_id"));
                                }else{
                                    editor.putString("user_id","");

                                }
                                if (!dataObject.isNull("user_fullname"))
                                {
                                    editor.putString("user_fullname",dataObject.getString("user_fullname"));

                                }else{
                                    editor.putString("user_fullname","");

                                }
                                if (!dataObject.isNull("user_mobile"))
                                {
                                    editor.putString("user_mobile",dataObject.getString("user_mobile"));

                                }else{
                                    editor.putString("user_mobile","");

                                }
                                if (!dataObject.isNull("user_email"))
                                {
                                    editor.putString("user_email",dataObject.getString("user_email"));

                                }else{
                                    editor.putString("user_email","");

                                }
                                if (!dataObject.isNull("user_password"))
                                {
                                    editor.putString("user_password",dataObject.getString("user_password"));

                                }else{
                                    editor.putString("user_password","");

                                }
                                if (!dataObject.isNull("user_type"))
                                {
                                    editor.putString("user_type",dataObject.getString("user_type"));

                                }else{
                                    editor.putString("user_type","");

                                }
                                if (!dataObject.isNull("user_gst_number"))
                                {
                                    editor.putString("user_gst_number",dataObject.getString("user_gst_number"));

                                }else{
                                    editor.putString("user_gst_number","");

                                }
                                if (!dataObject.isNull("user_status"))
                                {
                                    editor.putString("user_status",dataObject.getString("user_status"));

                                }else{
                                    editor.putString("user_status","");

                                }
                                if (!dataObject.isNull("user_added_date"))
                                {
                                    editor.putString("user_added_date",dataObject.getString("user_added_date"));

                                }else{
                                    editor.putString("user_added_date","");

                                }
                                if (!dataObject.isNull("user_device_token"))
                                {
                                    editor.putString("user_device_token",dataObject.getString("user_device_token"));

                                }else{
                                    editor.putString("user_device_token","");

                                }
                                if (!dataObject.isNull("user_code"))
                                {
                                    editor.putString("user_code",dataObject.getString("user_code"));

                                }else{
                                    editor.putString("user_code","1");

                                }
                                editor.putString("LoggediN","true");
                                editor.commit();
                                Toast.makeText(getActivity(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), Home_Drawer.class));
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity(), "No Data Available!!", Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            Toast.makeText(getActivity(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getActivity(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong at server!"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ForgotPassword(String mobile,String otp,String new1)
    {

        Log.i("values",mobile+"    "+otp+"    "+new1);
        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.forgetpassword(mobile,otp,new1);
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
                            Toast.makeText(getActivity(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            hideloader();
                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getActivity(), " " + message, Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            if (obj.has("otp")){
                                String otp = obj.getString("otp");
                                if (otp.equals("true")) {
                                    OTPget=true;
                                    Toast.makeText(getActivity(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    ForgotPasswordDialog();

                                } else {

                                }
                            }else{

                                    OTPget=false;
                                    if (dialog.isShowing()){
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(getActivity(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideloader();

                    } catch (Exception e) {
                        e.printStackTrace();
                        //       Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else if (response.code() == 401) {
                    Toast.makeText(getActivity(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    //        hideloader();
                } else {
                    Log.d("Message", "code..."+response.code() + " message..." + response.message()+" body..."+response.body());
                    Toast.makeText(getActivity(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later"+" message..." + response.message(), Toast.LENGTH_LONG).show();
                    //         hideloader();
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
                Toast.makeText(getActivity(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("login")
        @FormUrlEncoded
        Call<ResponseBody> login(@Field("mobile") String mobile,
                                          @Field("password") String password);

        @Headers("Authkey:APPLEDBDMPL")
        @POST("forgetpassword")
        @FormUrlEncoded
        Call<ResponseBody> forgetpassword	(@Field("phone") String phone,
                                              @Field("otp") String otp,
                                              @Field("new") String new1);

    }

    void ForgotPasswordDialog(){

        dialog.setContentView(R.layout.otp_verification_layout);
        dialog.setCanceledOnTouchOutside(false);

        ll_edittextLayout =(LinearLayout)dialog.findViewById(R.id.ll_edittextLayout) ;
        ll_OtpContent =(LinearLayout)dialog.findViewById(R.id.ll_OtpContent) ;

        final Pinview pinview=(Pinview)dialog.findViewById(R.id.pinview);
        ImageView img_cancel=(ImageView)dialog.findViewById(R.id.img_cancel);
        phoneEdittext=(EditText)dialog.findViewById(R.id.phoneEdittext);
        et_newPAssword=(EditText)dialog.findViewById(R.id.et_newPAssword);
        TextView tv_resend=(TextView)dialog.findViewById(R.id.tv_resend);
        tv_resend.setVisibility(View.GONE);
        Button btn_sendOTp=(Button)dialog.findViewById(R.id.btn_sendOTp);
        Button btn_submit=(Button)dialog.findViewById(R.id.btn_submit);

        if (OTPget==true)
        {
            ll_edittextLayout.setVisibility(View.GONE);
            ll_OtpContent.setVisibility(View.VISIBLE);
            et_newPAssword.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
        }
        else {
            ll_edittextLayout.setVisibility(View.VISIBLE);
            ll_OtpContent.setVisibility(View.GONE);
            et_newPAssword.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
        }

        dialog.show();

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_sendOTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneEdittext.getText().toString().equals("")){
                    phoneEdittext.setError("Please Enter Mobile Number For Get OTP");
                }else if (phoneEdittext.getText().toString().length()<=9){

                    phoneEdittext.setError("Please Enter Valid Mobile Number !");
                }else{
                    enteredMobile=phoneEdittext.getText().toString();
                    ForgotPassword(phoneEdittext.getText().toString(),"","");
                }
            }
        });
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                Toast.makeText(getContext(), pinview.getValue(), Toast.LENGTH_SHORT).show();


                pnviewValue= pinview.getValue();
//                ForgotPassword(phoneEdittext.getText().toString(),pinview.getValue(),et_newPAssword.getText().toString());
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_newPAssword.getText().toString().equals("")){
                    et_newPAssword.setError("Please Enter New Password");
                }else if (pinview.getValue().equals("")){

                    Toast.makeText(getActivity(), "Please Enter OTP!", Toast.LENGTH_SHORT).show();
                }else{
                    ForgotPassword(enteredMobile,pnviewValue,et_newPAssword.getText().toString());
                }
            }
        });
    }

    public void showloader(String message){
        dialog_progress = new Dialog(getActivity());
        dialog_progress.setContentView(R.layout.dialog_progress);
        dialog_progress.show();
        dialog_progress.setCancelable(false);

        TextView tv_text = (TextView) dialog_progress.findViewById(R.id.tv_text);
        tv_text.setText(message);
    }
    public void hideloader(){
        dialog_progress.hide();
    }
    public void vibrate_alert(int duration) {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(duration);
    }
}
