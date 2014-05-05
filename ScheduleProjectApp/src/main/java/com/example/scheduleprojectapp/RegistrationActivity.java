package com.example.scheduleprojectapp;

import android.app.Activity;
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

public class RegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void CheckFields() throws Exception{
        final EditText FirstNameET = (EditText)findViewById(R.id.FirstNameET);
        final EditText SecondNameET = (EditText)findViewById(R.id.SecondNameET);
        final EditText EmailET = (EditText)findViewById(R.id.EmailET);
        final EditText PasswordET = (EditText)findViewById(R.id.PasswordET);
        final EditText PasswordRepeatET = (EditText)findViewById(R.id.PasswordRepeatET);
        Context context;
        try{
            context = getApplicationContext();
        }catch (NullPointerException ex){
            // TODO: handle exception right
            Log.i(Constants.LOG_TAG, "context nullpointerexception");
            context = null;
        }

        try {
            FormChecker.isFilled(FirstNameET, "Введите имя");
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.isFilled(SecondNameET, "Введите фамилию");
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.isFilled(EmailET, "Введите e-mail");
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.isFilled(PasswordET, "Введите пароль");
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.ValidateFName(FirstNameET);
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.ValidateSName(SecondNameET);
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.FNameAndSNameIsUniform(FirstNameET, SecondNameET);
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.ValidateEmail(EmailET);
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.ValidatePassword(PasswordET);
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try {
            FormChecker.ValidatePasswordAndPasswordRepeat(PasswordET, PasswordRepeatET);
        }catch (Exception E){
            Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
            throw new Exception();
        }

        try
        {
            RegisteringWork();
        }catch (NullPointerException ex){
            Log.i(Constants.LOG_TAG, "null edittext in registration function");
        }
    }

    private void RegisteringWork() throws NullPointerException{
        final EditText FirstNameET = (EditText)findViewById(R.id.FirstNameET);
        final EditText SecondNameET = (EditText)findViewById(R.id.SecondNameET);
        final EditText EmailET = (EditText)findViewById(R.id.EmailET);
        final EditText PasswordET = (EditText)findViewById(R.id.PasswordET);

        String fName;
        String sName;
        String email;
        String password;

        try{
            fName = FirstNameET.getText().toString();
            sName = SecondNameET.getText().toString();
            email = EmailET.getText().toString();
            password = PasswordET.getText().toString();
        }catch (NullPointerException ex){
            throw new NullPointerException();
        }

        new SigningWorker(this).execute(email, fName, sName, password);
    }

    public void RegistrationButtonClick(View v){
        try{
            CheckFields();
        }catch (Exception E){
            Log.i(Constants.LOG_TAG, "Mistakes in registration form");
        }
    }

    public void CancelButtonClick(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    public class SigningWorker  extends AsyncTask<String,Integer,String> {
        private Context context;
        private String login;
        private String fname;
        private String sname;

        public SigningWorker(Activity activity) {
            context = activity.getApplicationContext();
        }

        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(String... arg0) {
            try{
                login = arg0[0];
                fname = arg0[1];
                sname = arg0[2];
                String password = arg0[3];

                String link = "http://uppdemonstration.comoj.com/registration.php";
                String data  = URLEncoder.encode("login", "UTF-8")
                        + "=" + URLEncoder.encode(login, "UTF-8");
                data += "&" + URLEncoder.encode("fname", "UTF-8")
                        + "=" + URLEncoder.encode(fname, "UTF-8");
                data += "&" + URLEncoder.encode("sname", "UTF-8")
                        + "=" + URLEncoder.encode(sname, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(password, "UTF-8");
                data += "&" + URLEncoder.encode("aut", "UTF-8")
                        + "=" + URLEncoder.encode(String.valueOf(Constants.DEFAULT_AUT), "UTF-8");

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
                String line;
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            }catch(Exception e){
                return Constants.NO_INTERNET_CONNECTION;
            }
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result){
            if (result.equals(Constants.REGISTRATION_CLEAR)){
                Toast.makeText(context, "Регистрация пройдена", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);

                finish();
            }else if (result.equals(Constants.DUPLICATE_LOGIN)){
                Toast.makeText(context, "Такой пользователь уже есть", Toast.LENGTH_LONG).show();
            }else if (result.equals(Constants.UNBOUND_USER)){
                Toast.makeText(context, "Достигнут лимит регистраций", Toast.LENGTH_LONG).show();
            }else if (result.equals(Constants.NO_INTERNET_CONNECTION)){
                Toast.makeText(context, "Отсутствует интернет соединение", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "Что-то пошло не по-плану", Toast.LENGTH_LONG).show();
            }

        }
    }
}
