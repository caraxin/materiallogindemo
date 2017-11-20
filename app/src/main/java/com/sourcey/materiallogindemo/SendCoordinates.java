package com.sourcey.materiallogindemo;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by mjaneduan on 11/16/17.
 */

public class SendCoordinates extends AsyncTask<String,Void,String> {

    Context context;

    SendCoordinates(Context c){

        context = c;
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
    protected void onPostExecute(String result) {

        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... params) {

        String login_url = "http://10.0.2.2:8006/sendcoordinates.php";

        String user_id = params[0];
        String user_lat = params[1];
        String user_long = params[2];

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

            //encode
            String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&" +
                    URLEncoder.encode("user_lat", "UTF-8") + "=" + URLEncoder.encode(user_lat, "UTF-8") + "&" +
                    URLEncoder.encode("user_long", "UTF-8") + "=" + URLEncoder.encode(user_long, "UTF-8") + "&";


            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            InputStream IS = httpURLConnection.getInputStream();
            IS.close();

            return "Successfully retrieved location.";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
