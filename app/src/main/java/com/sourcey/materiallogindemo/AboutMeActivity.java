package com.sourcey.materiallogindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AboutMeActivity extends AppCompatActivity{
    ListView listInfo;

    List<String> infoList;
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        listInfo = (ListView) findViewById(R.id.listInfo);

        infoList = new ArrayList<String>();
        //listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userList);
        //listInfo.setAdapter(listAdapter);

        //device id unique to every user
        //whether to store in database & use is up to you

        String info = getIntent().getExtras().getString("org.materiallogindemo.INFO");
        try {
            JSONObject obj = new JSONObject(info);
            infoList.add("name: " + obj.getString("user_name"));
            infoList.add("email: " + obj.getString("user_email"));
            infoList.add("phone: " + obj.getString("user_phone"));
            infoList.add("address: " + obj.getString("user_address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, infoList);
        listInfo.setAdapter(listAdapter);
    }
}
