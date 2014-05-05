package com.example.scheduleprojectapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by kenny on 10.04.14.
 */
public class UpdateUserTask extends AsyncTask<String,Integer,String> {
    private ProgressDialog pd;
    private Activity activity;

    public UpdateUserTask(Activity _activity) {
        activity = _activity;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            String login = (String)arg0[0];
            String sname = (String)arg0[1];
            String fname = (String)arg0[2];
            String password = (String)arg0[3];
            String aut = (String)arg0[4];
            String oldlogin = (String)arg0[5];

            String link = "http://uppdemonstration.comoj.com/userupdate.php";
            String data  = URLEncoder.encode("login", "UTF-8")
                    + "=" + URLEncoder.encode(login, "UTF-8");
            data += "&" + URLEncoder.encode("fname", "UTF-8")
                    + "=" + URLEncoder.encode(fname, "UTF-8");
            data += "&" + URLEncoder.encode("sname", "UTF-8")
                    + "=" + URLEncoder.encode(sname, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("aut", "UTF-8")
                    + "=" + URLEncoder.encode(aut, "UTF-8");
            data += "&" + URLEncoder.encode("oldlogin", "UTF-8")
                    + "=" + URLEncoder.encode(oldlogin, "UTF-8");

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
