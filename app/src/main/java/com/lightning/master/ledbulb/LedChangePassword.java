package com.lightning.master.ledbulb;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lightning.master.ledbulb.HomeMain.Home_Drawer;

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

public class LedChangePassword extends AppCompatActivity {

    Button button;
    ImageView iv_back;
    ImageView iv_call;
    EditText old_password,new_password,confirm_password;
    SharedPreferences sharedPreferences;
    String userid;

    Dialog dialog_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_change_password);

        init();
    }

    void init(){

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        old_password=(EditText)findViewById(R.id.old_password);
        new_password=(EditText)findViewById(R.id.new_password);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        button=(Button) findViewById(R.id.button);

        onClicks();
    }

    void onClicks(){


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (old_password.getText().toString().equals("")){

                    old_password.setError("Please Enter Old Password!");
                }else if (new_password.getText().toString().equals("")){

                    new_password.setError("Please Enter New Password!");
                }else if (confirm_password.getText().toString().equals("")){

                    confirm_password.setError("Please Confirm New Password!");
                }else if (!new_password.getText().toString().equals(confirm_password.getText().toString())){

                    confirm_password.setError("Password Not Matching With New Password!");
                }else{

                    ChangePassword(sharedPreferences.getString("user_id",""),
                            old_password.getText().toString(),new_password.getText().toString());
//                    Toast.makeText(LedChangePassword.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ChangePassword(String user_id,String old,String new1)
    {
        Log.i("value",user_id+"    "+old+"   "+new1);
        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.changepassword(user_id,old,new1);
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

                        }
                        else
                        {
                            String message = obj.getString("message");

                                Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Home_Drawer.class);
                            startActivity(intent);
//

                        }
                        hideloader();

                    } catch (Exception e) {
                        e.printStackTrace();
                        //       Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    //        hideloader();
                } else {
                    Log.d("Message", "code..."+response.code() + " message..." + response.message()+" body..."+response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later"+" message..." + response.message(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showloader(String message){
        dialog_progress = new Dialog(LedChangePassword.this);
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
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(duration);
    }


    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("changepassword/{user_id}")
        @FormUrlEncoded
        Call<ResponseBody> changepassword(@Path("user_id") String user_id,
                                          @Field("old") String old,
                                          @Field("new") String new1);

    }


}
