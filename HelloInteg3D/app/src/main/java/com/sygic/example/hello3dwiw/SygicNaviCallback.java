package com.sygic.example.hello3dwiw;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.sygic.aura.embedded.IApiCallback;
import com.sygic.sdk.api.events.ApiEvents;

public class SygicNaviCallback implements IApiCallback {

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
                        String str = "Hello Event";
                        Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
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
