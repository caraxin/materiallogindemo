package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class MenuActivity extends AppCompatActivity {
    private static final String getuser_url = "http://131.179.6.219:8006/BruinsInfo/GetUser";
    ImageButton btnLearnAboutLocation;
    ImageButton btnAboutMe;
    String email = null;

    //obtain unique id on successful login
    String deviceID = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if(getIntent().hasExtra("org.materiallogindemo.EMAIL")){
            email = getIntent().getExtras().getString("org.materiallogindemo.EMAIL");
            System.out.println("Menu get email info: " + email);
        }
        else System.out.println("menu cannot get email info!");

        btnLearnAboutLocation = (ImageButton) findViewById(R.id.btnLearnAboutLocation);
        btnAboutMe = (ImageButton) findViewById(R.id.btnAboutMe);

        btnLearnAboutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoLearnAboutLocation = new Intent(getApplicationContext(), LearnAboutLocationActivity.class);
                gotoLearnAboutLocation.putExtra("org.materiallogindemo.EMAIL", email);
                startActivity(gotoLearnAboutLocation);
            }
        });

        btnAboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(), new ProcessResult() {
                    @Override
                    public void returnString(String result) {
                        System.out.println("GetUser result: " + result);
                        Intent goToAboutMe = new Intent(getApplicationContext(), AboutMeActivity.class);
                        goToAboutMe.putExtra("org.materiallogindemo.INFO", result);
                        startActivity(goToAboutMe);
                    }
                });

                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String param = jsonParam.toString();

                backgroundTask.execute(getuser_url, param);
            }
        });

    }
}
