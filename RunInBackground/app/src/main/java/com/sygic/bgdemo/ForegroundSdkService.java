package com.sygic.bgdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sygic.sdk.api.ApiNavigation;
import com.sygic.sdk.api.exception.GeneralException;


public class ForegroundSdkService extends Service {

    public static volatile boolean bRunThread;
    private Thread worker = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        doStuff();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Sample title")
                .setContentText("Sample text")
                .setContentIntent(pendingIntent)
                .build();

        // we advise to use this service as foreground
        startForeground(1, notification);

        return super.onStartCommand(intent, flags, startId);
    }

    private void doStuff() {
        if(worker != null)
            return;

        bRunThread = true;

        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while(bRunThread) {
                    try {
                        Log.d("SygicDemo", "Route status = " + ApiNavigation.getRouteStatus(0));
                    } catch (GeneralException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf();
            }
        });

        worker.start();
    }

    @Override
    public void onDestroy() {
        // stop worker thread
        bRunThread = false;
        if(worker != null) {
            try {
                worker.join();
                worker = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        super.onDestroy();
    }
}
