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
import java.util.ArrayList;

/**
 * Created by mjaneduan on 11/16/17.
 */
interface ProcessResult {
    public void returnString(String result);
}

public class BackgroundTask extends AsyncTask<String,Void,String> {

    Context context;
    ProcessResult m_processResult;

    BackgroundTask(Context c, ProcessResult p){
        context = c;
        m_processResult = p;
    }

    void setContext(Context con) {
        context = con;
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

        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if (m_processResult != null) {
            m_processResult.returnString(result);
        }
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            System.out.println("backgroudTask: url = " + params[0]);
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

            String param = params[1];
            String encoded = URLEncoder.encode("param", "UTF-8") + "=" + URLEncoder.encode(param, "UTF-8");

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
        }
    }
}
