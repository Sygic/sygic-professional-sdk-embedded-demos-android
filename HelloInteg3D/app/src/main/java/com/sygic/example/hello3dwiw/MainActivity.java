package com.sygic.example.hello3dwiw;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sygic.aura.ResourceManager;
import com.sygic.aura.utils.PermissionsUtils;
import com.sygic.sdk.api.ApiNavigation;
import com.sygic.sdk.api.exception.GeneralException;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "hello3dwiw";

    private SygicNaviFragment fgm;
    private boolean uiInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(PermissionsUtils.requestStartupPermissions(this) == PackageManager.PERMISSION_GRANTED)
        {
            checkSygicResources();
        }
    }

    private void checkSygicResources() {
        ResourceManager resourceManager = new ResourceManager(this, null);
        if(resourceManager.shouldUpdateResources()) {
            Toast.makeText(this, "Please wait while Sygic resources are being updated", Toast.LENGTH_LONG).show();
            resourceManager.updateResources(new ResourceManager.OnResultListener() {
                @Override
                public void onError(int errorCode, @NotNull String message) {
                    Toast.makeText(MainActivity.this, "Failed to update resources: " + message, Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onSuccess() {
                    initUI();
                }
            });
        }
        else {
            initUI();
        }
    }

    private void initUI() {

        if(uiInitialized)
            return;

        uiInitialized = true;

        setContentView(R.layout.activity_main);
        fgm = new SygicNaviFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.sygicmap, fgm).commitAllowingStateLoss();

        final EditText address = (EditText)findViewById(R.id.edit1);

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    public void run() {
                        try {
                            ApiNavigation.navigateToAddress(address.getText().toString(), false, 0, 5000);
                        } catch (GeneralException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

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
        checkSygicResources();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dlg = fgm.onCreateDialog(id);
        if (dlg == null)
            return super.onCreateDialog(id);
        return dlg;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        fgm.onPrepareDialog(id, dialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fgm.onActivityResult(requestCode, resultCode, data);
    }

}
