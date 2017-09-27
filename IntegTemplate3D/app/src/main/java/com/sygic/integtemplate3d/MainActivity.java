package com.sygic.integtemplate3d;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sygic.aura.utils.PermissionsUtils;

public class MainActivity extends AppCompatActivity {

    SygicNaviFragment sygicNaviFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sygicNaviFragment = new SygicNaviFragment();

        if(PermissionsUtils.requestStartupPermissions(this) == PackageManager.PERMISSION_GRANTED)
            initUI();
    }

    private void initUI() {

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, sygicNaviFragment, null)
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
    protected void onNewIntent(Intent intent) {
        sygicNaviFragment.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
