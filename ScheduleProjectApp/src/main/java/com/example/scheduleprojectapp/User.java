package com.example.scheduleprojectapp;

public class User {
    private Integer _id;
    private String firstName;
    private String secondName;
    private String email;
    private Integer autoUpdateTime;
    private Integer permission;

    public void SetFirstName(final String _firstName){
        firstName = _firstName;
    }

    public void SetSecondName(final String _secondName){
        secondName = _secondName;
    }

    public void SetEmail(final String _email){
        email = _email;
    }

    public void SetAutoUpdateTime(final Integer _autoUpdateTime){
        autoUpdateTime = _autoUpdateTime;
    }

    public void SetPermission(final Integer _permission){
        permission = _permission;
    }

    public final Integer GetId(){
        return _id;
    }

    public final String GetFirstName(){
        return firstName;
    }

    public final String GetSecondName(){
        return secondName;
    }

    public final String GetEmail(){
        return email;
    }

    public final Integer GetAutoUpdateTime(){
        return autoUpdateTime;
    }

    public final Integer GetPermission(){
        return permission;
    }

    public User(Integer id, String _email, String _secondName, String _firstName, Integer _autoUpdateTime, Integer _permission){
        _id = id;
        email = _email;
        secondName = _secondName;
        firstName = _firstName;
        autoUpdateTime = _autoUpdateTime;
        permission = _permission;
    }
}
