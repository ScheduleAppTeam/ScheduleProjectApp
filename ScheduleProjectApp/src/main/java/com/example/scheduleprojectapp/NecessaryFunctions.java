package com.example.scheduleprojectapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;

public class NecessaryFunctions {

    public static int MakeNormalWeekDayNum(int dayInWeek){
        if (dayInWeek == 1){
            return 7;
        }else{
            return (dayInWeek - 1);
        }
    }

    public static String GetDay(Integer numOfDay) throws Exception{
        String[] day = {"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

        return day[numOfDay];
    }

    public static String GetMonth(Integer numOfMonth) throws Exception{
        String[] day = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

        return day[numOfMonth];
    }

    public static Constants.WEEK_COLOR GetColorOfTheWeek(Integer weekNum){
        if (weekNum % 2 == 0){
            return Constants.WEEK_COLOR.BLUE;
        }else{
            return Constants.WEEK_COLOR.RED;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static ArrayList<Schedule_Lesson> ParseLessons(String str) {
        ArrayList<Schedule_Lesson> result = new ArrayList<Schedule_Lesson>();
        String[] strs = str.split("\\|");
        for(String lesson : strs) {
            String[] data = lesson.split("\\_");
            if(data.length == 8) {
                Schedule_Lesson newLesson = new Schedule_Lesson(Integer.valueOf(data[0]), Integer.valueOf(data[1]),
                        Integer.valueOf(data[2]), data[3], Integer.valueOf(data[4]), Integer.valueOf(data[5]),
                        data[6], Integer.valueOf(data[7]));
                result.add(newLesson);
            }
        }
        return result;
    }

    public static ArrayList<Venue> ParseVenues(String str) {
        ArrayList<Venue> result = new ArrayList<Venue>();
        String[] strs = str.split("\\|");
        for(String lesson : strs) {
            String[] data = lesson.split("\\_");
            if(data.length == 3) {
                Venue venue = new Venue(Integer.valueOf(data[0]), Integer.valueOf(data[1]),Integer.valueOf(data[2]));
                result.add(venue);
            }
        }
        return result;
    }

    public static ArrayList<Comment> ParseComments(String str) {
        ArrayList<Comment> result = new ArrayList<Comment>();
        String[] strs = str.split("\\|");
        for(String comment : strs) {
            String[] data = comment.split("\\_");
            if(data.length == 7) {
                Log.i(Constants.LOG_TAG, comment);
                Comment comments = new Comment(
                        Integer.valueOf(data[1]),
                        data[2],
                        data[3],
                        Integer.valueOf(data[4]),
                        Long.valueOf(data[5]),
                        data[6]);
                result.add(comments);
            }
        }
        return result;
    }

    public static String GetLessonType(int num){
        String[] type = {"Лекция", "Практика", "Лабораторная", "Мероприятие"};

        return type[num];
    }

    public static String MakeLessonInfo (int type, Venue venue){
        return GetLessonType(type) + " " + venue.GetAudience() + " (" + venue.GetHousing() + ")";
    }
}
