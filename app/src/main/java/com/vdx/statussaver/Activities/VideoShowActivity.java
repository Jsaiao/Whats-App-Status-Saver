package com.vdx.statussaver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.vdx.statussaver.R;
import com.vdx.statussaver.Utils.Helper;

public class VideoShowActivity extends AppCompatActivity {

    private VideoView videoView;
    private FloatingActionMenu leftCenterMenu;
    private Helper helper;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);

        initView();
    }

    private void initView() {


        videoPath = getIntent().getStringExtra("videoPath");

        videoView = findViewById(R.id.videoshow);
        helper = new Helper();

        final MediaController mediaController = new MediaController(this);
        mediaController.setAlwaysDrawnWithCacheEnabled(true);
        mediaController.setAnchorView(videoView);
        mediaController.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaController.show(0);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaController.show(0);
                videoView.start();
            }
        });

        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.setVideoPath(videoPath);
        try {

            videoView.start();
        } catch (Exception e) {
            Log.e("VideoShowAct", String.valueOf(e));
        }
        videoView.requestFocus();

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
                helper.RateUs(VideoShowActivity.this, "com.vdx.statussaver");

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


        setAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.shareWhatsVideo(videoPath, VideoShowActivity.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.copyFile(videoPath, "video");
                Toast.makeText(VideoShowActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }
}
