package com.example.scheduleprojectapp;

public class Subject {
    private String lesson;
    private String cabinet;
    private String teacher;
    private Integer posOfLesson;

    public void SetLesson(final String _lesson){
        lesson = _lesson;
    }

    public void SetCabinet(final String _cabinet){
        cabinet = _cabinet;
    }

    public void SetTeacher(final String _teacher){
        teacher = _teacher;
    }

    public void SetPositionOfLesson(final Integer _posOfLesson){
        posOfLesson = _posOfLesson;
    }

    public final String GetLesson(){
        return lesson;
    }

    public final String GetCabinet(){
        return cabinet;
    }

    public final String GetTeacher(){
        return teacher;
    }

    public final Integer GetPositionOfLesson(){
        return posOfLesson;
    }

    public Subject(String _lesson, String _cabinet, String _teacher, Integer _posOfLesson) {
        lesson = _lesson;
        cabinet = _cabinet;
        teacher = _teacher;
        posOfLesson = _posOfLesson;
    }

    public Subject(){
        this("", "", "", 1);
    }
}
