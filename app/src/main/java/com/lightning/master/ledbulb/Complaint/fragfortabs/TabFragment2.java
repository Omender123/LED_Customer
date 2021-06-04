package com.lightning.master.ledbulb.Complaint.fragfortabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lightning.master.ledbulb.Complaint.fragfortabs.Complain_Content.ComplainAdapter;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complain_Content.ComplainModel;
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
import retrofit2.http.Path;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;


public class TabFragment2 extends Fragment {

    LinearLayout ll_Noresult,ll_progress,ll_yes_result;
    RecyclerView recyclerView_complainLIst;

    int scrolling_page = 0;
    boolean isLastPage = false, isFirst = false;
    boolean isLoading = false;

    ComplainAdapter complainAdapter;
    GridLayoutManager gridLayoutManager;
   public static ArrayList<ComplainModel> complainModelArrayList=new ArrayList<>();
   SharedPreferences sharedPreferences;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootview =  inflater.inflate(R.layout.fragment_tab_fragment2, container, false);

         init();
        return rootview;
    }

    void init()
    {
        sharedPreferences       = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ll_progress             =(LinearLayout) rootview.findViewById(R.id.ll_progress);
        ll_yes_result           =(LinearLayout) rootview.findViewById(R.id.ll_yes_result);
        ll_Noresult             =(LinearLayout) rootview.findViewById(R.id.ll_Noresult);
        recyclerView_complainLIst=(RecyclerView) rootview.findViewById(R.id.recyclerView_complainLIst);
        gridLayoutManager       =new GridLayoutManager(getActivity(),1);
        complainAdapter         =new ComplainAdapter(getActivity());

        complainModelArrayList.clear();
        scrolling_page += 1;
        isFirst = true;

        getallcomplain(sharedPreferences.getString("user_id",""),scrolling_page+"");

    }


    public void getallcomplain(String user_id, final String page) {
        isLoading = true;

        if (page.equals("1")) {
            ll_progress.setVisibility(View.VISIBLE);

        } else {
            Snackbar.make(recyclerView_complainLIst, "Loading More.. Please Wait", BaseTransientBottomBar.LENGTH_LONG).show();
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
                        String result = response.body().string();

                        Log.i("resp_mybookings", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");

//                        complainModelArrayList.clear();

                        ll_progress.setVisibility(View.GONE);

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();

                        } else {

                                JSONArray arr_response = obj.optJSONArray("data");

                                if (page.equals(obj.optString("total_pages"))) {
                                    isLastPage = true;
                                } else {
                                    isLastPage = false;
                                }

                                if (arr_response.length() > 0) {
                                    //   nearbyPropertiesModelArrayList.clear();
                                    for (int i = 0; i < arr_response.length(); i++) {
                                        JSONObject jsonObject = arr_response.optJSONObject(i);
                                        ComplainModel allProductsModel = new ComplainModel();
                                        //////////////    Adding to Datamodel   ////////////////
                                        allProductsModel.setId(jsonObject.optString("id"));
                                        allProductsModel.setBooking_code(jsonObject.optString("booking_code"));
                                        allProductsModel.setUser_id(jsonObject.optString("user_id"));
                                        allProductsModel.setBooking_lat(jsonObject.optString("booking_lat"));
                                        allProductsModel.setBooking_long(jsonObject.optString("booking_long"));
                                        allProductsModel.setBooking_location(jsonObject.optString("booking_location"));
                                        allProductsModel.setBooking_quantity(jsonObject.optString("booking_quantity"));
                                        allProductsModel.setBooking_description(jsonObject.optString("booking_description"));
                                        allProductsModel.setBooking_img_path(jsonObject.optString("booking_img_path"));
                                        allProductsModel.setBooking_type(jsonObject.optString("booking_type"));
                                        allProductsModel.setDroppoint_id(jsonObject.optString("droppoint_id"));
                                        allProductsModel.setBooking_date(jsonObject.optString("booking_date"));
                                        allProductsModel.setBooking_status(jsonObject.optString("booking_status"));
                                        allProductsModel.setDeleted(jsonObject.optString("deleted"));

                                        if (!jsonObject.isNull("pickup_otp")) {

                                            allProductsModel.setPickup_otp(jsonObject.optString("pickup_otp"));

                                        } else {

                                            allProductsModel.setPickup_otp("");
                                        }
//                                        allProductsModel.setDeleted(jsonObject.optString("pickup_otp"));
                                        complainModelArrayList.add(allProductsModel);
                                    }
                                    if (scrolling_page == 1) {
//                                        ll_yes_result.setVisibility(View.VISIBLE);

                                        setup_vendors_list();
                                    } else {
                                        complainAdapter.notifyDataSetChanged();
//                                        ll_progress.setVisibility(View.GONE);
//                                        ll_yes_result.setVisibility(View.VISIBLE);

                                    }

                                } else {
//                                    Toast.makeText(getActivity(), "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();

                                    if (scrolling_page>1)
                                    {

                                            ll_yes_result.setVisibility(View.VISIBLE);
                                            ll_progress.setVisibility(View.GONE);
                                            ll_Noresult.setVisibility(View.GONE);

//                                            Snackbar.make(recyclerView_complainLIst, "No More Data !!", BaseTransientBottomBar.LENGTH_LONG).show();

                                    }else{

                                        ll_yes_result.setVisibility(View.GONE);
                                        ll_progress.setVisibility(View.GONE);
                                        ll_Noresult.setVisibility(View.VISIBLE);
                                    }
                                }
                            ll_progress.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getActivity(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getActivity(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

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

                Toast.makeText(getActivity(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("mybookings/{user_id}")
        @FormUrlEncoded
        Call<ResponseBody> mybookings(@Path("user_id") String user_id,
                                      @Field("page") String page);
    }

    public void setup_vendors_list() {

//        int numberOfColumns = 1;
//        ll_yes_result.setVisibility(View.VISIBLE);
//        ll_progress.setVisibility(View.GONE);
//        gridLayoutManager = new GridLayoutManager(getActivity());
        recyclerView_complainLIst.setLayoutManager(gridLayoutManager);
        recyclerView_complainLIst.setAdapter(complainAdapter);

        recyclerView_complainLIst.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        if (!Utils.isNetworkConnected(getActivity())) {
                            Toast.makeText(getActivity(), "No Internet, Reconnect and Retry", Toast.LENGTH_SHORT).show();

                        } else {

                            scrolling_page += 1;
                            isFirst = false;

                            getallcomplain(sharedPreferences.getString("user_id",""),
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
