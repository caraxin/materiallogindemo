package com.sourcey.materiallogindemo;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by mjaneduan on 11/16/17.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {

    Context context;

    BackgroundTask(){
    }

    void setContext(Context con) {
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
    protected void onPostExecute(String result) {

        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        delegate.processFinish(result);
    }


    @Override
    protected String doInBackground(String... params) {
        String login_url = "http://10.0.2.2:8006/BruinsInfo/Login";
        String signup_url = "http://10.0.2.2:8006/BruinsInfo/Signup";

        String method = params[0];
        if(method.equals("login")){

            String user_email = params[1];
            String user_pass = params[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                //encode
                String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8") + "&" +
                        URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "UTF-8"));
                String result = bufferedReader.readLine();
                IS.close();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        else if (method.equals("signup")) {
            String user_name = params[1];
            String user_pass = params[2];
            String user_email = params[3];
            String user_phone = params[4];
            String user_address = params[5];
            System.out.println(user_name + " " + user_email + " " + user_pass + " " + user_phone + " " + user_address);
            try {
                URL url = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user_name", user_name);
                jsonParam.put("user_pass", user_pass);
                jsonParam.put("user_email", user_email);
                jsonParam.put("user_phone", user_phone);
                jsonParam.put("user_address", user_address);
                String encoded = URLEncoder.encode("param", "UTF-8") + "=" + URLEncoder.encode(jsonParam.toString(), "UTF-8");
                bufferedWriter.write(encoded);

                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "UTF-8"));
                String result = bufferedReader.readLine();
                IS.close();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
