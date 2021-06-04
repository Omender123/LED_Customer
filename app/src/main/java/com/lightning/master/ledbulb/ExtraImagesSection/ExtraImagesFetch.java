package com.lightning.master.ledbulb.ExtraImagesSection;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

public class ExtraImagesFetch extends AppCompatActivity {


    RecyclerView recycler_extraImages;
    public static ArrayList<String> imagesArray=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_images_fetch);

        recycler_extraImages=(RecyclerView)findViewById(R.id.recycler_extraImages);
        getallcomplain(getIntent().getExtras().getString("bookingID"));
    }


    public void getallcomplain(String user_id) {

        Log.i("user_id",user_id);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.mybookings(user_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();

                        Log.i("Get All Categories", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");

                        imagesArray.clear();
                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();

                        } else {

                            JSONArray arr_response = obj.optJSONArray("data");

                            if (arr_response.length()>0){
                                for(int i=0;i<arr_response.length();i++){

                                    JSONObject jsonObject=arr_response.getJSONObject(i);

                                    if (!jsonObject.isNull("booking_img_path")){
                                        imagesArray.add(jsonObject.getString("booking_img_path"));
                                    }else{
                                        imagesArray.add("");
                                    }
                                }
                                setData();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                   /* DialogueCustom.dialogue_custom(SplashActivity.this,"Something went Wrong!",
                            "Aw Snap! Error code:"+response.code(),
                            "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                            "GOT IT","",false, R.drawable.error,
                            "exit","", Color.parseColor("#1EBEA5"),Color.parseColor("#FFA1A1A1"));*/
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progressDialog.dismiss();
                Log.d("t", "Something went wrong at server!" + t);

                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("getbookingimages")
        @FormUrlEncoded
        Call<ResponseBody> mybookings(@Field("booking_id") String user_id);
    }

    void setData(){
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        recycler_extraImages.setLayoutManager(gridLayoutManager);
        recycler_extraImages.setAdapter(new ExtraImagesAdapter(this));
    }
}
