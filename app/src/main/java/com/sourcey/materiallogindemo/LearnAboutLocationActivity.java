package com.sourcey.materiallogindemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import im.delight.android.location.SimpleLocation;

public class LearnAboutLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String geoinfo_url = "http://131.179.6.219:8006/BruinsInfo/GeoInfo";

    Button btnGetLoc;
    double user_latitude = 34.0686201;
    double user_longitude = -118.442857;

    GoogleMap m_googleMap;
    Marker marker = null;

    //private SimpleLocation location;
    String email;

    SimpleLocation location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_about_location);

        email = getIntent().getExtras().getString("org.materiallogindemo.EMAIL");
        btnGetLoc = (Button) findViewById(R.id.btnGetLoc);

        ActivityCompat.requestPermissions(LearnAboutLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        /*
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT);
            return;
        }

        location = new SimpleLocation(this, true, false, 5 * 1000, true);
        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
        location.beginUpdates();
        user_latitude = location.getLatitude();
        user_longitude = location.getLongitude();
        location.setListener(new SimpleLocation.Listener() {
            @Override
            public void onPositionChanged() {
                user_latitude = location.getLatitude();
                user_longitude = location.getLongitude();
            }
        });
        */

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        updateMap2();

        btnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMap2();
            }
        });
    }

    void updateMap2() {
        GPStracker g = new GPStracker(getApplicationContext());
        Location l = g.getLocation();
        if(l != null){
            user_latitude = l.getLatitude();
            user_longitude = l.getLongitude();
            System.out.println("Learn about Location Activity!: " + user_latitude + ", " + user_longitude);
            //textViewDisplayCoordinates.setText("You are currently at: " + user_latitude + ", " + user_longitude);
        }
        else {
            user_latitude = 34.0686201;
            user_longitude = -118.442857;
            //textViewDisplayCoordinates.setText("geo is null, the default location is: 34.0716, -118.4422, for testing");
            System.out.println("geo is null, the default location is: 34.0686201, -118.442857, for testing");
        }

        BackgroundTask task = new BackgroundTask(getApplicationContext(), new ProcessResult() {
            @Override
            public void returnString(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String landmark_name = obj.getString("name");
                    double latitude = obj.getDouble("latitude");
                    double longitude = obj.getDouble("longitude");
                    String landmark_url = obj.getString("url");
                    double distance = obj.getDouble("distance");
                    int user_count = obj.getInt("userCount");

                    marker.setPosition(new LatLng(user_latitude, user_longitude));
                    marker.setTitle(landmark_name);
                    marker.setSnippet("useful url: " + landmark_url + "\n current users: " + user_count);
                    marker.showInfoWindow();
                    System.out.println("landmark = " + landmark_name + "distance = " + distance + " user count = " + user_count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("latitude", user_latitude);
            jsonParam.put("longitude", user_longitude);
            jsonParam.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String param = jsonParam.toString();
        task.execute(geoinfo_url, param);
    }

    void updateMap() {
        user_latitude = location.getLatitude();
        user_longitude = location.getLongitude();
        System.out.println("Learn about Location Activity!: " + user_latitude + ", " + user_longitude);
        BackgroundTask task = new BackgroundTask(getApplicationContext(), new ProcessResult() {
            @Override
            public void returnString(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String landmark_name = obj.getString("name");
                    double latitude = obj.getDouble("latitude");
                    double longitude = obj.getDouble("longitude");
                    String landmark_url = obj.getString("url");
                    double distance = obj.getDouble("distance");
                    int user_count = obj.getInt("userCount");

                    //textViewCurrentLandmark.setText(landmark_name);
                    //textViewURL.setText(landmark_url);
                    marker.setPosition(new LatLng(user_latitude, user_longitude));
                    marker.setTitle(landmark_name);
                    marker.setSnippet("useful url: " + landmark_url + "\n current users: " + user_count);
                    marker.showInfoWindow();
                    System.out.println("landmark = " + landmark_name + "distance = " + distance + " user count = " + user_count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("latitude", user_latitude);
            jsonParam.put("longitude", user_longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String param = jsonParam.toString();
        task.execute(geoinfo_url, param);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        m_googleMap = googleMap;
        m_googleMap.setMinZoomPreference(15.0f);
        m_googleMap.setMaxZoomPreference(20.0f);
        LatLng current_pos = new LatLng(user_latitude, user_longitude);
        marker = m_googleMap.addMarker(new MarkerOptions().position(current_pos));
        m_googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                TextView title = (TextView) v.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView content = (TextView) v.findViewById(R.id.content);
                content.setText(marker.getSnippet());

                return v;

            }
        });
        m_googleMap.moveCamera(CameraUpdateFactory.newLatLng(current_pos));
    }

    /*
    @Override
    protected void onDestroy() {
        // stop location updates (saves battery)
        location.endUpdates();
        super.onDestroy();
    }
    */
}
