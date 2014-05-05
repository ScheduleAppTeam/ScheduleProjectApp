package com.example.scheduleprojectapp;

/**
 * Created by kenny on 09.04.14.
 */
public class Schedule_Lesson {

    private Integer _id;
    private Integer weekday;
    private Integer lessonnum;
    private String lessonname;
    private Integer lessontype;
    private Integer venue;
    private String teacher;
    private Integer weektype;

    public void SetId(final Integer id){
        _id = id;
    }

    public void SetWeekDay(final Integer _weekday){
        weekday = _weekday;
    }

    public void SetLessonNum(final Integer _lessonnum){
        lessonnum = _lessonnum;
    }

    public void SetLessonName(final String _lessonname){
        lessonname = _lessonname;
    }

    public void SetLessonType(final Integer _lessontype){
        lessontype = _lessontype;
    }

    public void SetVenue(final Integer _venue){
        venue = _venue;
    }

    public void SetTeacher(final String _teacher){
        teacher = _teacher;
    }

    public void SetWeekType(final Integer _weektype){
        weektype = _weektype;
    }

    public final Integer GetId(){
        return _id;
    }

    public final Integer GetWeekDay(){
        return weekday;
    }

    public final Integer GetLessonNum(){
        return lessonnum;
    }

    public final String GetLessonName(){
        return lessonname;
    }

    public final Integer GetLessonType(){
        return lessontype;
    }

    public final Integer GetVenue(){
        return venue;
    }

    public final String GetTeacher(){
        return teacher;
    }

    public final Integer GetWeekType(){
        return weektype;
    }

    public Schedule_Lesson(Integer id, Integer _weekday, Integer _lessonnum,
                           String _lessonname, Integer _lessontype, Integer _venue,
                           String _teacher, Integer _weektype){
        _id = id;
        weekday = _weekday;
        lessonnum = _lessonnum;
        lessonname = _lessonname;
        lessontype = _lessontype;
        venue = _venue;
        teacher = _teacher;
        weektype = _weektype;
    }

    public Schedule_Lesson(Integer _weekday, Integer _lessonnum,
                           String _lessonname, Integer _lessontype, Integer _venue,
                           String _teacher, Integer _weektype){
        weekday = _weekday;
        lessonnum = _lessonnum;
        lessonname = _lessonname;
        lessontype = _lessontype;
        venue = _venue;
        teacher = _teacher;
        weektype = _weektype;
    }

    public Schedule_Lesson(){
    }
}
