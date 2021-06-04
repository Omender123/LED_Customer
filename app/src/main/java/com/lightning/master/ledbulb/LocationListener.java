package com.lightning.master.ledbulb;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by RAHUL on 9/27/2017.
 */

public class LocationListener implements android.location.LocationListener {

    Location mLastLocation;
    TargetView.Targetview targetview;


    public LocationListener(String provider, TargetView.Targetview targetview) {
        Log.d("LocationListener ", "LocationListener " + provider);
        mLastLocation = new Location(provider);
        this.targetview=targetview;

    }

    @Override
    public void onLocationChanged(final Location location) {
        targetview.locationchange(location);
        Log.d("onLocationChanged: ", "" + location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("onProviderEnabled: ", provider);
    }

    @Override
    public void onProviderEnabled(String provider) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Log.d("onStatusChanged: ", provider);
    }


}