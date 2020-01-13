package com.vdx.statussaver.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.vdx.statussaver.Fragments.ImageBusinessFragment;
import com.vdx.statussaver.Fragments.ImageFragment;
import com.vdx.statussaver.Fragments.VideoBusinessFragment;
import com.vdx.statussaver.Fragments.VideoFragment;
import com.vdx.statussaver.R;

public class BusinessActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Business Status");
        }
        initView();
    }

    private void initView() {
        ViewPager viewPager = findViewById(R.id.viewpager_business);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 1:
                        return VideoBusinessFragment.newInstance();
                    case 0:
                    default:
                        return ImageBusinessFragment.newInstance();
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


        final SmartTabLayout viewPagerTab = findViewById(R.id.view_pager_tab_business);
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
    protected boolean useHomeButton() {
        return true;
    }

    @Override
    protected boolean useMenu() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.whatsapp:
                onBackPressed();
                return true;
            case R.id.business:
                item.setCheckable(false);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
