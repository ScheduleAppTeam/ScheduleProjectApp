package com.example.scheduleprojectapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LoadingGlobalDBtoLocalDBTask extends AsyncTask<String,Integer,String> {
    private ProgressDialog pd;

    @Override
    protected String doInBackground(String... arg0) {
        try{
            String scriptname = (String)arg0[0];
            Log.i(Constants.LOG_TAG, "Loading global DB");
            String link = "http://uppdemonstration.comoj.com/" + scriptname;
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
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
