package com.vdx.statussaver.Activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.vdx.statussaver.Fragments.ImageFragment;
import com.vdx.statussaver.Fragments.VideoFragment;
import com.vdx.statussaver.R;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends DrawerActivity implements EasyPermissions.PermissionCallbacks {

    private static final int WRITE_REQUEST_CODE = 300;
    private static final int READ_REQUEST_CODE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();

        initView();

    }

    private void initView() {
        ViewPager viewPager = findViewById(R.id.viewpager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 1:
                        return VideoFragment.newInstance();
                    case 0:
                    default:
                        return ImageFragment.newInstance();
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 1:
                        return "Video";
                    case 0:
                    default:
                        return "Image";
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });


        final SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("Granted", "Permission has been granted");
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        Log.d("Denied", "Permission has been denied");
    }


    private void getPermission() {
        if (EasyPermissions.hasPermissions(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && EasyPermissions.hasPermissions(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Log.d("Permission", "Granted");
        } else {
            EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
}
