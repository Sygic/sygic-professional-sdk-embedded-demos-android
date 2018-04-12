package com.sygic.integdemo3d;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.sygic.aura.embedded.IApiCallback;
import com.sygic.sdk.api.events.ApiEvents;

import java.util.HashMap;

public class SygicNaviCallback implements IApiCallback {

    private HashMap<Integer, String> mEvents = new HashMap<Integer, String>();
    private Activity mActivity;

    public SygicNaviCallback(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onEvent(final int event, final String data) {
        if(event == ApiEvents.EVENT_APP_EXIT) {
            mActivity.finish();
        }

        mActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "Event: " + event, Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    @Override
    public void onServiceConnected() {
        Log.d(MainActivity.LOG_TAG, "service connected");
    }

    @Override
    public void onServiceDisconnected() {
        Log.d(MainActivity.LOG_TAG, "service disconnected");
    }
}
