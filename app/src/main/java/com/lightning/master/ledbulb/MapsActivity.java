package com.lightning.master.ledbulb;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lightning.master.ledbulb.Complaint.fragfortabs.TabFragment1;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String addresses = "";

    ImageView pin;
    Button btn_set_location;
    TextView text;
    String radioValue="PICKUP";
    public static String  address="";
    public static double final_lattitude=0,final_longitude=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        text = (TextView)findViewById(R.id.text);
        pin = (ImageView)findViewById(R.id.pin);
        btn_set_location = (Button) findViewById(R.id.btn_set_location);



      /*  btn_set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                text.setText(address);
                TabFragment1.et_setlocation.setText(address);
                Intent intent = new Intent(getApplicationContext(), Complaint.class);
                startActivity(intent);
                finish();
            }
        });*/

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
    @Override
    public void onMapReady(GoogleMap googleMap) {

        double lat = Double.parseDouble(getIntent().getExtras().getString("lat"));
        double longa = Double.parseDouble(getIntent().getExtras().getString("long"));

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng thislatlong = new LatLng(lat, longa);
        mMap.addMarker(new MarkerOptions().position(thislatlong).draggable(false).visible(false));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thislatlong, 15));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(thislatlong, 15));


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = mMap.getCameraPosition().target;
//                mMap.addMarker(new MarkerOptions().position(center));
                String cityName = getCityName(center);
//                Toast.makeText(MapsActivity.this, ""+center, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);

            address = addresses.get(0).getAddressLine(0);
//            double lat = addresses.get(0).getLatitude();
//            double longa = addresses.get(0).getLongitude();

            TabFragment1.latitude = String.valueOf(addresses.get(0).getLatitude());
            TabFragment1.longatitude = String.valueOf(addresses.get(0).getLongitude());
//            final_longitude = addresses.get(0).getLongitude();
         //   Toast.makeText(this, "lat :" + lat + "\n long : " + longa, Toast.LENGTH_SHORT).show();
         /*   Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
*/

            myCity = addresses.get(0).getLocality();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);

            text.setText(address);
            btn_set_location.setVisibility(View.VISIBLE);
            btn_set_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TabFragment1.et_setlocation.setText(address);
//                    Intent intent = new Intent(getApplicationContext(), Complaint.class);
//                    startActivity(intent);
                    finish();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
