package com.vdx.statussaver.Activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.vdx.statussaver.Adapters.ImagePageAdapter;
import com.vdx.statussaver.R;
import com.vdx.statussaver.Utils.Helper;
import com.vdx.statussaver.Utils.Transformations.HingeTransformation;

import static com.vdx.statussaver.Utils.TotalList.imgList;

public class ImageShowActivity extends AppCompatActivity {

    private FloatingActionMenu leftCenterMenu;
    private Helper helper;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        initView();
    }


    private void initView() {

        ViewPager viewPager = findViewById(R.id.view_pager);
        helper = new Helper();
        pos = getIntent().getIntExtra("pos", 0);
        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_action_new_light));

        // Set up the large red butt on the center right side
        // With custom butt and content sizes and margins

        final ImageView fabIconStar = new ImageView(this);
        fabIconStar.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_action_new));

        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconStar.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .setLayoutParams(starParams)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
        lCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);

        // Set custom layout params

        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);

        ImageView share = new ImageView(this);
        final ImageView save = new ImageView(this);
        final ImageView setAs = new ImageView(this);
        final ImageView rate = new ImageView(this);

        share.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_share));
        save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white));
        setAs.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_setas));
        rate.setImageDrawable(getResources().getDrawable(R.drawable.ic_rate));

        // Build another menu with custom options
        leftCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(lCSubBuilder.setContentView(share, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(save, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(setAs, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(rate, blueContentParams).build())
                .setRadius(redActionMenuRadius)
                .attachTo(leftCenterButton)
                .build();


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Download your friend's Whats App Status and Post them as your status for free. \n" +
                        "अपने दोस्तों के व्हाट्स ऐप स्टेटस को डाउनलोड करें और उन्हें अपने स्टेटस के रूप में मुफ्त में पोस्ट करें|\n" + "https://play.google.com/store/apps/details?id=com.vdx.statussaver");

                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whats App Not Found!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.RateUs(ImageShowActivity.this, "com.vdx.statussaver");

            }
        });

        leftCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                fabIconStar.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconStar, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                fabIconStar.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconStar, pvhR);
                animation.start();

            }
        });

        ImagePageAdapter myPager = new ImagePageAdapter(ImageShowActivity.this, imgList);
        viewPager.setAdapter(myPager);

        viewPager.setCurrentItem(pos);
        HingeTransformation hingeTransformation = new HingeTransformation();

        viewPager.setPageTransformer(true, hingeTransformation);


        setAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgPath = imgList.get(pos).getImage();
                helper.copyFile(imgPath, "image");
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (leftCenterMenu.isOpen()) {
                    leftCenterMenu.close(true);
                }
            }

            @Override
            public void onPageSelected(final int position) {


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String imgPath = imgList.get(position).getImage();
                        helper.copyFile(imgPath, "image");
                        Toast.makeText(ImageShowActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                    }
                });

                setAs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.shareWhatsImage(position, ImageShowActivity.this);
                    }

                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

    }

}
