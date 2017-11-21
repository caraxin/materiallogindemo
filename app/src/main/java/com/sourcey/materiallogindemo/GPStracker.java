package com.sourcey.materiallogindemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by mjaneduan on 11/16/17.
 */

public class GPStracker implements LocationListener {

    //members

    Context context;

    //methods

    //constructor
    public GPStracker(Context c){
        context = c;
    }

    //get location, if possible
    public Location getLocation(){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT);
            return null;
        }


        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isGPSEnabled){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 1, this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            System.out.println("detect the location!");
            return l;
        }else{
            System.out.println("Please enable GPS!");
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG);
        }

        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}