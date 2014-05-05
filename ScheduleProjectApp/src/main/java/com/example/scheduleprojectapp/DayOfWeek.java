package com.example.scheduleprojectapp;

import java.io.Serializable;

public class DayOfWeek implements Serializable{
    public Integer dayInYear;
    public Integer numOfDayInWeek;
    public Integer dayInMonth;
    public Integer month;
    public Schedule_Lesson[] subjects;
    public Constants.WEEK_COLOR color;

    public void SetDayInYear(final Integer _dayInYear){
        dayInYear = _dayInYear;
    }

    public void SetNumOfDayInWeek(final Integer _numOfDayInWeek){
        numOfDayInWeek = _numOfDayInWeek;
    }

    public void SetDayInMonth(final Integer _dayInMonth){
        dayInMonth = _dayInMonth;
    }

    public void SetMonth(final Integer _month){
        month = _month;
    }

    public void SetSubjects(final Schedule_Lesson[] _subjects){
        subjects = _subjects;
    }

    public void SetColor(final Constants.WEEK_COLOR _color){
        color = _color;
    }

    public final Integer GetDayInYear(){
        return dayInYear;
    }

    public final Integer GetNumOfDayInWeek(){
        return numOfDayInWeek;
    }

    public final Integer GetDayInMonth(){
        return dayInMonth;
    }

    public final Integer GetMonth(){
        return month;
    }

    public final Schedule_Lesson[] GetSubjects(){
        return subjects;
    }

    public final Constants.WEEK_COLOR GetColorOfDay(){
        return color;
    }

    public DayOfWeek(Integer _dayInYear, Integer _numOfDayInWeek, Integer _dayInMonth, Integer _month, Schedule_Lesson[] _subjects, Constants.WEEK_COLOR _color) {
        dayInYear = _dayInYear;
        numOfDayInWeek = _numOfDayInWeek;
        dayInMonth = _dayInMonth;
        month = _month;
        subjects = _subjects;
        color = _color;
    }

    public final DayOfWeek GetDay(){
        return this;
    }
}
