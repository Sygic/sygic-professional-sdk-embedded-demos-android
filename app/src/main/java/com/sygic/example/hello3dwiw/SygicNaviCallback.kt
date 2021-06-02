package com.sygic.example.hello3dwiw

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.sygic.aura.embedded.IApiCallback
import com.sygic.sdk.api.events.ApiEvents

private const val LOG_TAG = "SygicCallback"

class SygicNaviCallback(private val mActivity: Activity) : IApiCallback {

    override fun onEvent(event: Int, data: String?) {
        if (event == ApiEvents.EVENT_APP_EXIT) {
            mActivity.finish()
        }
        mActivity.runOnUiThread {
            Toast.makeText(mActivity, "Hello Event", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onServiceConnected() {
        Log.d(LOG_TAG, "service connected")
    }

    override fun onServiceDisconnected() {
        Log.d(LOG_TAG, "service disconnected")
    }

}