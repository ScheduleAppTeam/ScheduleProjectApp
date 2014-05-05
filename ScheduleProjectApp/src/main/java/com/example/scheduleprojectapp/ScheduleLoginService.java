package com.example.scheduleprojectapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ScheduleLoginService extends Service {

    public ScheduleLoginService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UserTableDataBaseHelper db = new UserTableDataBaseHelper(getApplicationContext());
        //user = db.getUser(Constants.DefaultAppUserID);

        play();

        return(START_NOT_STICKY);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    private void play() {
        Notification note=new Notification(R.drawable.stat_notify_chat,
                "Can you hear the music?",
                System.currentTimeMillis());
        Intent i=new Intent(this, ScheduleActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi=PendingIntent.getActivity(this, 0,
                i, 0);

        note.setLatestEventInfo(this, "Fake Player",
                "Now Playing: \"Ummmm, Nothing\"",
                pi);
        note.flags|=Notification.FLAG_NO_CLEAR;

        startForeground(1337, note);
    }
}
