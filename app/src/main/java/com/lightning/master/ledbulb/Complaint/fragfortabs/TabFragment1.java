package com.lightning.master.ledbulb.Complaint.fragfortabs;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.lightning.master.ledbulb.Adapter.Adapter_Complaint_items;
import com.lightning.master.ledbulb.Complaint.fragfortabs.Complain_Content.AddImagesAdapter;
import com.lightning.master.ledbulb.Interface.ItemClickListner;
import com.lightning.master.ledbulb.MapsActivity;
import com.lightning.master.ledbulb.Model.ModelPriceList;
import com.lightning.master.ledbulb.New_MapsActivity2;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Utils;
import com.lightning.master.ledbulb.databinding.FragmentTabFragment1Binding;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment implements OnMapReadyCallback, AddImagesAdapter.ImagePicker {

    RecyclerView recycler_addphotos;
    AddImagesAdapter addImagesAdapter;

    public static ArrayList<ModelPriceList> itemDataList = new ArrayList<>();
    public static ArrayList<String> item_id = new ArrayList<>();
    public static ArrayList<String> item_title = new ArrayList<>();
    public static ArrayList<String> item_max = new ArrayList<>();

    public static ArrayList<String> Id = new ArrayList<>();
    public static ArrayList<String> Watt = new ArrayList<>();
    public static ArrayList<String> Qty = new ArrayList<>();



    public static ArrayList<String> selected_images = new ArrayList<>();
    public static ArrayList<String> final_images = new ArrayList<>();
    public static int current_add_limit;
    public static int max_add_images;
    int numberOfGridColumns = 3;
    Animation shake;
    int currenttapped = 1;
    LinearLayout ll_addimg;
    Service uploadService;
    Call<ResponseBody> call;
    public static ArrayList<String> reverted_img_names = new ArrayList<>();
    String reverted_imgnames = "";
    Dialog dialog_progress;
    int current_img_upload_index = 0;

    private GoogleMap mMap;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Marker marker;

    Dialog dialog_progress1;

    int scrolling_page = 0;
    boolean isLastPage = false, isFirst = false;
    boolean isLoading = false;

    MultipartBody requestBody;
    public static TextView et_setlocation;
    Button btn_submit_complaint, btn_Next, btn_Previous;
    RadioGroup radioGroup;
    RadioButton rbtn_service;
    ImageView iv_add_image;
    TextView tv_add_image, tv_add_desc;
    EditText et_description, et_ledCount;
    View rootview;

    public static LinearLayout text_layout;
    public static TextView drop_name;
    public static String latitude = "", longatitude = "", radioValue = "", droppoint_id = "", address = "";

    LinearLayout ll_firstStep, ll_secondStep, ll_couplesBtns;
    SharedPreferences sharedPreferences;

    String converted_path_featured = "", converted_path = "", mCurrentPhotoPath = "",
            photo_name = "", img_path = "", bookingSuccess_message = "";
    Uri picUri;
    Bitmap bitmap;
    LatLng myCoordinates;
    FragmentTabFragment1Binding binding;
    Context context;

    String selectedType = "", selectedWatt = "", selectedId = "";
    int itemMax = 0;
    JSONArray jsonArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTabFragment1Binding.inflate(inflater, container, false);
        rootview = binding.getRoot();
        context = getActivity();

        init();
        lighttypes();
        item_id.clear();
        item_max.clear();
        item_title.clear();
        Id.clear();
        Watt.clear();
        Qty.clear();
        return rootview;
    }

    private void spinner() {

        ArrayAdapter<String> dataAdapterpayment = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, item_title);
        dataAdapterpayment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerType.setAdapter(dataAdapterpayment);
        binding.spinnerType.setSelection(0);
        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {

                if (!binding.spinnerType.getSelectedItem().equals("Select type")) {
                    itemMax = Integer.parseInt(item_max.get(position));
                    selectedType = String.valueOf(binding.spinnerType.getSelectedItem());
                    selectedId = item_id.get(position);
//                    binding.etWatt.setHint("Maximum " + itemMax + " Watt");
                    binding.llwatt.setVisibility(View.VISIBLE);
                    binding.llPiece.setVisibility(View.VISIBLE);

                } else {
                    selectedType = "";
                    binding.llwatt.setVisibility(View.GONE);
                    binding.llPiece.setVisibility(View.GONE);

                }
                Log.v("resp Item", "" + binding.spinnerType.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Log.v("NothingSelected Item", "" + binding.spinnerType.getSelectedItem());
            }
        });
    }


    void init() {


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        et_setlocation = rootview.findViewById(R.id.et_setlocation);

        btn_submit_complaint = rootview.findViewById(R.id.btn_submit_complaint);
        btn_Next = rootview.findViewById(R.id.btn_Next);
        btn_Previous = rootview.findViewById(R.id.btn_Previous);
        tv_add_desc = rootview.findViewById(R.id.tv_add_description);
        tv_add_image = rootview.findViewById(R.id.tv_add_Image);
        et_description = rootview.findViewById(R.id.et_description);
        et_ledCount = rootview.findViewById(R.id.et_ledCount);
        iv_add_image = rootview.findViewById(R.id.iv_add_image);
        drop_name = (TextView) rootview.findViewById(R.id.drop_name);
        text_layout = (LinearLayout) rootview.findViewById(R.id.text_layout);
        ll_firstStep = (LinearLayout) rootview.findViewById(R.id.ll_firstStep);
        ll_secondStep = (LinearLayout) rootview.findViewById(R.id.ll_secondStep);
        ll_couplesBtns = (LinearLayout) rootview.findViewById(R.id.ll_couplesBtns);

        radioGroup = (RadioGroup) rootview.findViewById(R.id.radioGroup1);
        rbtn_service = (RadioButton) rootview.findViewById(R.id.rbtn_service);
        scrolling_page += 1;
        isFirst = true;

        current_add_limit = 1;
        max_add_images = 20;
        selected_images.clear();

        shake = AnimationUtils.loadAnimation(getContext(), R.anim.error_shake);
        recycler_addphotos = (RecyclerView) rootview.findViewById(R.id.recycler_addphotos);
        ll_addimg = (LinearLayout) rootview.findViewById(R.id.ll_addimg);
//        btn_upload_everything=(Button)rootview.findViewById(R.id.btn_upload_everything);
        addImagesAdapter = new AddImagesAdapter(getContext(), this);
        setup_addimage();

        if (current_add_limit > 0) {
            for (int i = 0; i < current_add_limit; i++) {
                selected_images.add("");
            }
        }


        onClicks();
    }

    private void setup_addimage() {
        recycler_addphotos.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_addphotos.setVisibility(View.VISIBLE);
        recycler_addphotos.setLayoutManager(new GridLayoutManager(getContext(), numberOfGridColumns));
        recycler_addphotos.setAdapter(addImagesAdapter);
        addImagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void image_Picker(int current_tapped) {
        currenttapped = current_tapped;
        if (Build.VERSION.SDK_INT >= 23) {
            AllowPermissions();
        } else {
            imagepickclick();
        }
    }

    public void addimage() {
        if (current_add_limit == max_add_images) {
            vibrate_alert(500);
            Toast.makeText(getContext(), "You Can't Select more than " + max_add_images + " Images", Toast.LENGTH_SHORT).show();
            recycler_addphotos.startAnimation(shake);
        } else {
            current_add_limit = current_add_limit + 1;
            selected_images.add("");
            addImagesAdapter.notifyDataSetChanged();
        }
    }

    public void vibrate_alert(int duration) {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(duration);
    }

    void onClicks() {


        binding.btnAdd.setOnClickListener(v -> {
            String watt = binding.etWatt.getText().toString();
            if (selectedId.equals("")) {
                Utils.snack(binding.container, "Please choose light type");

            } else if (watt.equals("")) {
                Utils.snack(binding.container, "Please enter valid light watt");

            } else if (Integer.parseInt(watt) > itemMax) {
                Utils.snack(binding.container, "Maximum watt is " + itemMax);
            } else if (binding.etPieces.getText().toString().equals("")) {
                Utils.snack(binding.container, "Please add quantity");
            } else {

                Id.add("" + selectedId);
                Qty.add("" + binding.etPieces.getText().toString());
                Watt.add("" + binding.etWatt.getText().toString());

                try {
                    putDataIn_JSON();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                estimatecharges("" + jsonArray);
                selectedId = "";
                binding.etPieces.setText("");
                binding.etWatt.setText("");
                binding.spinnerType.setSelection(0);
            }


        });

        ll_addimg.setOnClickListener(view -> addimage());

        btn_Previous.setOnClickListener(view -> {
            ll_firstStep.setVisibility(View.VISIBLE);
            btn_Next.setVisibility(View.VISIBLE);
            ll_secondStep.setVisibility(View.GONE);
            ll_couplesBtns.setVisibility(View.GONE);
        });

        btn_submit_complaint.setOnClickListener(view -> {
            final_images.clear();
            Log.i("selected_images ", selected_images + "");
            Add_Selectionof_Images();
            Log.i("final_images ", final_images + "");
        });

        et_setlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start_Location_Picker();
            }

            private void Start_Location_Picker() {
                Places.initialize(getActivity(), "" + sharedPreferences.getString("google_map_api", ""));
                PlacesClient placesClient = Places.createClient(getActivity());
                List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS);
                 String country = "IN";
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry(country)
                        .build(getActivity());
                startActivityForResult(intent, 1);
            }
        });


        iv_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowPermissions();
            }
        });

        binding.tvDetectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnected(getActivity())) {
                    binding.tvDetectLocation.getRootView().setBackgroundColor(Color.WHITE);
                    isLocationEnabled();
                    if (!isLocationEnabled()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("GPS not available.")
                                .setMessage("Turn on device GPS and set to high accuracy.")
                                .setPositiveButton("Turn on GPS ",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {

                        if (Build.VERSION.SDK_INT >= 23) {
                            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                Log.d("mylog", "Not granted");
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                            } else{
                                Toast.makeText(context, "Wait while GPS fetch your current location", Toast.LENGTH_SHORT).show();
                                requestLocation();
                            }

                        } else{
                            Toast.makeText(context, "Wait while GPS fetch your current location", Toast.LENGTH_SHORT).show();

                            requestLocation();
                        }

                    }

                } else {
                    Snackbar snackbar = Snackbar
                            .make(tv_add_desc, "Slow or No Connection", Snackbar.LENGTH_LONG).setDuration(4000);
                    snackbar.show();
                }
            }
        });

        tv_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_add_image.setVisibility(View.VISIBLE);
                tv_add_image.setCompoundDrawables(null, null, null, null);
            }
        });

        tv_add_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_description.setVisibility(View.VISIBLE);
                tv_add_desc.setCompoundDrawables(null, null, null, null);
            }
        });

        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_setlocation.getText().toString().isEmpty()) {
                    et_setlocation.setError("Enter the Location");
                } else if (Id.isEmpty()) {
                    et_ledCount.setError("Add LED details.");
                } else if (et_description.getText().toString().isEmpty()) {
                    et_description.setError("Enter the Description");
                    et_description.setVisibility(View.VISIBLE);
                } else if (droppoint_id.equals("")) {
                    Toast.makeText(getActivity(), "Select One Drop Point Prefrence", Toast.LENGTH_SHORT).show();
                } else if (latitude.equals("") || longatitude.equals("")) {
                    Toast.makeText(getActivity(), "Value of Latitude and Longitude is Empty ", Toast.LENGTH_SHORT).show();
                } else {


                    ll_firstStep.setVisibility(View.GONE);
                    btn_Next.setVisibility(View.GONE);
                    binding.llSecondStep.setVisibility(View.VISIBLE);
                    ll_couplesBtns.setVisibility(View.VISIBLE);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.rbtn_pick) {
                    radioValue = "PICKUP";
                    text_layout.setVisibility(View.GONE);

                } else if (checkedId == R.id.rbtn_service) {
                    radioValue = "DROP";

                }
            }
        });


        rbtn_service.setOnClickListener(v -> {

            if (latitude.isEmpty() && longatitude.isEmpty() || et_setlocation.getText().toString().equals("")) {
//                    rbtn_service.setChecked(false);
                Toast.makeText(getContext(), "Please Select Location!", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(getActivity(), New_MapsActivity2.class);
                intent.putExtra("startFrom", "NewComplain");
                startActivity(intent);
            }
        });

        text_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (latitude.equals("") && longatitude.equals("") || et_setlocation.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Select Location!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), New_MapsActivity2.class);
                    intent.putExtra("startFrom", "NewComplain");
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
    }

    protected boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getContext().getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    public void Add_Selectionof_Images() {
        if (selected_images.size() > 0) {
            for (int i = 0; i < selected_images.size(); i++) {
                if (!selected_images.get(i).equals("")) {
                    final_images.add(selected_images.get(i));
                }
            }

            if (final_images.size() == 0) {
                vibrate_alert(500);
                Toast.makeText(getContext(), "Add atleast one image to Proceed", Toast.LENGTH_SHORT).show();
                recycler_addphotos.startAnimation(shake);
            } else {
                ////// Start Uploading

//                showloader("UPLOADING THE DATA","It might take more than a minute",true);

                try {
                    putDataIn_JSON();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int sum=0;
                for (int i=0;i<Qty.size();i++){
                    sum+= Integer.parseInt(Qty.get(i));
                }

                newbooking(sharedPreferences.getString("user_id", ""),
                        latitude, longatitude, ""+sum,
                        et_setlocation.getText().toString(),
                        et_description.getText().toString(), radioValue, droppoint_id
                        , ""+jsonArray);


            }
        } else {
            vibrate_alert(500);
            Toast.makeText(getContext(), "Add atleast one image to Proceed", Toast.LENGTH_SHORT).show();
            ll_addimg.startAnimation(shake);
        }
    }

    private void requestLocation() {

        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location mCurrentLocation = locationResult.getLastLocation();

                double lat = mCurrentLocation.getLatitude();
                double longa = mCurrentLocation.getLongitude();
                latitude = String.valueOf(lat);
                longatitude = String.valueOf(longa);

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("lat", latitude);
                intent.putExtra("long", longatitude);
                startActivity(intent);

                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                String cityName = getCityName(myCoordinates);

            }
        };

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        Log.d("mylog", "In Requesting Location");
        if (location != null && (System.currentTimeMillis() - location.getTime()) <= 1000 * 2) {
            myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);

