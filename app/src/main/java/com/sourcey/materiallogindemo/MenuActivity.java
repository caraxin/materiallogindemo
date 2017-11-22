package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.UUID;

public class MenuActivity extends AppCompatActivity {

    ImageButton btnLearnAboutLocation;
    ImageButton btnFindFriends;

    //obtain unique id on successful login
    String deviceID = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnLearnAboutLocation = (ImageButton) findViewById(R.id.btnLearnAboutLocation);
        btnFindFriends = (ImageButton) findViewById(R.id.btnFindFriends);

        btnLearnAboutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoLearnAboutLocation = new Intent(getApplicationContext(), LearnAboutLocationActivity.class);
                gotoLearnAboutLocation.putExtra("org.materiallogindemo.DEVICEID", deviceID);
                startActivity(gotoLearnAboutLocation);
            }
        });

        btnFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToFindFriends = new Intent(getApplicationContext(), FindFriendsActivity.class);
                goToFindFriends.putExtra("org.materiallogindemo.DEVICEID", deviceID);
                startActivity(goToFindFriends);
            }
        });

    }
}
