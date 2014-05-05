package com.example.scheduleprojectapp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static com.example.scheduleprojectapp.NecessaryFunctions.isOnline;

@SuppressWarnings("ALL")
public class LoginActivity extends Activity {

    private SigninActivity mt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public Object onRetainNonConfigurationInstance() {
        return mt;
    }

    public void RegistrationButtonClick(View v)
    {
        if (isOnline(getApplicationContext())){
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Нет интернет соединения", Toast.LENGTH_LONG).show();
        }
    }

    public Boolean FieldsNonEmpty(String email, String password){
        return ((!(email.isEmpty())) && (!(password.isEmpty())));
    }

    public void LoginButtonClick(View v){
        final EditText EmailET;
        EmailET = (EditText)findViewById(R.id.EmailET);
        final EditText PasswordET;
        PasswordET = (EditText)findViewById(R.id.PasswordET);

        final String email = EmailET.getText().toString();
        final String password = PasswordET.getText().toString();

        if (FieldsNonEmpty(email, password)){
            mt = (SigninActivity)getLastNonConfigurationInstance();
            if (mt == null) {
                new SigninActivity(this).execute(email, password);
            }
        }
        else{
            Toast.makeText(this.getApplicationContext(), "Введите данные", Toast.LENGTH_LONG).show();
        }
    }

    public class SigninActivity  extends AsyncTask<String,Integer,String>{
        private ProgressDialog pd;
        private Activity activity;
        private String login;
        public SigninActivity(Activity _activity) {
            activity = _activity;
        }

        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(String... arg0) {
            try{
                login = (String)arg0[0];
                String password = (String)arg0[1];
                String link = "http://uppdemonstration.comoj.com/login.php";
                String data  = URLEncoder.encode("login", "UTF-8")
                        + "=" + URLEncoder.encode(login, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(password, "UTF-8");
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

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result){
            if (result.equals(Constants.AUTHORIZATION_ACCEPTED)){

                UserInfoGettingTask ugt = new UserInfoGettingTask(activity);
                ugt.execute(login);
                String responseResult = null;

                try {
                    responseResult = ugt.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String [] resultArray = responseResult.split(" ");

                UserTableDataBaseHelper db = new UserTableDataBaseHelper(activity.getApplicationContext());

                User user = new User(Constants.DEFAULT_USER_APP_ID, login, resultArray[0], resultArray[1], Integer.valueOf(resultArray[2]), Constants.DEFAULT_USER_PERMISSION);

                db.dropDB();
                db.addUser(user);

                Toast.makeText(activity.getApplicationContext(), "Авторизация пройдена", Toast.LENGTH_LONG).show();

                Intent in = new Intent(activity.getApplicationContext(), ScheduleRefreshService.class);
                startService(in);

                Intent i = new Intent(activity.getApplicationContext(), ScheduleLoginService.class);
                startService(i);

                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(intent);

                finish();
            }else if (result.equals(Constants.INCORRECT_PASSWORD)){
                Toast.makeText(activity.getApplicationContext(), "Неправильный пароль", Toast.LENGTH_LONG).show();
            }else if (result.equals(Constants.INCORRECT_ALL)){
                Toast.makeText(activity.getApplicationContext(), "Неправильный пароль и логин", Toast.LENGTH_LONG).show();
            }else if (result.equals(Constants.NO_INTERNET_CONNECTION)){
                Toast.makeText(activity.getApplicationContext(), "Отсутствует интернет соединение", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(activity.getApplicationContext(), "Что-то пошло не по-плану", Toast.LENGTH_LONG).show();
            }

        }
    }
}