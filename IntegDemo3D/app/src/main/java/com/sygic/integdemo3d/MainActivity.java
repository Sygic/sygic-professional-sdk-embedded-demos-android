package com.sygic.integdemo3d;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.sygic.aura.embedded.SygicFragmentSupportV4;
import com.sygic.aura.utils.PermissionsUtils;
import com.sygic.integdemo3d.fragment.*;

public class MainActivity extends AppCompatActivity {

    public static final int API_CALL_TIMEOUT    = 5000;
    public static final String LOG_TAG = "ItegDemo3D";

    private Fragment mFragment[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragment = new Fragment[4];

        if(PermissionsUtils.requestStartupPermissions(this) == PackageManager.PERMISSION_GRANTED)
            initUI();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initUI() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    mFragment[0] = new SygicNaviFragment();
                    break;
                case 1:
                    mFragment[1] = new LocationFragment();
                    break;
                case 2:
                    mFragment[2] = new ItineraryFragment();
                    break;
                case 3:
                    mFragment[3] = new PoiFragment();
                    break;
            }
            return mFragment[position];
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Navi";
                case 1: return "Loc";
                case 2: return "Itin";
                case 3: return "Poi";
            }
            return null;
        }
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

        Dialog dlg = ((SygicFragmentSupportV4)mFragment[0]).onCreateDialog(id);

        if( dlg == null )
            return super.onCreateDialog(id);

        return dlg;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        ((SygicFragmentSupportV4)mFragment[0]).onPrepareDialog(id, dialog);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        ((SygicFragmentSupportV4)mFragment[0]).onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment[0].onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return ((SygicFragmentSupportV4)mFragment[0]).onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return ((SygicFragmentSupportV4)mFragment[0]).onKeyUp(keyCode, event);
    }
}