//                myCity = addresses.get(0).getLocality();
                Log.d("mylog", "Complete Address: " + addresses.toString());
                Log.d("mylog", "Address: " + address);

//                Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
//            String cityName = getCityName(myCoordinates);
            //  Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog", "Last location too old getting new location!");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    }

    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    private void AllowPermissions() {
        int hasWritePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasCameraPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        List<String> permissions = new ArrayList<String>();
        if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissions.toArray(new String[permissions.size()]), 101);
        } else {
            imagepickclick();
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissions.toArray(new String[permissions.size()]), 101);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                        imagepickclick();
                        return;
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void imagepickclick() {
        picUri = null;
        CropImage.activity(picUri)
// .setMinCropResultSize(850,500)
// .setAspectRatio(450, 230)
// .setRequestedSize(775,1024)
                .start(getContext(), this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                /////////////// Address //////////////////////////
                String address = place.getAddress() + "";

                et_setlocation.setText("" + address);
                /////////////// Lat-Long //////////////////////////
                latitude = place.getLatLng().latitude + "";
                longatitude = place.getLatLng().longitude + "";

                //  String long=place.getLatLng().longitude+"";

                Log.i("places lat ", "lat: " + latitude + ", ");
                Log.i("places longg ", "longg: " + longatitude + ", ");

                Log.i("places ok ", "Place: " + place.getLatLng() + ", " + place.getAddress());
                // Toast.makeText(getActivity(), ""+address, Toast.LENGTH_SHORT).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("place error ", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("place RESULT_CANCELED ", "RESULT_CANCELED");
                // The user canceled the operation.
            }
        }

        //////////////////////crop image working

        try {

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    try {
                        picUri = result.getUri();
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                        //  bitmap = getResizedBitmap(bitmap, 100);

//                    Saving image to mobile internal memory for sometime
                        String root = getActivity().getFilesDir().toString();
                        File myDir = new File(root + "/dx");
                        myDir.mkdirs();
                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        String fname = "Img" + n + ".jpg";
                        mCurrentPhotoPath = root + "/dx/" + fname;
                        File file1 = new File(myDir, fname);
                        saveFile(bitmap, file1);
                        File file = new File(mCurrentPhotoPath);
                        Log.i("Name", "File" + file.getName());
                        photo_name = file.getName();
                        Log.i("converted_path", "" + file.getPath());
                        img_path = "" + file.getPath();

                        Log.i("img_path", "" + img_path);

                        selected_images.set(currenttapped, img_path);
                        addImagesAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                        img_path = "";
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(getContext(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                    img_path = "";
                }
            }
        } catch (Exception e) {

            Log.i("ImagePick_Exception", e.getMessage());
        }
    }

    void setImageviewPath(Bitmap bitmap) {
        converted_path_featured = converted_path;
    }

    private void saveFile(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();
        try {
            FileOutputStream out = new FileOutputStream(destination);
            sourceUri.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showloader(String title, String desc, boolean isuploading) {
        dialog_progress1 = new Dialog(getActivity());
        dialog_progress1.setContentView(R.layout.dialog_progress1);
        dialog_progress1.setCanceledOnTouchOutside(false);
        TextView tv_dial_title = (TextView) dialog_progress1.findViewById(R.id.tv_dial_title);
        TextView tv_dial_desc = (TextView) dialog_progress1.findViewById(R.id.tv_dial_desc);
        GifImageView gf_uploading = (GifImageView) dialog_progress1.findViewById(R.id.gf_uploading);
        GifImageView gf_finalizing = (GifImageView) dialog_progress1.findViewById(R.id.gf_finalizing);
        tv_dial_title.setText(title);
        tv_dial_desc.setText(desc);

        dialog_progress1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        dialog_progress1.show();

        if (isuploading) {
            gf_uploading.setVisibility(View.VISIBLE);
            gf_finalizing.setVisibility(View.GONE);
        } else {
            gf_uploading.setVisibility(View.GONE);
            gf_finalizing.setVisibility(View.VISIBLE);
        }
    }

    public void dissmissloader1() {
        dialog_progress1.hide();
        dialog_progress1.dismiss();
    }
/////////////////////// API Integration  /////////

    private void Upload_imgs(final String user_id, final String path, final String booking_id) {

        Log.i("vendor_id :", "" + user_id);

        try {

            ll_couplesBtns.setVisibility(View.GONE);
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).build();
            // Change base URL to your upload server URL.
            uploadService = new Retrofit.Builder()
                    .baseUrl(Utils.base_url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Service.class);

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("user_id", user_id);
            builder.addFormDataPart("booking_id", booking_id);

            File sign = new File(path);
            Log.i("img_path :", "" + path);
            Log.i("sign.getName() :", "" + sign.getName());
            builder.addFormDataPart("image", sign.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), sign));
            MultipartBody requestBody = builder.build();
            call = uploadService.addbookingimage(requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string();
                            JSONObject obj = new JSONObject(result);
                            Log.i("Resp Success:", "" + obj);

                            if (obj.has("error")) {
                                dissmissloader1();
                                vibrate_alert(500);
                                String message = obj.getString("message");
                                Log.i("error:", "" + message);
                            } else if (obj.has("warning")) {
                                dissmissloader1();
                                vibrate_alert(500);
                                String message = obj.getString("message");
                                Log.i("warning:", "" + message);
                            } else {

                                if (obj.has("data")) {
                                    JSONArray data_array = obj.getJSONArray("data");
                                    if (data_array.length() > 0) {

                                        reverted_img_names.add(data_array.get(0) + "");
                                        reverted_imgnames = "" + reverted_img_names.toString().replace("[", "").replace("]", "");
                                        Log.i("reverted_imgnames:", reverted_imgnames + "");

                                    }
                                }

                                Log.i("Current Uploaded Img", ":" + current_img_upload_index + " \n " + obj);

                                if (current_img_upload_index == final_images.size() - 1) {

                                    if (dialog_progress1.isShowing()) {

                                        dialog_progress1.dismiss();

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                showloader1(bookingSuccess_message + "");
                                            }
                                        }, 1000);

                                    } else {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                showloader1(bookingSuccess_message + "");
                                            }
                                        }, 1000);
                                    }

                                } else {
                                    current_img_upload_index = current_img_upload_index + 1;
                                    upload_img(booking_id);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("Resp Exc:", "" + e.getMessage() + " \n " + response);

                            ll_couplesBtns.setVisibility(View.VISIBLE);
                            dissmissloader1();
                            //   photoView.showSuccessDialog();
                        }
                    } else {
                        Log.i("Resp error:", "" + response);
                        ll_couplesBtns.setVisibility(View.VISIBLE);
                        dissmissloader1();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("Resp onFail:", "" + t.getMessage());
                    ll_couplesBtns.setVisibility(View.VISIBLE);
                    dissmissloader1();
                }
            });

        } catch (Exception e) {
            ll_couplesBtns.setVisibility(View.VISIBLE);

            Log.i("Resp ex ", e.getMessage() + "");
            dissmissloader1();
        }
    }

    private void lighttypes() {

        try {
            item_id.clear();
            item_max.clear();
            item_title.clear();

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait..");
            //  progressDialog.setCancelable(false);
            progressDialog.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.base_urlv3)
                    .build();
            Service service = retrofit.create(Service.class);
            Call<ResponseBody> call;
            call = service.lighttypes();


            //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("WrongViewCast")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {

                            String result = response.body().string();
                            JSONObject obj = new JSONObject(result);
                            Log.i("Resp Success:", "" + obj);
                            String success = obj.optString("success");


                            if (success.equals("true")) {

                                item_id.add("");
                                item_max.add("");
                                item_title.add("Select type");
                                if (obj.has("data")) {
                                    JSONArray data_array = obj.getJSONArray("data");

                                    if (data_array.length() > 0) {
                                        //   nearbyPropertiesModelArrayList.clear();
                                        for (int i = 0; i < data_array.length(); i++) {
                                            JSONObject jsonObject = data_array.optJSONObject(i);

                                            item_id.add(jsonObject.optString("item_id"));
                                            item_max.add(jsonObject.optString("item_max"));
                                            item_title.add(jsonObject.optString("item_title"));


                                        }
                                        spinner();
                                    }
                                }
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Log.i("Resp Exc:", "" + e.getMessage() + " \n " + response);

                        }
                    } else {
                        progressDialog.dismiss();
                        Log.i("Resp error:", "" + response);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.i("Resp onFail:", "" + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.i("Resp ex ", e.getMessage() + "");
        }
    }

    private void estimatecharges(String ITEMS) {

        Log.i("vendor_id :", "" + ITEMS);

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).build();
            Service uploadService = new Retrofit.Builder()
                    .baseUrl(Utils.base_urlv3)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Service.class);
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("items", ITEMS);
            builder.addFormDataPart("customer_id", sharedPreferences.getString("user_id", ""));
            builder.setType(MultipartBody.FORM);
            MultipartBody requestBody = builder.build();
            call = uploadService.estimatecharges(requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            itemDataList.clear();
                            String result = response.body().string();
                            JSONObject obj = new JSONObject(result);
                            Log.i("Respafter:", "" + obj);
                            String charges = obj.optString("charges");
                            binding.total.setText("₹ " + charges);
                            if (obj.has("data")) {
                                JSONArray data_array = obj.getJSONArray("data");

                                if (data_array.length() > 0) {
                                    //   nearbyPropertiesModelArrayList.clear();
                                    for (int i = 0; i < data_array.length(); i++) {
                                        JSONObject jsonObject = data_array.optJSONObject(i);
                                        ModelPriceList modelPriceList = new ModelPriceList();
                                        modelPriceList.setCHARGES(jsonObject.optInt("CHARGES"));
                                        modelPriceList.setTITLE(jsonObject.optString("TITLE"));
                                        modelPriceList.setPIECES(jsonObject.optString("PIECES"));
                                        modelPriceList.setPERWATT(jsonObject.optString("PERWATT"));
                                        modelPriceList.setWATT(jsonObject.optString("WATT"));

                                        itemDataList.add(modelPriceList);
                                    }
                                    setup_cart_items();
                                }

                                if (itemDataList.size() > 0) {
                                    binding.llExpand.setVisibility(View.VISIBLE);
                                    binding.llCharges.setVisibility(View.VISIBLE);
                                } else {
                                    binding.llExpand.setVisibility(View.GONE);
                                    binding.llCharges.setVisibility(View.GONE);
                                }
                            }
                            progressDialog.dismiss();


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("Resp Exc:", "" + e.getMessage() + " \n " + response);
                            progressDialog.dismiss();
                            //   photoView.showSuccessDialog();
                        }
                    } else {
                        Log.i("Resp error:", "" + response);
                        progressDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("Resp onFail:", "" + t.getMessage());
                    progressDialog.dismiss();

                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();

        }
    }

    public void finalizing() {
    }

    private void upload_img(String booking_id) {
        Upload_imgs(sharedPreferences.getString("user_id", ""),
                final_images.get(current_img_upload_index), booking_id);
    }


    public void newbooking(String user_id, String booking_lat, String booking_long, String quantity
            , String location, String description, String type, String droppoint_id, String items) {

        Log.i("new_user_id :", "" + user_id);
        Log.i("new_booking_lat :", "" + booking_lat);
        Log.i("new_booking_long :", "" + booking_long);
        Log.i("new_quantity :", "" + quantity);
        Log.i("new_location :", "" + location);
        Log.i("new_description :", "" + description);
        Log.i("new_type :", "" + type);
        Log.i("new_droppoint_id :", "" + droppoint_id);

        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).build();

            showloader("UPLOADING THE DATA", "It might take more than a minute", true);// Change base URL to your upload server URL.
            Service uploadService = new Retrofit.Builder()
                    .baseUrl(Utils.base_urlv3)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Service.class);

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
// builder.addFormDataPart("signature", photoSignatureModel.getSign_path());
            builder.addFormDataPart("user_id", user_id);
            builder.addFormDataPart("booking_lat", booking_lat);
            builder.addFormDataPart("booking_long", booking_long);
            builder.addFormDataPart("quantity", quantity);
            builder.addFormDataPart("location", location);
            builder.addFormDataPart("description", description);
            builder.addFormDataPart("type", type);
            builder.addFormDataPart("droppoint_id", droppoint_id);
            builder.addFormDataPart("items", items);

            File sign = new File(img_path);

            Log.i("img_path :", "" + img_path);
            Log.i("sign.getName() :", "" + sign.getName());

            builder.addFormDataPart("image", sign.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), sign));

            requestBody = builder.build();

            Call<ResponseBody> call;

            call = uploadService.newbooking(requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {
                            String result = response.body().string();

                            Log.i("resultBooking", "" + result);
                            JSONObject obj = new JSONObject(result);
                            String success = obj.optString("success");

                            if (obj.has("error")) {
                                dissmissloader1();
                                Toast.makeText(getActivity(), "error " + obj.getString("error_message"), Toast.LENGTH_SHORT).show();

                            } else {

                                bookingSuccess_message = obj.getString("message");
                                JSONObject dataobject = obj.getJSONObject("data");
                                upload_img(dataobject.getString("ID"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            dissmissloader1();
                            btn_submit_complaint.setEnabled(true);
                            Log.i("result_exception", "" + e.getMessage());
//                            Toast.makeText(getActivity(), "catch" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else if (response.code() == 401) {
                        dissmissloader1();
                        btn_submit_complaint.setEnabled(true);
                        Toast.makeText(getActivity(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                    } else {
                        dissmissloader1();
                        btn_submit_complaint.setEnabled(true);

                        Log.d("result_Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                        Toast.makeText(getActivity(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dissmissloader1();
                    Log.i("resultBookingt", "" + t.getMessage());
                    btn_submit_complaint.setEnabled(true);
                    Toast.makeText(getActivity(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception ee) {
            dissmissloader1();
            Log.i("resultBookingException2", "" + ee.getMessage());
            btn_submit_complaint.setEnabled(true);
        }
//        hideloader();
    }

    public void showloader1(String message) {
        dialog_progress = new Dialog(getActivity());
        dialog_progress.setContentView(R.layout.gif_layout);
        dialog_progress.show();
        dialog_progress.setCancelable(false);

        TextView tv_text = (TextView) dialog_progress.findViewById(R.id.tv_text);
        tv_text.setText(message);
        hideloader1();
    }

    public void hideloader1() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        dialog_progress.hide();
                        Complaint.i = 1;
                        Intent intent = new Intent(getActivity(), Complaint.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }, 3000);
    }


    void putDataIn_JSON() throws JSONException {


        if (Id.size() > 0) {

            jsonArray = new JSONArray();

            for (int i = 0; i < Id.size(); i++) {
                try {
                    JSONObject cust = new JSONObject();
                    cust.put("ID", Id.get(i));
                    cust.put("QUANTITY", Qty.get(i));
                    cust.put("WATTS", Watt.get(i));
                    jsonArray.put(cust);
                    Log.i("resp salaryArray", cust + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("resp _rep_Array", jsonArray + "");
        }

    }

    private void
    setup_cart_items() {


        Adapter_Complaint_items adapter_brand = new Adapter_Complaint_items(context, new ItemClickListner() {
            @Override
            public void onClick(String status) {

                try {
                    putDataIn_JSON();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                estimatecharges("" + jsonArray);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        binding.recyclerItems.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) binding.recyclerItems.getItemAnimator()).setSupportsChangeAnimations(false);
        binding.recyclerItems.setAdapter(adapter_brand);

    }


    public interface Service {
        @Headers("Authkey:APPLEDBDMPL")
        @POST("newbooking")
// @FormUrlEncoded
        Call<ResponseBody> newbooking(@Body RequestBody file);


        @Headers("Authkey:APPLEDBDMPL")
        @POST("addbookingimage")
// @FormUrlEncoded
        Call<ResponseBody> addbookingimage(@Body RequestBody file);


        @Headers("Authkey:APPLEDBDMPL")

        @POST("lighttypes")
        Call<ResponseBody> lighttypes();

        @Headers("Authkey:APPLEDBDMPL")
        @POST("estimatecharges")
        Call<ResponseBody> estimatecharges(@Body RequestBody file);

    }
}
