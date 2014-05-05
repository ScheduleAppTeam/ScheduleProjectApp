package com.example.scheduleprojectapp;

import android.widget.TextView;

public class ScheduleLesson {
    private TextView lesson;
    private TextView cabinet;
    private TextView teacher;

    public void SetLesson(final TextView _lesson){
        lesson = _lesson;
    }

    public void SetCabinet(final TextView _cabinet){
        cabinet = _cabinet;
    }

    public void SetTeacher(final TextView _teacher){
        teacher = _teacher;
    }

    public final TextView GetLesson(){
        return lesson;
    }

    public final TextView GetCabinet(){
        return cabinet;
    }

    public final TextView GetTeacher(){
        return teacher;
    }

    public ScheduleLesson(TextView _lesson, TextView _cabinet, TextView _teacher) {
        lesson = _lesson;
        cabinet = _cabinet;
        teacher = _teacher;
    }

    public ScheduleLesson() {
        this(null, null, null);
    }
}