package com.sourcey.materiallogindemo;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class LearnAboutLocationActivity extends AppCompatActivity {

    Button btnGetLoc;
    TextView textViewDisplayCoordinates;
    String deviceID;

    //new fields

    TextView textViewURL;
    TextView textViewCurrentLandmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //grab device ID
        if(getIntent().hasExtra("org.materiallogindemo.DEVICEID")){
            deviceID = getIntent().getExtras().getString("org.materiallogindemo.DEVICEID");
        }
        else
            deviceID = null;

        btnGetLoc = (Button) findViewById(R.id.btnGetLoc);
        textViewDisplayCoordinates = (TextView) findViewById(R.id.textViewDisplayCoordinates);
        ActivityCompat.requestPermissions(LearnAboutLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        btnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPStracker g = new GPStracker(getApplicationContext());
                Location l = g.getLocation();
                if(l != null){
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    System.out.println("Learn about Location Activity!: " + lat + ", " + lon);
                    textViewDisplayCoordinates.setText("You are currently at: " + lat + ", " + lon);

                    String latString = String.valueOf(lat);
                    String lonString = String.valueOf(lon);

                    SendCoordinates sendCoordinates = new SendCoordinates(getApplicationContext());
                    sendCoordinates.execute(deviceID, latString, lonString);

                    textViewURL = (TextView) findViewById(R.id.textViewURL);
                    textViewCurrentLandmark =  findViewById(R.id.textViewCurrentLandmark);

                    //Toast.makeText(getApplicationContext(), "LAT: " + latString + "\nLONG: " + lonString, Toast.LENGTH_LONG).show();
                }
                else {
                    textViewDisplayCoordinates.setText("geo is null");
                }

                CheckDistance checkDistance = new CheckDistance(getApplicationContext(), new CheckDistInterface() {
                    @Override
                    public void returnString(ArrayList<String> result) {

                        String currentLandmark = result.get(0);
                        String usefulURL = result.get(1);

                        textViewCurrentLandmark.setText(currentLandmark);
                        textViewURL.setText(usefulURL);
                    }
                });

                checkDistance.execute(deviceID, l);
            }
        });


    }
}
