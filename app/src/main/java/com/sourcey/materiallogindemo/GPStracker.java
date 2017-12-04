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
        System.out.println("constructing GPSTracker");
    }

    //get location, if possible
    public Location getLocation(){
        Toast.makeText(context, "entering gpsTracker", Toast.LENGTH_SHORT);
        System.out.println("entering gpsTracker");
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            System.out.println("**********0**********");
            return null;
        }


        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSEnabled){
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 3, this);
            Location l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (l == null) {
                Toast.makeText(context, "location is ", Toast.LENGTH_SHORT);
                System.out.println("**********************location is null!*************************");
            }
            else {
                System.out.println("location is not null");
                Toast.makeText(context, "location: " + l.getLatitude() + " " + l.getLongitude(), Toast.LENGTH_SHORT);
                System.out.println(l.getLatitude() + " " + l.getLongitude());
            }
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
