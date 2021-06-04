package com.lightning.master.ledbulb.HomeMain.bottom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Contact_us extends AppCompatActivity {

    ImageView iv_back_contact;
    TextView tv_proceed,tv_call;
    TextInputEditText et_name,et_email,et_phone;
    EditText et_message;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);


        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        et_name=(TextInputEditText)findViewById(R.id.et_name);
        et_email=(TextInputEditText)findViewById(R.id.et_email);
        et_phone=(TextInputEditText)findViewById(R.id.et_phone);
        et_message=(EditText)findViewById(R.id.et_message);

//REFERRAL_BONUS

        iv_back_contact=findViewById(R.id.iv_back_contact);
         iv_back_contact.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 startActivity(new Intent(Contact_us.this, Home.class));
                 finish();
             }
         });

        tv_proceed=findViewById(R.id.tv_proceed_contact);
      tv_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (et_name.getText().toString().equals(""))
                {
                    et_name.setError("Enter Your Name");

                }else if(et_email.getText().toString().equals(""))
                {
                    et_email.setError("Enter Your Email");
                }
                else if(!et_email.getText().toString().matches(emailPattern))
                {
                    et_email.setError("Please Enter valid Email Address!");
                }
                else if(et_phone.getText().toString().equals(""))
                {
                    et_phone.setError("Enter Your Phone No.");
                }
                else if (et_phone.getText().toString().length()<9)
                {
                    et_phone.setError("Please Enter Valid Mobile No.!");
                }

                else if(et_message.getText().toString().equals(""))
                {
                    et_message.setError("Enter A Query ");
                }
                else
                {
                    Contact_us_Api(et_name.getText().toString()
                            ,et_email.getText().toString()
                            ,et_phone.getText().toString()
                            ,et_message.getText().toString());
                }



            }
        });
        tv_call=findViewById(R.id.tv_Call);
        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+sharedPreferences.getString("contactus_phone","")));
                startActivity(intent);
            }
        });
    }
    public void Contact_us_Api(String name,String phone,String email,String message)
    {
        final ProgressDialog progressDialog = new ProgressDialog(Contact_us.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Contact_us.Service service = retrofit.create(Contact_us.Service.class);
        Call<ResponseBody> call;
        call = service.contact_us(name,phone,email,message);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String message = obj.optString("message");

                        Toast.makeText(getApplicationContext(), "" +message, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Contact_us.this, Home_Drawer.class));
                        finish();

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
                Toast.makeText(getApplicationContext(), "Something went wrong at server!"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("contactrequest")
        @FormUrlEncoded
        Call<ResponseBody> contact_us(@Field("name") String name,
                                 @Field("phone") String phone,
                                 @Field("email") String email,
                                 @Field("message") String message);


    }
}
