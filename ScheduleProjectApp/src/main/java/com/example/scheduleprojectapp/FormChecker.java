package com.example.scheduleprojectapp;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class FormChecker {
    public static Boolean SatisfyRegularExpression(final String text, final String regx){
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

    public static Boolean ValidateFName(final EditText fNameET) throws Exception {
        final String fNameStr;
        fNameStr = fNameET.getText().toString();

        Boolean result = ((SatisfyRegularExpression(fNameStr, Constants.RUS_REGX) || SatisfyRegularExpression(fNameStr, Constants.ENGLISH_REGX)));

        try{
            if (!result){
                throw new Exception();
            }
        }catch (Exception E){
            throw new Exception("Введите корректное имя");
        }

        return result;
    }

    public static Boolean ValidateSName(final EditText sNameET) throws Exception {
        final String sNameStr;
        sNameStr = sNameET.getText().toString();

        Boolean result = ((SatisfyRegularExpression(sNameStr, Constants.RUS_REGX) || SatisfyRegularExpression(sNameStr, Constants.ENGLISH_REGX)));

        try{
            if (!result){
                throw new Exception();
            }
        }catch (Exception E){
            throw new Exception("Введите корректную фамилию");
        }

        return result;
    }

    public static Boolean FNameAndSNameIsUniform(final EditText fNameET, final EditText sNameET) throws Exception {
        final String fNameStr;
        fNameStr = fNameET.getText().toString();
        final String sNameStr;
        sNameStr = sNameET.getText().toString();

        Boolean result = ((SatisfyRegularExpression(fNameStr, Constants.RUS_REGX) && SatisfyRegularExpression(sNameStr, Constants.RUS_REGX)) ||
                (SatisfyRegularExpression(fNameStr, Constants.ENGLISH_REGX) && SatisfyRegularExpression(sNameStr, Constants.ENGLISH_REGX)));

        try{
            if (!result){
                throw new Exception();
            }
        }catch (Exception E){
            throw new Exception("Фамилия и имя должны быть одного языка");
        }

        return result;
    }

    public static Boolean ValidateEmail(final EditText textET) throws Exception{
        final String textStr;
        textStr = textET.getText().toString();

        Boolean result = (SatisfyRegularExpression(textStr, Constants.EMAIL_REGX));

        try{
            if (!result){
                throw new Exception();
            }
        }catch (Exception E){
            throw new Exception("Введите корректный адрес почты");
        }

        return result;
    }

    public static Boolean ValidatePassword(final EditText textET) throws Exception{
        final String textStr;
        textStr = textET.getText().toString();

        Boolean result = (SatisfyRegularExpression(textStr, Constants.PASSWORD_REGX));

        try{
            if (!result){
                throw new Exception();
            }
        }catch (Exception E){
            throw new Exception("Пароль содержит недопустимые знаки");
        }

        return result;
    }

    public static Boolean ValidatePasswordAndPasswordRepeat(final EditText passwordOrigET, final EditText passwordRepeatET) throws Exception{
        final String passwordOrigStr;
        passwordOrigStr = passwordOrigET.getText().toString();
        final String passwordRepeatStr;
        passwordRepeatStr = passwordRepeatET.getText().toString();

        Boolean result = (passwordOrigStr.equals(passwordRepeatStr));

        try{
            if (!result){
                throw new Exception();
            }
        }catch (Exception E){
            throw new Exception("Пароли не совпадают");
        }
        return result;
    }

    public static Boolean isFilled(final EditText textET, String message) throws Exception{
        Boolean result = (textET.getText().toString().isEmpty());
        try{
            if (result){
                throw new Exception();
            }
        }catch (Exception E){
            throw new Exception(message);
        }
        return result;
    }
}
