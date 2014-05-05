package com.example.scheduleprojectapp;

public final class Constants {
    public static final Integer PASSWORD_MIN_LENGTH = 6;
    public static final Integer PASSWORD_MAX_LENGTH = 15;
    public static final Integer NAME_MIN_LENGTH = 2;
    public static final Integer NAME_MAX_LENGTH = 12;
    public static final Integer DAYS_DEPTH = -4;
    public static final Integer DIAPASON_OF_DAYS = 40;
    public static final Integer NUM_OF_LESSONS_IN_DAY = 7;
    public static final String AUTHORIZATION_ACCEPTED = "ACCEPT";
    public static final String INCORRECT_PASSWORD = "INCPAS";
    public static final String INCORRECT_ALL = "INCALL";
    public static final String REGISTRATION_CLEAR = "CLEAR";
    public static final String DUPLICATE_LOGIN = "DUPLICATE";
    public static final String UNBOUND_USER = "UNBOUND";
    public static final String NO_INTERNET_CONNECTION = "NOINTERNETCONN";
    public static final String LOG_TAG = "UppInfo";
    public static final Integer DEFAULT_AUT = 15;
    public static final String ENGLISH_REGX = "^[A-Za-z]{" + (NAME_MIN_LENGTH - 1) + "," + (NAME_MAX_LENGTH - 1) + "}$";
    public static final String RUS_REGX = "^[А-Яа-я]{" + (NAME_MIN_LENGTH - 1) + "," + (NAME_MAX_LENGTH - 1) + "}$";
    public static final String EMAIL_REGX = "^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$";
    public static final String PASSWORD_REGX = "^([a-zA-Z0-9@*#]{" + (PASSWORD_MIN_LENGTH - 1) + "," + (PASSWORD_MAX_LENGTH - 1) + "})$";
    public static final Integer DEFAULT_USER_APP_ID = -1;
    public static final Integer DEFAULT_USER_ID = 0;
    public static final Integer DEFAULT_USER_PERMISSION = 0;

    //DB Helper Consts
    // Database Version
    public static final int DATABASE_VERSION = 2;
    // Database Name
    public static final String USER_DATABASE_NAME = "User.db";
    public static final String LESSONS_DATABASE_NAME = "Lessons.db";
    public static final String VENUES_DATABASE_NAME = "Venues.db";
    public static final String COMMENTS_DATABASE_NAME = "Comments.db";
    // User table name
    public static final String TABLE_USER = "user";
    // ScheduleWD table name
    public static final String TABLE_SCHEDULE_WD = "schedule_week_days";
    // ScheduleWD table name
    public static final String TABLE_VENUE = "venue";
    //Comments tablename
    public static final String TABLE_COMMENTS = "comments";

    public static enum WEEK_COLOR{
        BLUE, RED
    }

    public static enum AUT{
        TIME_1, TIME_5, TIME_10, TIME_15
    }

    private Constants(){
        throw new AssertionError();
    }

    public static final String SCHEDULE_LESSON_SCRIPT_NAME = "get_schedule_from_db.php";
    public static final String VENUES_SCRIPT_NAME = "get_all_venues.php";
    public static final String COMMENTS_SCRIPT_NAME = "get_all_comments.php";
}
