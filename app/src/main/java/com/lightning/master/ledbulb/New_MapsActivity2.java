package com.lightning.master.ledbulb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint;
import com.lightning.master.ledbulb.Complaint.fragfortabs.TabFragment1;
import com.lightning.master.ledbulb.HomeMain.Home_Drawer;

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


public class New_MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Dialog dialog_progress;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    TextView text_drop,tv_ElectricianName,tv_PhoneNumber,tv_texttitle;
    LinearLayout ll_setLayout,ll_cardData;
    ImageView iv_imageElectri;
    Button set_drop_point,btn_assign;
    ProgressBar progressBar;

    public static String drop_title="", drop_rc_ID="";
    public static double drop_latitude =0 ,drop_longitude=0;
    
    String electricianID="",bookingID="";

    public static ArrayList<String> rc_ID = new ArrayList<>();
    public static ArrayList<String> name = new ArrayList<>();
    public static ArrayList<String> email = new ArrayList<>();
    public static ArrayList<String> phone = new ArrayList<>();
    public static ArrayList<String> city_id = new ArrayList<>();
    public static ArrayList<String> state_id = new ArrayList<>();
    public static ArrayList<Double> location_lat = new ArrayList<>();
    public static ArrayList<Double> location_long = new ArrayList<>();

    public static ArrayList<String> idList=new ArrayList<>();
    public static ArrayList<String> NameList=new ArrayList<>();
    public static ArrayList<String> PhoneNumberList=new ArrayList<>();
    public static ArrayList<String> ImageList=new ArrayList<>();
    public static ArrayList<String> is_selected=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__maps2);

        init();
    }


    void init(){

        set_drop_point      =(Button)findViewById(R.id.set_drop_point);
        btn_assign          =(Button)findViewById(R.id.btn_assign);

        text_drop           =(TextView) findViewById(R.id.text_drop);
        tv_ElectricianName  =(TextView) findViewById(R.id.tv_ElectricianName);
        tv_PhoneNumber      =(TextView) findViewById(R.id.tv_PhoneNumber);
        tv_texttitle        =(TextView) findViewById(R.id.tv_texttitle);

        ll_setLayout        =(LinearLayout) findViewById(R.id.ll_setLayout);
        ll_cardData         =(LinearLayout) findViewById(R.id.ll_cardData);
        iv_imageElectri     =(ImageView) findViewById(R.id.iv_imageElectri);
        progressBar         =(ProgressBar) findViewById(R.id.progressBar);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().getExtras().getString("startFrom").equals("NewComplain")){

            ll_setLayout.setVisibility(View.VISIBLE);
            ll_cardData.setVisibility(View.GONE);
            tv_texttitle.setText("Select Your Drop Point");
            Set_Drop_Point_API(TabFragment1.latitude + "", TabFragment1.longatitude + "");
        }else{
            tv_texttitle.setText("Select Electrician");
            ll_setLayout.setVisibility(View.GONE);
            bookingID=getIntent().getExtras().getString("bookingID");
            nearbyelectricians(bookingID);
        }
        onClicks();
    }

    void onClicks(){
        btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (electricianID.equals("")){
                    Toast.makeText(New_MapsActivity2.this, "Please Select One Electrician !", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(New_MapsActivity2.this);
                    alert.setTitle("Confirmation");
                    alert.setIcon(R.mipmap.ic_launcher);
                    alert.setMessage("We will inform electrician for this booking so electrician can contact you. After selecting the electrician, it can't be undone.");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            selectelectrician(bookingID,electricianID);

                        }
                    })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
        set_drop_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void nearbyelectricians(String booking_id) {


        Log.i("booking_id",booking_id);

        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(Utils.base_url)
                .build();
        New_MapsActivity2.Service service = retrofit.create(New_MapsActivity2.Service.class);
        Call<ResponseBody> call;
        call = service.nearbyelectricians(booking_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        Log.i("Resp success: ", obj + "");

                        if (obj.has("error")) {
                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            hideloader();
                            Toast.makeText(New_MapsActivity2.this, " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            hideloader();
                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(New_MapsActivity2.this, " " + message, Toast.LENGTH_LONG).show();

                        } else {

                            idList.clear();
                            NameList.clear();
                            PhoneNumberList.clear();
                            ImageList.clear();
                            is_selected.clear();
                            location_lat.clear();
                            location_long.clear();

                            JSONArray data = obj.getJSONArray("data");

                            if (data.length() >= 0) {


                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = data.getJSONObject(i);

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
                                        is_selected.add(jsonObject.optString("is_selected"));
                                    }else{
                                        is_selected.add("");
                                    }
                                    if (!jsonObject.isNull("location_lat")) {
                                        location_lat.add(Double.parseDouble(jsonObject.optString("location_lat")));
                                    } else {
                                        location_lat.add(Double.parseDouble(""));
                                    }
                                    if (!jsonObject.isNull("location_lng")) {
                                        location_long.add(Double.parseDouble(jsonObject.optString("location_lng")));
                                    } else {
                                        location_long.add(Double.parseDouble(""));
                                    }

                                }
// onMapReady(mMap);

                                Marker[] allMarkers = new Marker[idList.size()];

                                for (int i = 0; i < idList.size(); i++) {

                                    if (mMap != null) {

                                        LatLng latLng = new LatLng(location_lat.get(i), location_long.get(i));

                                        allMarkers[i] = mMap.addMarker(new MarkerOptions().position(latLng)
                                                .title(NameList.get(i)).snippet(idList.get(i))
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.electrician_icon)));
                                        allMarkers[i].setTag(i+"");

                                    }
                                }
                                hideloader();
                            } else {
                                hideloader();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        hideloader();
                        Toast.makeText(New_MapsActivity2.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(New_MapsActivity2.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    hideloader();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(New_MapsActivity2.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                    hideloader();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideloader();
                Toast.makeText(New_MapsActivity2.this, "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Set_Drop_Point_API(String current_lat, String current_long) {

        showloader("Please Wait..");
        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(Utils.base_url)
                .build();
        New_MapsActivity2.Service service = retrofit.create(New_MapsActivity2.Service.class);
        Call<ResponseBody> call;
        call = service.drop_point(current_lat,current_long);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        Log.i("Resp success: ", obj + "");

                        if (obj.has("error")) {
                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            hideloader();
                            Toast.makeText(New_MapsActivity2.this, " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            hideloader();
                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(New_MapsActivity2.this, " " + message, Toast.LENGTH_LONG).show();

                        } else {
                            rc_ID.clear();
                            name.clear();
                            email.clear();
                            phone.clear();
                            city_id.clear();
                            state_id.clear();
                            location_lat.clear();
                            location_long.clear();

                            JSONArray data = obj.getJSONArray("data");

                            if (data.length() >= 0) {


                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = data.getJSONObject(i);

                                    rc_ID.add(jsonObject.optString("id"));
                                    name.add(jsonObject.optString("name"));
                                    email.add(jsonObject.optString("email"));
                                    phone.add(jsonObject.optString("phone"));
                                    city_id.add(jsonObject.optString("city_id"));
                                    state_id.add(jsonObject.optString("state_id"));
                                    if (!jsonObject.isNull("location_lat")) {
                                        location_lat.add(Double.parseDouble(jsonObject.optString("location_lat")));
                                    } else {
                                        location_lat.add(Double.parseDouble(""));
                                    }
                                    if (!jsonObject.isNull("location_long")) {
                                        location_long.add(Double.parseDouble(jsonObject.optString("location_long")));
                                    } else {
                                        location_long.add(Double.parseDouble(""));
                                    }

                                }
// onMapReady(mMap);

                                Marker[] allMarkers = new Marker[rc_ID.size()];

                                for (int i = 0; i < rc_ID.size(); i++) {

                                    if (mMap != null) {
                                        LatLng latLng = new LatLng(location_lat.get(i), location_long.get(i));
                                        allMarkers[i] = mMap.addMarker(new MarkerOptions().position(latLng)
                                                .title(name.get(i)).snippet(rc_ID.get(i))
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.shop)));

                                    }
                                }
                                hideloader();
                            } else {
                                hideloader();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        hideloader();
                        Toast.makeText(New_MapsActivity2.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(New_MapsActivity2.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    hideloader();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(New_MapsActivity2.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                    hideloader();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideloader();
                Toast.makeText(New_MapsActivity2.this, "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showloader(String message) {
        dialog_progress = new Dialog(New_MapsActivity2.this);
        dialog_progress.setContentView(R.layout.dialog_progress);
        dialog_progress.getWindow().setBackgroundDrawableResource(R.drawable.backkkk);
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
                        dialog_progress.dismiss();
                    }
                }, 1200);
    }

    public void vibrate_alert(int duration) {
        Vibrator v = (Vibrator) New_MapsActivity2.this.getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        v.vibrate(duration);
    }

    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("nearbydroppoints")
        @FormUrlEncoded
        Call<ResponseBody> drop_point(@Field("current_lat") String current_lat,
                                      @Field("current_long") String current_long);

        @Headers("Authkey:APPLEDBDMPL")
        @POST("nearbyelectricians")
        @FormUrlEncoded
        Call<ResponseBody> nearbyelectricians(@Field("booking_id") String booking_id);

        @Headers("Authkey:APPLEDBDMPL")
        @POST("selectelectrician")
        @FormUrlEncoded
        Call<ResponseBody> selectelectrician(@Field("booking_id") String booking_id,
                                             @Field("electrician_id") String electrician_id);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.clear();

        if (getIntent().getExtras().getString("startFrom").equals("NewComplain")){

            LatLng sydney = new LatLng(Double.parseDouble( TabFragment1.latitude), Double.parseDouble(TabFragment1.longatitude));
// builder = new LatLngBounds.Builder();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(sydney) // Sets the center of the map to
                    .zoom(13)// Sets the zoom// Sets the orientation of the camera to east
                    .tilt(30) // Sets the tilt of the camera to 30 degrees
                    .build(); // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }else{

            LatLng sydney = new LatLng(Double.parseDouble(Home_Drawer.latitude), Double.parseDouble(Home_Drawer.longatitude));
// builder = new LatLngBounds.Builder();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(sydney) // Sets the center of the map to
                    .zoom(13)// Sets the zoom// Sets the orientation of the camera to east
                    .tilt(30) // Sets the tilt of the camera to 30 degrees
                    .build(); // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }


/* bounds = builder.build();
CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
mMap.animateCamera(cu);*/


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

//                Toast.makeText(New_MapsActivity2.this, "Phone: "+)), Toast.LENGTH_SHORT).show();

                if (getIntent().getExtras().getString("startFrom").equals("NewComplain")){

                    drop_title = marker.getTitle();
                    drop_rc_ID=marker.getSnippet();

                    marker.getId();

                    drop_latitude = marker.getPosition().latitude;
                    drop_longitude = marker.getPosition().longitude;

                    TabFragment1.droppoint_id = drop_rc_ID;

                    text_drop.setText(drop_title);
                    TabFragment1.text_layout.setVisibility(View.VISIBLE);
                    TabFragment1.drop_name.setText(drop_title);
                }else{

                    drop_title = marker.getTitle();
                    electricianID=marker.getSnippet();

                    tv_ElectricianName.setText(drop_title);
                    tv_PhoneNumber.setText(PhoneNumberList.get(Integer.parseInt(marker.getTag()+"")));

                    ll_cardData.setVisibility(View.VISIBLE);

                    if (is_selected.get(Integer.parseInt(marker.getTag()+"")).equals("0")){
                        btn_assign.setVisibility(View.VISIBLE);
                    }else{
                        btn_assign.setVisibility(View.GONE);
                    }

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.imageloading);
                    requestOptions.error(R.drawable.noimage);

                    Glide.with(New_MapsActivity2.this)
                            .setDefaultRequestOptions(requestOptions)
                            .load(ImageList.get(Integer.parseInt(marker.getTag()+"")))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(iv_imageElectri);

                }

                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(getApplicationContext(), Complaint.class);

        startActivity(intent);*/
        finish();
    }

    public void selectelectrician(String booking_id, String electrician_id) {


        Log.i("booking_id : ", "" + booking_id);
        Log.i("electrician_id : ", "" + electrician_id);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.selectelectrician(booking_id,electrician_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        Log.i("result : ", "" + result);
                        if (obj.has("error")) {
                            String message = obj.getString("error_message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else if (obj.has("warning")) {
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

                        } else {
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), " " + message, Toast.LENGTH_LONG).show();

//                            myViewHolder.btn_assign.setVisibility(View.GONE);
                            Complaint.i=1;
                            Intent intent = new Intent(getApplicationContext(),Complaint.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getApplicationContext().startActivity(intent);
                        }

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


}