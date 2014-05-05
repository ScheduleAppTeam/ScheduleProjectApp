package com.example.scheduleprojectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ScheduleTableDataBaseHelper extends SQLiteOpenHelper {

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_WEEKDAY = "weekday";
    private static final String KEY_LESSONNUM = "lessonnum";
    private static final String KEY_LESSONNAME = "lessonname";
    private static final String KEY_LESSONTYPE = "lessontype";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_TEACHER = "teacher";
    private static final String KEY_WEEKTYPE = "weektype";

    public ScheduleTableDataBaseHelper(Context context) {
        super(context, Constants.LESSONS_DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCHEDULE_WD_TABLE = "CREATE TABLE " + Constants.TABLE_SCHEDULE_WD + "("
                + KEY_ID + " INTEGER,"
                + KEY_WEEKDAY + " INTEGER,"
                + KEY_LESSONNUM + " INTEGER,"
                + KEY_LESSONNAME + " TEXT,"
                + KEY_LESSONTYPE + " INTEGER,"
                + KEY_VENUE + " INTEGER,"
                + KEY_TEACHER + " TEXT,"
                + KEY_WEEKTYPE + " INTEGER,"
                + "UNIQUE ("+ KEY_ID +") ON CONFLICT REPLACE)";
        db.execSQL(CREATE_SCHEDULE_WD_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SCHEDULE_WD);

        // Create tables again
        onCreate(db);
    }

    public void dropDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SCHEDULE_WD);

        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Schedule_Lesson
    void addLesson(Schedule_Lesson lesson) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, lesson.GetId());
        values.put(KEY_WEEKDAY, lesson.GetWeekDay());
        values.put(KEY_LESSONNUM, lesson.GetLessonNum());
        values.put(KEY_LESSONNAME, lesson.GetLessonName());
        values.put(KEY_LESSONTYPE, lesson.GetLessonType());
        values.put(KEY_VENUE, lesson.GetVenue());
        values.put(KEY_TEACHER, lesson.GetTeacher());
        values.put(KEY_WEEKTYPE, lesson.GetWeekType());

        // Inserting Row
        assert db != null;

        //long id = db.insertWithOnConflict(Constants.TABLE_SCHEDULE_WD, BaseColumns._ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        long id = db.replace(Constants.TABLE_SCHEDULE_WD, null, values);
        //Log.i(Constants.LogTag, "Lesson: " + String.valueOf(lesson.GetId()) + "  " + String.valueOf(id));

        db.close(); // Closing database connection
    }

    // Getting single lesson by id
    Schedule_Lesson getLesson(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;

        Cursor cursor = db.query(
                Constants.TABLE_SCHEDULE_WD,
                new String[] { KEY_ID, KEY_WEEKDAY, KEY_LESSONNUM, KEY_LESSONNAME,
                        KEY_LESSONTYPE, KEY_VENUE, KEY_TEACHER, KEY_WEEKTYPE },
                KEY_ID + " = ?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Schedule_Lesson lesson = new Schedule_Lesson();
        lesson.SetId(Integer.parseInt(cursor.getString(0)));
        lesson.SetWeekDay(Integer.parseInt(cursor.getString(1)));
        lesson.SetLessonNum(Integer.parseInt(cursor.getString(2)));
        lesson.SetLessonName(cursor.getString(3));
        lesson.SetLessonType(Integer.parseInt(cursor.getString(4)));
        lesson.SetVenue(Integer.parseInt(cursor.getString(5)));
        lesson.SetTeacher(cursor.getString(6));
        lesson.SetWeekType(Integer.parseInt(cursor.getString(7)));

        return lesson;
    }

    // Getting single lesson by weekday & weektype
    public List<Schedule_Lesson> getLesson(int weekday, int weektype) {
        List<Schedule_Lesson> lessonList= new ArrayList<Schedule_Lesson>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Constants.TABLE_SCHEDULE_WD,
                new String[] { KEY_ID, KEY_WEEKDAY, KEY_LESSONNUM, KEY_LESSONNAME,
                        KEY_LESSONTYPE, KEY_VENUE, KEY_TEACHER, KEY_WEEKTYPE },
                KEY_WEEKDAY + " = ? AND " + KEY_WEEKTYPE + " = ?",
                new String[] { String.valueOf(weekday), String.valueOf(weektype) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Schedule_Lesson lesson = new Schedule_Lesson();
                lesson.SetId(Integer.parseInt(cursor.getString(0)));
                lesson.SetWeekDay(Integer.parseInt(cursor.getString(1)));
                lesson.SetLessonNum(Integer.parseInt(cursor.getString(2)));
                lesson.SetLessonName(cursor.getString(3));
                lesson.SetLessonType(Integer.parseInt(cursor.getString(4)));
                lesson.SetVenue(Integer.parseInt(cursor.getString(5)));
                lesson.SetTeacher(cursor.getString(6));
                lesson.SetWeekType(Integer.parseInt(cursor.getString(7)));

                lessonList.add(lesson);
            } while (cursor.moveToNext());
        }

        // return contact list
        return lessonList;
    }

    public int updateLesson(Schedule_Lesson lesson) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, lesson.GetId());
        values.put(KEY_WEEKDAY, lesson.GetWeekDay());
        values.put(KEY_LESSONNUM, lesson.GetLessonNum());
        values.put(KEY_LESSONNAME, lesson.GetLessonName());
        values.put(KEY_LESSONTYPE, lesson.GetLessonType());
        values.put(KEY_VENUE, lesson.GetVenue());
        values.put(KEY_TEACHER, lesson.GetTeacher());
        values.put(KEY_WEEKTYPE, lesson.GetWeekType());

        // updating row
        assert db != null;
        return db.update(Constants.TABLE_SCHEDULE_WD, values, KEY_ID + " = ?",
                new String[]{String.valueOf(lesson.GetId())});
    }

    // Deleting single lesson by id
    public void deleteLesson(Schedule_Lesson lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(Constants.TABLE_SCHEDULE_WD, KEY_ID + " = ?",
                new String[]{String.valueOf(lesson.GetId())});
        db.close();
    }

    // Deleting single lesson by id
    public void deleteLessonByWD_WT(Schedule_Lesson lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(Constants.TABLE_SCHEDULE_WD, KEY_WEEKDAY + " = ? AND " + KEY_WEEKTYPE + " = ?",
                new String[] { String.valueOf(lesson.GetWeekDay()), String.valueOf(lesson.GetWeekType())});
        db.close();
    }

    // Deleting single venue by id
    public void deleteLessonByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(Constants.TABLE_SCHEDULE_WD, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public int getLessonCount() {
        String countQuery = "SELECT  * FROM " + Constants.TABLE_SCHEDULE_WD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteAllLesson(){
        for (int i = 0; i < getLessonCount(); i++){
            deleteLessonByID(i);
        }
    }
}
