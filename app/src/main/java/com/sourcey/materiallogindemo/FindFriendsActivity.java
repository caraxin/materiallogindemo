package com.sourcey.materiallogindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsActivity extends AppCompatActivity{

    String deviceID;
    ListView listViewUsers;
    Button btnRefreshUserList;

    //holds list of all people 'logged in' in database
    List<String> userList;
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        btnRefreshUserList = (Button) findViewById(R.id.btnRefreshUserList);

        userList = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userList);
        listViewUsers.setAdapter(listAdapter);

        //device id unique to every user
        //whether to store in database & use is up to you

        if(getIntent().hasExtra("org.materiallogindemo.DEVICEID")){
            deviceID = getIntent().getExtras().getString("org.materiallogindemo.DEVICEID");
        }
        else
            deviceID = null;


        //every button click refreshes list

        btnRefreshUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountUsers countUsers = new CountUsers();
                countUsers.setContext(getApplicationContext(), new CountUsersInterface() {
                    @Override
                    public void returnList(ArrayList<String> result) {
                        userList = result;
                    }
                });
                countUsers.execute(deviceID);
                listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userList);
                listViewUsers.setAdapter(listAdapter);
            }
        });

    }
}
