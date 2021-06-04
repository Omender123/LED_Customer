package com.lightning.master.ledbulb.Splash_log_reg;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class WelcomeScreen extends Fragment {

    TextView tv_login,tv_register;

    LinearLayout ll_title1,ll_title2;
    EditText et_mobile;
    View rootView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        init();



        return rootView;
    }

    void init()
    {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        tv_login = (TextView) rootView.findViewById(R.id.tv_login);
        tv_register = (TextView) rootView.findViewById(R.id.tv_register);
        ll_title1 = (LinearLayout) rootView.findViewById(R.id.ll_title);
        ll_title2 = (LinearLayout) rootView.findViewById(R.id.ll_title2);
        et_mobile = (EditText) rootView.findViewById(R.id.et_mobile);
      //  et_mobile.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        //et_mobile.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        //et_mobile.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        onClicks();
    }

    void onClicks()
    {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                otpVerificationDialog();
                if (et_mobile.getText().toString().equals(""))
                {
                    et_mobile.setError("Please Enter Your Mobile Number");
                }else if (et_mobile.getText().toString().length()<9){

                    et_mobile.setError("Please Enter Valid Mobile Number");
                }
                else{
                    checkmobile(et_mobile.getText().toString());
                }
            }


        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    void otpVerificationDialog()
    {
        final Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.otp_verification_layout);
        dialog.setCanceledOnTouchOutside(false);

        Pinview pinview=(Pinview)dialog.findViewById(R.id.pinview);
        ImageView img_cancel=(ImageView)dialog.findViewById(R.id.img_cancel);
        TextView tv_resend=(TextView)dialog.findViewById(R.id.tv_resend);

        dialog.show();
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendotp(et_mobile.getText().toString());
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
//                Toast.makeText(getContext(), pinview.getValue(), Toast.LENGTH_SHORT).show();
                verifyregister(et_mobile.getText().toString(),pinview.getValue());
            }
        });
    }


    public void checkmobile(String mobile) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Checking User...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.checkmobile(mobile);

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
                            if(message.equals("verifyuser")){
                                otpVerificationDialog();

                            }else if(message.equals("existsuser")){

                                ll_title1.setVisibility(View.GONE);
                                ll_title2.setVisibility(View.VISIBLE);

                                Login login=new Login();
                                Bundle b=new Bundle();
                                b.putString("mobile",et_mobile.getText().toString());
                                login.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                                transaction.addToBackStack(null);
                                transaction.add(R.id.container_splash,login);
                                // fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                                transaction.commit();


                            }else {
                                ll_title1.setVisibility(View.GONE);
                                ll_title2.setVisibility(View.VISIBLE);

                                Register register=new Register();
                                Bundle b=new Bundle();
                                b.putString("mobile",et_mobile.getText().toString());
                                register.setArguments(b);

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                                transaction.addToBackStack(null);
                                transaction.add(R.id.container_splash,register);

                                // fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                                transaction.commit();
//                                getActivity().finish();
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


    public void verifyregister(String mobile ,String email ) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Verifing User...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Register.Service service = retrofit.create(Register.Service.class);
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
                                editor.putBoolean("loggedIn", true);

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
        Register.Service service = retrofit.create(Register.Service.class);
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

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("checkmobile")
        @FormUrlEncoded
        Call<ResponseBody> checkmobile(@Field("mobile") String mobile
        );

    }
}
