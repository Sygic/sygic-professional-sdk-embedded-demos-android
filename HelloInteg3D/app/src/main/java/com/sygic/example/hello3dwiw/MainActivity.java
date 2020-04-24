package com.sygic.example.hello3dwiw;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sygic.sdk.api.ApiNavigation;

import com.sygic.aura.utils.PermissionsUtils;
import com.sygic.sdk.api.exception.GeneralException;

public class MainActivity extends AppCompatActivity
{
    public static final String LOG_TAG = "hello3dwiw";

    private SygicNaviFragment fgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(PermissionsUtils.requestAllPermissions(this) == PackageManager.PERMISSION_GRANTED)
        {
            initUI();
        }
    }

    private void initUI() {
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
        initUI();
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
