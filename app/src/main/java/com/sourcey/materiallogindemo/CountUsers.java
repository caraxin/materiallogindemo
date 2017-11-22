package com.sourcey.materiallogindemo;

import android.content.Context;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mjaneduan on 11/21/17.
 */

interface CountUsersInterface {
    public void returnList(ArrayList<String> result);
}

public class CountUsers extends AsyncTask<String,Void,ArrayList<String>> {

    Context context;
    CountUsersInterface m_interface;

    CountUsers(){

    }

    void setContext(Context con, CountUsersInterface i) {
        m_interface = i;
        context = con;
    }

    public AsyncResponse delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if(m_interface != null)
            m_interface.returnList(result);
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {

        //replace with username, password, url of your server
        String login_url = "jdbc:mysql://10.0.2.2:8889/bruininfodatabase";
        String database_user = "root";
        String database_password = "root";

        String user_id = params[0];

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(login_url, database_user, database_password);

            PreparedStatement getst = conn.prepareStatement("select user_name from user_info");
            ResultSet rs = getst.executeQuery();

            ArrayList<String> currentUsers = new ArrayList<String>();
            String currentusername;
            String previoususername = "";

            //count the # of unique users currently logged in

            int index = 0;

            while (rs.next()) {
                currentusername = rs.getString(1);

                if (index == 0)
                    currentUsers.add(currentusername);
                else {
                    if (currentusername != previoususername)
                        currentUsers.add(currentusername);
                }

                previoususername = currentusername;
                index++;
            }

            //sort alphabetically

            Collections.sort(currentUsers);

            getst.close();
            conn.close();

            return currentUsers;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
