package com.reactnativetruckintegration;

import com.facebook.react.ReactFragmentActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sygic.aura.utils.PermissionsUtils;

public class SygicNaviActivity extends ReactFragmentActivity {

    SygicNaviFragment sygicNaviFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sygic_navi);
        sygicNaviFragment = new SygicNaviFragment();

        if(PermissionsUtils.requestMinimalPermissions(this) == PackageManager.PERMISSION_GRANTED)
            initUI();
    }

    private void initUI() {

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.sygicmap, sygicNaviFragment, null)
                .commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for(int res : grantResults) {
            if(res != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You have to allow all permissions", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }

        // all permissions are granted
        initUI();
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dlg = sygicNaviFragment.onCreateDialog(id);

        if( dlg == null )
            return super.onCreateDialog(id);

        return dlg;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        sygicNaviFragment.onPrepareDialog(id, dialog);
    }

    @Override
    public void onNewIntent(Intent intent) {
        sygicNaviFragment.onNewIntent(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sygicNaviFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return sygicNaviFragment.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return sygicNaviFragment.onKeyUp(keyCode, event);
    }
}
