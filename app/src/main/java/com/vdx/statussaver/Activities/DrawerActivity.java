package com.vdx.statussaver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.vdx.statussaver.R;

import java.util.Objects;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    public ActionBar actionBar;
    public static Toolbar toolbar;

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(int layoutResId) {
        mDrawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        FrameLayout activityContainer = mDrawer.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(mDrawer);
        initView();

    }

    private void initView() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.addDrawerListener(mDrawerToggle);
        navigationView = mDrawer.findViewById(R.id.navi_view);
        // actionBar = getSupportActionBar();
//
        toolbar = mDrawer.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(true);
//            actionBar.setDisplayUseLogoEnabled(false);
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setTitle(R.string.app_name);
//            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_menu_black_24dp));
//        }


        setUpNavView();

    }


    protected void setUpNavView() {
        navigationView.setNavigationItemSelectedListener(this);
        if (useDrawerToggle()) { // use the hamburger menu
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, 0, 0);

            mDrawer.addDrawerListener(mDrawerToggle);

            toolbar.post(new Runnable() {
                @Override
                public void run() {
                    Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_black_24dp, null);
                    toolbar.setNavigationIcon(d);
                }
            });
            mDrawerToggle.syncState();

        } else if (useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Whats Status Saver");

        }

    }

    protected boolean useDrawerToggle() {
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);
        return onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return useMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.whatsapp:
                item.setCheckable(false);
                Toast.makeText(this, "Status", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.business:
                startActivity(new Intent(this, BusinessActivity.class));
                item.setCheckable(false);
                return true;
            case R.id.more:
                item.setCheckable(false);
//                startActivity(new Intent(this, BusinessActivity.class));
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                item.setCheckable(false);
//                startActivity(new Intent(this, BusinessActivity.class));
                Toast.makeText(this, "In Development", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.actionAbout:
                item.setCheckable(false);
                startActivity(new Intent(this, AboutUsActivity.class));
            default:
                return true;
        }

    }


    protected boolean useToolbar() {
        return true;
    }

    protected boolean useHomeButton() {
        return true;
    }

    protected boolean useMenu() {
        return true;
    }

}

