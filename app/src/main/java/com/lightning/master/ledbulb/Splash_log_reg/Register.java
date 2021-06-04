package com.lightning.master.ledbulb.Splash_log_reg;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
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


public class Register extends Fragment {


    TextInputEditText et_register_name ,et_register_email ,et_register_mobileno,
    et_register_password ,et_register_confirmPassword,et_register_gstNo,et_referral_code;
    TextView tv_sign_up;
    TextInputLayout tv_gst;
    SharedPreferences sharedPref;
    Dialog dialog_progress;

    Button btn_sign, btn_apply, btn_applied;
    RadioGroup radioGroup;
    String radioValue="personal",deviceToken="",gstNumber="",mobileNumber="";
    Bundle b;
    Dialog dialog;
    View root;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_register, container, false);

         init();
        return root;
    }

    void init()
    {

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        b=getArguments();
        btn_sign =(Button) root.findViewById(R.id.btn_sign);
        tv_gst=(TextInputLayout)root.findViewById(R.id.tv_gst);

        radioGroup = (RadioGroup) root.findViewById(R.id.radioGroup1);

        et_register_name = (TextInputEditText) root.findViewById(R.id.et_register_name);
        et_register_email = (TextInputEditText) root.findViewById(R.id.et_register_email);
        et_register_mobileno = (TextInputEditText) root.findViewById(R.id.et_register_mobileno);
        et_register_password = (TextInputEditText) root.findViewById(R.id.et_register_password);
        et_register_confirmPassword = (TextInputEditText) root.findViewById(R.id.et_register_confirmPassword);
        et_register_gstNo = (TextInputEditText) root.findViewById(R.id.et_register_gstNo);
        et_referral_code = (TextInputEditText) root.findViewById(R.id.et_referral_code);
        btn_apply = (Button)root.findViewById(R.id.btn_apply);
        btn_applied = (Button) root.findViewById(R.id.btn_applied);

        if (!b.getString("mobile").equals("")){

            et_register_mobileno.setText(b.getString("mobile"));
            et_register_mobileno.setEnabled(false);
        }
        onClicks();
    }
    void onClicks()
    {
        btn_sign.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            otpVerificationDialog();

            SubmitDetailsRegister();
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
//            transaction.addToBackStack(null);
//            transaction.replace(R.id.container_splash, new Login());
//            transaction.commit();

        }
    });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_referral_code.getText().toString().isEmpty()) {
                    et_referral_code.setError("Enter Referral Code");
                } else {
                    Check_Referral_Code(et_referral_code.getText().toString());
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedId) {

                // find which radio button is selected
                if(checkedId == R.id.rbtn_personal) {

                    tv_gst.setVisibility(View.GONE);
                    radioValue="personal";

                } else if(checkedId == R.id.rbtn_enterprises) {

                    tv_gst.setVisibility(View.VISIBLE);
                    radioValue="enterprises";
                }
            }
        });
    }
    void otpVerificationDialog()
    {
         dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.otp_verification_layout);
        dialog.setCanceledOnTouchOutside(false);

        Pinview pinview=(Pinview)dialog.findViewById(R.id.pinview);
        ImageView img_cancel=(ImageView)dialog.findViewById(R.id.img_cancel);
        TextView tv_resend=(TextView)dialog.findViewById(R.id.tv_resend);

        dialog.show();
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendotp(mobileNumber);
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.container_splash,new WelcomeScreen());
                fragmentTransaction.commit();
            }
        });
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
//                Toast.makeText(getContext(), pinview.getValue(), Toast.LENGTH_SHORT).show();

                verifyregister(mobileNumber,pinview.getValue());

            }
        });
    }

    void SubmitDetailsRegister()
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (et_register_name.getText().toString().equals(""))
        {
            et_register_name.setError("Please Enter Your Name!");
        }else if (et_register_email.getText().toString().equals(""))
        {
            et_register_email.setError("Please Enter Email Address!");
        }else if (!et_register_email.getText().toString().matches(emailPattern))
        {
            et_register_email.setError("Please Enter valid Email Address!");
        }else if (et_register_mobileno.getText().toString().equals(""))
        {
            et_register_mobileno.setError("Please Enter Mobile No.!");
        }else if (et_register_mobileno.getText().toString().length()<9)
        {
            et_register_mobileno.setError("Please Enter Valid Mobile No.!");
        }
        else if (et_register_password.getText().toString().equals(""))
        {
            et_register_password.setError("Please Create Your Password!");
        }else if (et_register_confirmPassword.getText().toString().equals(""))
        {
            et_register_confirmPassword.setError("Please Enter Your Password to confirm!");
        }else if (radioValue.equals(""))
        {
            Toast.makeText(getActivity(), "Please Select Your One Type!", Toast.LENGTH_SHORT).show();
        }else if (radioValue.equals("enterprises")  && et_register_gstNo.getText().toString().equals(""))
        {
            et_register_gstNo.setError("Please Enter Your GST No.!");
        }else if (!et_register_password.getText().toString().equals(et_register_confirmPassword.getText().toString()))
        {
            et_register_confirmPassword.setError("Please Match Your Password Carefully!");
        }else{

            gstNumber=et_register_gstNo.getText().toString();
            mobileNumber=et_register_mobileno.getText().toString();
            register(et_register_name.getText().toString(),
                    et_register_mobileno.getText().toString(),
                    et_register_email.getText().toString(),
                    et_register_password.getText().toString(),
                    et_referral_code.getText().toString(),
                    radioValue,deviceToken,gstNumber);
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
//            transaction.addToBackStack(null);
//            transaction.replace(R.id.container_splash, new Login());
//            transaction.commit();
        }
    }


    public void register(String fullname ,String mobile ,String email ,String password ,String refferal ,String type,
                         String device_token ,String gst_number) {



        Log.i("register_fullname",fullname+"");

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Checking User...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.register(fullname,mobile,email,password,refferal,type,device_token,gst_number);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);

                        Log.i("register_obj",obj+"");
                        String success = obj.optString("success");
                        String message = obj.optString("message");

                        if (success.equals("true"))
                        {
                            otpVerificationDialog();
                        }else{
                            Toast.makeText(getActivity(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.i("register_Exception",e.getMessage()+"");
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
                Log.i("register_t",t.getMessage()+"");
                Toast.makeText(getActivity(), "Something went wrong at server!"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void verifyregister(String mobile ,String email ) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Verifing User...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.verifyregister(mobile,email);

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

    public void resendotp(String mobile ) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

                        if (success.equals("true"))
                        {
//                            otpVerificationDialog();
                            Toast.makeText(getActivity(), ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                            startActivity(new Intent(getActivity(),Home.class));
//                            getActivity().finish();
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

    public void Check_Referral_Code(String refferal) {

        showloader("Please Wait..");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.check_referral(refferal);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);

                        Log.i("Resp obj: ", obj + "");


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

                        } else {

                            String message = obj.getString("message");
                            if (message.equals("Referral is available.")) {
                                btn_apply.setVisibility(View.GONE);
                                et_referral_code.setEnabled(false);
                                btn_applied.setVisibility(View.VISIBLE);


                            } else {
                                et_referral_code.setError("Wrong Refferal Code");
                            }
                            hideloader();
                        }

                    } catch (Exception e) {
                        Log.i("Resp Exception: ", e.getMessage() + "");
                        e.printStackTrace();
                        hideloader();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    hideloader();
                    Log.i("Resp Un-Success Code: ", response.code() + "");
                    Toast.makeText(getActivity(), "Error " + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideloader();
                Log.i("Resp onFailure: ", t.getMessage() + "");
                Toast.makeText(getActivity(), "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showloader(String message) {
        dialog_progress = new Dialog(getActivity());
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
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(duration);
    }

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("register")
        @FormUrlEncoded
        Call<ResponseBody> register(@Field("fullname") String fullname,
                                    @Field("mobile") String mobile,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("refferal") String refferal,
                                    @Field("type") String type,
                                    @Field("device_token") String device_token,
                                    @Field("gst_number") String gst_number);
        @Headers("Authkey:APPLEDBDMPL")
        @POST("verifyregister")
        @FormUrlEncoded
        Call<ResponseBody> verifyregister(@Field("mobile") String mobile,
                                    @Field("otp") String otp);

        @Headers("Authkey:APPLEDBDMPL")
        @POST("resendotp")
        @FormUrlEncoded
        Call<ResponseBody> resendotp(@Field("mobile") String mobile);

        @FormUrlEncoded
        @POST("verifyreferral")
        @Headers("Authkey: APPLEDBDMPL")
        Call<ResponseBody> check_referral(@Field("refferal") String refferal);

    }
}