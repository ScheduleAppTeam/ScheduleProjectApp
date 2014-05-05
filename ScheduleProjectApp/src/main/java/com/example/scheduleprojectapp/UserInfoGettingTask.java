package com.example.scheduleprojectapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UserInfoGettingTask  extends AsyncTask<String,Integer,String> {
    private ProgressDialog pd;
    private Activity activity;
    private String login;
    public UserInfoGettingTask(Activity _activity) {
        activity = _activity;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            login = (String)arg0[0];

            String link = "http://uppdemonstration.comoj.com/userget.php";
            String data  = URLEncoder.encode("login", "UTF-8")
                    + "=" + URLEncoder.encode(login, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }

            return sb.toString();
        }catch(Exception e){
            return new String(Constants.NO_INTERNET_CONNECTION);
        }
    }
}