package com.sygic.bgdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sygic.aura.embedded.IApiCallback;
import com.sygic.aura.embedded.SygicFragmentSupportV4;
import com.sygic.sdk.api.events.ApiEvents;

public class SygicNaviFragment extends SygicFragmentSupportV4 {

    @Override
    public void onResume() {
        setCallback(new ApiCallback());

        // Call this, if you don't want navigation to be shut down when fragment is destroyed.
        // Note that it is then  your responsibility to shut down navigation with SygicFragment.shutdownNavigation().
        // If the navigation keeps running in background, behaviour may be unpredictable.
        setAutoShutdownNavigation(false);


        final Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
        final PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Sample title")
                .setContentText("Sample text")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        // This call will make Sygic services run in foreground and therefore make them less likely to be killed by system.
        setServiceNotification(2, notification);

        startNavi();
        super.onResume();
    }

    private class ApiCallback implements IApiCallback {

        @Override
        public void onEvent(int i, String s) {
            Log.d("SygicDemo", "onEvent " + i);
            if(i == ApiEvents.EVENT_APP_EXIT)
                getActivity().finish();
        }

        @Override
        public void onServiceConnected() {
            Log.d("SygicDemo", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected() {
            Log.d("SygicDemo", "onServiceDisconnected");
        }
    }
}
