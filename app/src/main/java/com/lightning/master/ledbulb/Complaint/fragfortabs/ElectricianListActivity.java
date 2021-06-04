package com.lightning.master.ledbulb.Complaint.fragfortabs;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lightning.master.ledbulb.Complaint.fragfortabs.Complain_Content.ElectricianAdapter;
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

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

public class ElectricianListActivity extends AppCompatActivity {


    LinearLayout ll_yes_result,ll_Noresult;
    RecyclerView recyclerView_electricianList;
    public static ArrayList<String> idList=new ArrayList<>();
    public static ArrayList<String> NameList=new ArrayList<>();
    public static ArrayList<String> PhoneNumberList=new ArrayList<>();
    public static ArrayList<String> ImageList=new ArrayList<>();
    public static ArrayList<String> isSelected=new ArrayList<>();
    ElectricianAdapter electricianAdapter;
    GridLayoutManager gridLayoutManager;

    int scrolling_page = 0;
    boolean isLastPage = false, isFirst = false;
    boolean isLoading = false;
    SharedPreferences sharedPreferences;
    ImageView iv_stepBack;

    public static String BookingID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrician_list);

        init();

    }

    void init(){

        sharedPreferences       = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ll_yes_result                  = (LinearLayout) findViewById(R.id.ll_yes_result);
        ll_Noresult                    = (LinearLayout) findViewById(R.id.ll_Noresult);
        iv_stepBack                    = (ImageView) findViewById(R.id.iv_stepBack);
        recyclerView_electricianList   = (RecyclerView) findViewById(R.id.recyclerView_electricianList);
        electricianAdapter             = new ElectricianAdapter(this);
        gridLayoutManager              = new GridLayoutManager(this,1);


        idList.clear();
        NameList.clear();
        PhoneNumberList.clear();
        ImageList.clear();
        isSelected.clear();

        scrolling_page += 1;
        isFirst = true;

        iv_stepBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        BookingID=getIntent().getExtras().getString("bookingID");

        getallcomplain(getIntent().getExtras().getString("bookingID"),
                ""+scrolling_page);

    }


    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }

    public void getallcomplain(String user_id, final String page) {
        isLoading = true;
        Log.i("booking_id", "" + user_id);

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        if (page.equals("1")) {

            progressDialog.show();
        } else {
            Snackbar.make(recyclerView_electricianList, "Loading More.. Please Wait", BaseTransientBottomBar.LENGTH_LONG).show();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.mybookings(user_id,page);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    try {
                        if (progressDialog.isShowing()){

                            progressDialog.dismiss();
                        }
                        String result = response.body().string();

                        Log.i("Get All Categories", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");

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

                            if (page.equals(obj.optString("total_pages"))) {
                                isLastPage = true;
                            } else {
                                isLastPage = false;
                            }

                            if (arr_response.length() > 0) {
                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);

                                    if (!jsonObject.isNull("id")){
                                        idList.add(jsonObject.optString("id"));
                                    }else{
                                        idList.add("");
                                    }
                                    if (!jsonObject.isNull("name")){
                                        NameList.add(jsonObject.optString("name"));
                                    }else{
                                        NameList.add("");
                                    }
                                    if (!jsonObject.isNull("phone")){
                                        PhoneNumberList.add(jsonObject.optString("phone"));
                                    }else{
                                        PhoneNumberList.add("");
                                    }
                                    if (!jsonObject.isNull("photo")){
                                        ImageList.add(jsonObject.optString("photo"));
                                    }else{
                                        ImageList.add("");
                                    }
                                    if (!jsonObject.isNull("is_selected")){
                                        isSelected.add(jsonObject.optString("is_selected"));
                                    }else{
                                        isSelected.add("");
                                    }

                                }
                                if (scrolling_page == 1) {
                                    setup_vendors_list();
                                } else {
                                    electricianAdapter.notifyDataSetChanged();
                                }

                            } else {
                                    ll_yes_result.setVisibility(View.GONE);
                                    ll_Noresult.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (progressDialog.isShowing()){

                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    if (progressDialog.isShowing()){

                        progressDialog.dismiss();
                    }
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog.isShowing()){

                    progressDialog.dismiss();
                }
                Log.d("t", "Something went wrong at server!" + t);

                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("nearbyelectricians")
        @FormUrlEncoded
        Call<ResponseBody> mybookings(@Field("booking_id") String user_id,
                                      @Field("page") String page);
    }

    public void setup_vendors_list() {

        ll_yes_result.setVisibility(View.VISIBLE);
        ll_Noresult.setVisibility(View.GONE);
        recyclerView_electricianList.setLayoutManager(gridLayoutManager);
        recyclerView_electricianList.setAdapter(electricianAdapter);

        recyclerView_electricianList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        if (!Utils.isNetworkConnected(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "No Internet, Reconnect and Retry", Toast.LENGTH_SHORT).show();

                        } else {

                            scrolling_page += 1;
                            isFirst = false;

                            getallcomplain(getIntent().getExtras().getString("bookingID"),
                                    ""+scrolling_page);
                        }
                    }
                }
                else
                {

                }
            }
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }
}
