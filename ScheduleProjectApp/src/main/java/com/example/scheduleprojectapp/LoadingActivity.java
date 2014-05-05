package com.example.scheduleprojectapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.example.scheduleprojectapp.util.SystemUiHider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
public class LoadingActivity extends Activity {
    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 0;

    private static final boolean TOGGLE_ON_CLICK = true;

    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        final View contentView = findViewById(R.id.LoadScreenGreeting);

        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        UserTableDataBaseHelper db = new UserTableDataBaseHelper(getApplicationContext());
        //Log.i(Constants.LogTag, String.valueOf(db.getCount()));
        boolean result = /*(db.getUserCount() > 0) ? true : false**/false;
        if (result){//если есть пользователь в локальной БД, то запускаем расписание

            Intent i = new Intent(getApplicationContext(), ScheduleLoginService.class);
            startService(i);

            Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
            startActivity(intent);

            finish();
        }
        else {//если есть пользователь в локальной БД, то запускаем форму авторизации
            CreateScheduleLocalDB();
            CreateVenueLocalDB();
            CreateCommentsLocalDB();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Создём локальную БД комментариев
    public void CreateCommentsLocalDB(){
        LoadingGlobalDBtoLocalDBTask dbl = new LoadingGlobalDBtoLocalDBTask();
        dbl.execute(Constants.COMMENTS_SCRIPT_NAME);

        String commentsRawDate = null;

        try {
            commentsRawDate = dbl.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<Comment> commentList = NecessaryFunctions.ParseComments(commentsRawDate);

        CommentTableDatabaseHelper dbct = new CommentTableDatabaseHelper(this);

        dbct.dropDB();
        Log.i(Constants.LOG_TAG, commentsRawDate);

        for (Comment item : commentList){
            dbct.addComment(item);
        }
    }

    //Создём локальную БД расписания
    public void CreateScheduleLocalDB(){

        LoadingGlobalDBtoLocalDBTask dbl = new LoadingGlobalDBtoLocalDBTask();
        dbl.execute(Constants.SCHEDULE_LESSON_SCRIPT_NAME);

        String lessonsRawDate = null;

        try {
            lessonsRawDate = dbl.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<Schedule_Lesson> lessonList = NecessaryFunctions.ParseLessons(lessonsRawDate);

        //Log.i(Constants.LogTag, lessonsRawDate);
        ScheduleTableDataBaseHelper dbst = new ScheduleTableDataBaseHelper(this);

        dbst.dropDB();

        for (Schedule_Lesson item : lessonList){
            dbst.addLesson(item);
        }

        dbst.close();
    }

    //Создём локальную БД аудиторий
    public void CreateVenueLocalDB(){

        LoadingGlobalDBtoLocalDBTask dbv = new LoadingGlobalDBtoLocalDBTask();
        dbv.execute(Constants.VENUES_SCRIPT_NAME);

        String venuesRawDate = null;
        try {
            venuesRawDate = dbv.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<Venue> venueList = NecessaryFunctions.ParseVenues(venuesRawDate);
        VenueTableDataBaseHelper dbvt = new VenueTableDataBaseHelper(this);

        dbvt.dropDB();

        for (Venue item : venueList){
            dbvt.addVenue(item);
        }

        dbvt.close();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }


    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}