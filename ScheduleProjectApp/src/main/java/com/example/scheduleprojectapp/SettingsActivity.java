package com.example.scheduleprojectapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.scheduleprojectapp.NecessaryFunctions.isOnline;

public class SettingsActivity extends Activity {

    EditText FirstNameET;
    EditText SecondNameET;
    EditText EmailET;
    EditText PasswordET;
    EditText PasswordRepeatET;
    String oldLogin;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.i(Constants.LOG_TAG, "settings activity created");

        spinner = (Spinner)findViewById(R.id.AutoUpdateTimeSpinner);
        FirstNameET = (EditText)findViewById(R.id.FirstNameET);
        SecondNameET = (EditText)findViewById(R.id.SecondNameET);
        EmailET = (EditText)findViewById(R.id.EmailET);
        PasswordET = (EditText)findViewById(R.id.PasswordET);
        PasswordRepeatET = (EditText)findViewById(R.id.PasswordRepeatET);

        UserTableDataBaseHelper db = new UserTableDataBaseHelper(getApplicationContext());

        User user = db.getUser(Constants.DEFAULT_USER_APP_ID);

        FirstNameET.setText(user.GetFirstName());
        SecondNameET.setText(user.GetSecondName());
        EmailET.setText(user.GetEmail());
        oldLogin = EmailET.getText().toString();

        ArrayList<String> spinnerArray = new ArrayList<String>();

        spinnerArray.add("1");
        spinnerArray.add("5");
        spinnerArray.add("15");
        spinnerArray.add("30");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);

        int spinnerPosition = spinnerArrayAdapter.getPosition(String.valueOf(user.GetAutoUpdateTime()));

        spinner.setSelection(spinnerPosition);
    }

    public void UpdateFields() throws Exception{
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

        if (!PasswordET.getText().toString().isEmpty()){
            try {
                FormChecker.ValidatePassword(PasswordET);

                try {
                    FormChecker.ValidatePasswordAndPasswordRepeat(PasswordET, PasswordRepeatET);
                }catch (Exception E){
                    Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
                    throw new Exception();
                }

            }catch (Exception E){
                Toast.makeText(context, E.getMessage(), Toast.LENGTH_LONG).show();
                throw new Exception();
            }
        }

        try
        {
            Log.i(Constants.LOG_TAG, "updating DB's started");
            UpdateDB();
        }catch (NullPointerException ex){
            Log.i(Constants.LOG_TAG, "null edittext in registration function");
        }
    }

    private void UpdateDB(){
        Log.i(Constants.LOG_TAG, "updating DB's in process");
        String email = EmailET.getText().toString();
        String fname = FirstNameET.getText().toString();
        String sname = SecondNameET.getText().toString();
        String password = PasswordET.getText().toString();
        String aut = spinner.getSelectedItem().toString();

        Log.i(Constants.LOG_TAG, spinner.getSelectedItem().toString());

        UpdateLocalDB(email, fname, sname, aut);
        UpdateGlobalDB(email, fname, sname, password, aut, oldLogin);
        Log.i(Constants.LOG_TAG, "updating DB's ended");
        Toast.makeText(this.getApplicationContext(), "Данные сохранены", Toast.LENGTH_LONG).show();
    }

    private void UpdateGlobalDB(String email, String fname, String sname, String password, String aut, String _oldLogin){
        Log.i(Constants.LOG_TAG, "updating global DB");
        UpdateUserTask uut = new UpdateUserTask(this);
        uut.execute(email, sname, fname, password, String.valueOf(aut), _oldLogin);
    }

    private void UpdateLocalDB(String email, String fname, String sname, String aut){
        Log.i(Constants.LOG_TAG, "updating local DB");
        UserTableDataBaseHelper db = new UserTableDataBaseHelper(getApplicationContext());

        User user = new User(Constants.DEFAULT_USER_APP_ID, email, sname, fname, Integer.valueOf(aut), Constants.DEFAULT_USER_PERMISSION);

        db.updateUser(user);
    }

    public void SaveButtonClick(View v){
        if (isOnline(getApplicationContext())){
            try {
                UpdateFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Нет интернет соединения", Toast.LENGTH_LONG).show();
        }
    }

    public void BackButtonClick(View v){
        super.onBackPressed();
    }
}