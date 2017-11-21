package com.sourcey.materiallogindemo;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by mjaneduan on 11/20/17.
 */

interface CheckDistInterface {
    public void returnString(ArrayList<String> result);
}

public class CheckDistance extends AsyncTask<Object,Void,ArrayList<String>> {

    Context context;
    CheckDistInterface m_interface;

    CheckDistance(Context c, CheckDistInterface i){
        context = c;
        m_interface = i;
    }

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
            m_interface.returnString(result);
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {

        //replace with username, password, url of your server
        String login_url = "jdbc:mysql://10.0.2.2:8889/bruininfodatabase";
        String database_user = "root";
        String database_password = "root";

        String user_id = (String) params[0];
        Location user_loc = (Location) params[1];

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(login_url, database_user, database_password);

            PreparedStatement getst = conn.prepareStatement("select landmark_name,landmark_lat,landmark_long, landmark_url from ucla_landmarks");
            ResultSet rs = getst.executeQuery();

            double landmarklat;
            double landmarklong;
            String landmarkname;
            String landmarkurl;

            ArrayList<String> closelandmarknames = new ArrayList<String>();
            ArrayList<Double> closelandmarkdists = new ArrayList<Double>();
            ArrayList<String> closelandmarkurls = new ArrayList<String>();

            //go through all stored landmarks
            //store & return whichever is closest to user's current location

            while(rs.next())
            {
                landmarkname = rs.getString(1);
                landmarklat = rs.getDouble(2);
                landmarklong = rs.getDouble(3);
                landmarkurl = rs.getString(4);

                Location landmark_loc = new Location("");
                landmark_loc.setLatitude(landmarklat);
                landmark_loc.setLongitude(landmarklong);

                double dist = user_loc.distanceTo(landmark_loc);

                if(dist <= 10.0)
                {
                    closelandmarknames.add(landmarkname);
                    closelandmarkdists.add(dist);
                    closelandmarkurls.add(landmarkurl);
                }

            }

            int landmarklistlength = closelandmarknames.size();
            int mindistindex = 0;
            String mindistname = closelandmarknames.get(0);
            double mindistance = closelandmarkdists.get(0);
            for(int i = 0; i < landmarklistlength; i++)
            {
                double currentDist = closelandmarkdists.get(i);
                if(currentDist < mindistance)
                {
                    mindistindex = i;
                    mindistance = currentDist;
                    mindistname = closelandmarknames.get(i);
                }

            }

            String returnedURL = closelandmarkurls.get(mindistindex);

            ArrayList<String> returnedInfo = new ArrayList<String>();
            returnedInfo.add(mindistname);
            returnedInfo.add(returnedURL);

            getst.close();
            conn.close();

            return returnedInfo;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

}
