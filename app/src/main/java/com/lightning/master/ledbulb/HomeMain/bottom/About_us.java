package com.lightning.master.ledbulb.HomeMain.bottom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import retrofit2.http.GET;
import retrofit2.http.Headers;


public class About_us extends AppCompatActivity {

    ImageView iv_back_contact;
    TextView tv_contact,tv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        init();
    }

    void init()
    {

        iv_back_contact=(ImageView) findViewById(R.id.iv_back_about);

        tv_contact=(TextView) findViewById(R.id.tv_contact_about);
        tv_about=(TextView) findViewById(R.id.tv_about);

        About();
        onClicks();
    }

    void onClicks()
    {
        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(About_us.this,Contact_us.class));
                finish();
            }
        });
        iv_back_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(About_us.this, Home.class));
                finish();
            }
        });
    }

    public void About() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.about();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);

                        if (obj.has("error")) {
                            String message = obj.getString("error_message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else {
                           if (obj.has("data")){
                               if (obj.isNull("data")){
                                   tv_about.setText("");
                               }else{

                                   String s= obj.getString("data").replaceAll("(\r\n)", "\n");
                                   tv_about.setText(s);
                               }
                           }
                        }
//                        Toast.makeText(getApplicationContext(), " Successfully Add" , Toast.LENGTH_LONG).show();

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
        @GET("getpage/about")
//        @FormUrlEncoded
        Call<ResponseBody> about();

    }

}
