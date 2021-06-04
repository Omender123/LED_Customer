package com.lightning.master.ledbulb.HomeMain.bottom;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import retrofit2.http.Path;

public class Earned_Points extends AppCompatActivity {
    ImageView iv_back,iv_copy,share_app,share_whtsapp,sms_share;
    TextView referral_code,point_amount,refer_text,terms_condition;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earned__points);

        iv_back=findViewById(R.id.iv_back_points);
        referral_code=(TextView)findViewById(R.id.referral_code);
        refer_text=(TextView)findViewById(R.id.refer_text);
        terms_condition=(TextView)findViewById(R.id.terms_condition);
        point_amount=(TextView)findViewById(R.id.point_amount);
        iv_copy=(ImageView) findViewById(R.id.iv_copy);
        share_app=(ImageView) findViewById(R.id.share_app);
        share_whtsapp=(ImageView) findViewById(R.id.share_whtsapp);
        sms_share=(ImageView) findViewById(R.id.sms_share);


        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Earned_Points.this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        My_Wallet_API(sharedPreferences.getString("user_id",""));
        referral_code.setText(sharedPreferences.getString("user_code",""));
        point_amount.setText(sharedPreferences.getString("amount",""));

        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("",""+sharedPreferences.getString("user_code",""));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Earned_Points.this, "Refferal Code Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });
        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent share = new Intent(Intent.ACTION_SEND);

                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, "Download The Lighting Masters App !! - " +
                            ":\n https://play.google.com/store/apps/details?id="+getPackageName()+" \n invite code : "+" "+sharedPreferences.getString("user_code",""));
                    startActivity(Intent.createChooser(share, "Share using"));
                } catch(Exception e) {

                }
            }
        });
        share_whtsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();

            }
        });

        sms_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { " " });
                intent.putExtra(Intent.EXTRA_TEXT, "Download The Lighting Masters App !! - " +
                        ":\n https://play.google.com/store/apps/details?id="+getPackageName()+" \n invite code : "+" "+sharedPreferences.getString("user_code",""));
                startActivity(Intent.createChooser(intent, "Email via..."));

            }
        });

        terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String term_conditions=sharedPreferences.getString("term_conditions","");
                Uri uri = Uri.parse(term_conditions);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void My_Wallet_API(String user_id)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Earned_Points.Service service = retrofit.create(Earned_Points.Service.class);
        Call<ResponseBody> call;
        call = service.wallet(user_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);

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
                                if (!dataObject.isNull("user_code"))
                                {
                                    editor.putString("user_code",dataObject.getString("user_code"));
                                    referral_code.setText(dataObject.getString("user_code"));

                                }else{
                                    editor.putString("user_code","");
                                    referral_code.setText("");
                                }
                                if (!dataObject.isNull("amount"))
                                {
                                    editor.putString("amount",dataObject.getString("amount"));
                                    point_amount.setText(dataObject.getString("amount"));

                                }else{
                                    editor.putString("amount","");
                                    point_amount.setText("");

                                }
                                if (!dataObject.isNull("REFERRAL_BONUS"))
                                {
                                    editor.putString("REFERRAL_BONUS",dataObject.getString("REFERRAL_BONUS"));
                                    refer_text.setText("Refer to your Friends and Family and Get "+dataObject.getString("REFERRAL_BONUS")+" Points ");

                                }else{
                                    editor.putString("REFERRAL_BONUS","");
                                    refer_text.setText("Refer to your Friends and Family and get ** Points ");

                                }
                                editor.commit();


                            }else{
                                Toast.makeText(getApplicationContext(), "No Data Available!!", Toast.LENGTH_SHORT).show();

                            }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), " " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong at server!"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @GET("mywallet/{user_id}")
        Call<ResponseBody> wallet(@Path("user_id") String user_id);


    }
    private void openWhatsApp() {
        String smsNumber = "7****"; //without '+'
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
//sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Download LED App !! - " +
                    ":\n https://play.google.com/store/apps/details?id="+getPackageName()+" \n invite code : "+" "+sharedPreferences.getString("user_code",""));
            sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch(Exception e) {
            Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
