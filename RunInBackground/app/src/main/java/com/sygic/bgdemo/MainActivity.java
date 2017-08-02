package com.sygic.bgdemo;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sygic.aura.utils.PermissionsUtils;

public class MainActivity extends AppCompatActivity {

    private SygicNaviFragment sygicNaviFragment;
    private Button btnReopen, btnDestroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sygicNaviFragment = new SygicNaviFragment();

        if(PermissionsUtils.requestStartupPermissions(this) == PackageManager.PERMISSION_GRANTED)
            initUI();
    }

    private void initUI() {

        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, sygicNaviFragment, null)
                .commitAllowingStateLoss();

        // This button destroys SygicFragment and starts ForegroundSdkService
        btnDestroy = (Button) findViewById(R.id.btnDestroy);
        btnDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().remove(sygicNaviFragment).commit();
                sygicNaviFragment.onDestroy();      // just for testing purposes

                Intent serviceIntent = new Intent(MainActivity.this, ForegroundSdkService.class);
                startService(serviceIntent);

                btnReopen.setEnabled(true);
                btnDestroy.setEnabled(false);
            }
        });

        // just exit
        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Button to restart navigation after SygicFragment has been destroyed. App has to be restarted
        // and may not remember last state (e.g. route computed)
        btnReopen = (Button) findViewById(R.id.btnReopen);
        btnReopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForegroundSdkService.bRunThread = false;
                sygicNaviFragment.shutdownNavigation();

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, sygicNaviFragment, null)
                        .commitAllowingStateLoss();

                btnReopen.setEnabled(false);
                btnDestroy.setEnabled(true);

            }
        });

        btnReopen.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        // Make sure no SDK calls are made anymore
        ForegroundSdkService.bRunThread = false;

        // Use this only when using SygicFragment.setAutoShutdownNavigation(false).
        // Otherwise, navigation is shut down when SygicFragment is destroyed
        sygicNaviFragment.shutdownNavigation();

        super.onDestroy();
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
