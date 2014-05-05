package com.example.scheduleprojectapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.app.Service;
public class ScheduleBroadcastReceiver extends BroadcastReceiver {
    public User user;
    public ScheduleBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Toast.makeText(context, "Service started", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, ScheduleRefreshService.class);
            context.startService(i);
            context.startService(new Intent(context, ScheduleLoginService.class));
        }
    }

    public final User GetUser(){
        return user;
    }
}
