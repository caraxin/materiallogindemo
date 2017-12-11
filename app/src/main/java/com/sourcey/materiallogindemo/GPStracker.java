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
    LocationManager lm;
    boolean canProceed;

    //methods

    //constructor
    public GPStracker(Context c){
        context = c;
        System.out.println("constructing GPSTracker");
        if (!checkPermission())
            canProceed = false;
        else
        {
            //Make Location Manager once
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled){
                System.out.println("Network provider not enabled.");
                canProceed = false;
            }
            else
            {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 3, this);
                canProceed = true;
                System.out.println("Ok to proceed.");
            }
        }

    }

    //new method for checking ACCESS_COARSE_LOCATION permissions

    public boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            System.out.println("Permission not granted.");
            return false;
        }
        return true;
    }

    //get location, if possible
    public Location getLocation(){
        /*Toast.makeText(context, "entering gpsTracker", Toast.LENGTH_SHORT);
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
        return null;*/
        if(!canProceed){
            System.out.println("Can't proceed...");
            return null;
        }
        if(!checkPermission()) {
            System.out.println("No permission...");
            return null;
        }

        //FOR RUNNING APPLICATION ON EMULATOR,
        //BUILD VIRTUAL DEVICE WITH APK >= 26NE
        //AND CHANGE PROVIDER TO GPS_PROVIDER

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
