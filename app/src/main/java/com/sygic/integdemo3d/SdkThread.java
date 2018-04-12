package com.sygic.integdemo3d;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class SdkThread extends Thread {
    private Handler mHandler;
    private ProgressDialog mProgressDialog;

    public SdkThread(Context context, Handler handler) {
        mHandler = handler;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Executing...");
    }

    public void execSdkCommand() {
    }

    public void postExecute() {
    }

    @Override
    public void run() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
            }
        });
        execSdkCommand();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                postExecute();
            }
        });
    }
}
