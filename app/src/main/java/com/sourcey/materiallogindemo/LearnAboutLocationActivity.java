package com.sourcey.materiallogindemo;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LearnAboutLocationActivity extends AppCompatActivity {
    private static final String geoinfo_url = "http://10.0.2.2:8006/BruinsInfo/GeoInfo";

    Button btnGetLoc;
    TextView textViewDisplayCoordinates;
    String deviceID;

    //new fields

    TextView textViewURL;
    TextView textViewCurrentLandmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_about_location);

        //grab device ID
        if(getIntent().hasExtra("org.materiallogindemo.DEVICEID")){
            deviceID = getIntent().getExtras().getString("org.materiallogindemo.DEVICEID");
        }
        else
            deviceID = null;

        textViewURL = (TextView) findViewById(R.id.textViewURL);
        textViewCurrentLandmark =  findViewById(R.id.textViewCurrentLandmark);
        btnGetLoc = (Button) findViewById(R.id.btnGetLoc);
        textViewDisplayCoordinates = (TextView) findViewById(R.id.textViewDisplayCoordinates);
        ActivityCompat.requestPermissions(LearnAboutLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        btnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPStracker g = new GPStracker(getApplicationContext());
                Location l = g.getLocation();
                double user_latitude, user_longitude;
                if(l != null){
                    user_latitude = l.getLatitude();
                    user_longitude = l.getLongitude();
                    System.out.println("Learn about Location Activity!: " + user_latitude + ", " + user_longitude);
                    textViewDisplayCoordinates.setText("You are currently at: " + user_latitude + ", " + user_longitude);
                }
                else {
                    user_latitude = 34.0716;
                    user_longitude = -118.4422;
                    textViewDisplayCoordinates.setText("geo is null, the default location is: 34.0716, -118.4422, for testing");
                }

                BackgroundTask task = new BackgroundTask(getApplicationContext(), new ProcessResult() {
                    @Override
                    public void returnString(String result) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            String name = obj.getString("name");
                            double latitude = obj.getDouble("latitude");
                            double longitude = obj.getDouble("longitude");
                            String url = obj.getString("url");
                            double distance = obj.getDouble("distance");

                            textViewCurrentLandmark.setText(name);
                            textViewURL.setText(url);
                            System.out.println("distance = " + distance);
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
        });


    }
}
