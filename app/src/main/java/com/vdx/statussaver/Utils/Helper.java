package com.vdx.statussaver.Utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import static com.vdx.statussaver.Utils.TotalList.imgList;

public class Helper {
    public Helper() {

    }

    public void RateUs(Context c, String name) {
        try {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + name)));
        } catch (ActivityNotFoundException e) {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + name)));
        }

    }

    public void whats_share(Context context) {
        try {
            final ComponentName name = new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker");
            Intent oShareIntent = new Intent();
            oShareIntent.setComponent(name);
            oShareIntent.setType("text/plain");
            oShareIntent.putExtra(Intent.EXTRA_TEXT, "Find the Super and best 4k & Amoled Wallpapers: " + "https://play.google.com/store/apps/details?id=com.vdx.super4kwallpapers");
            context.startActivity(oShareIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Whats App Not Found!", Toast.LENGTH_SHORT).show();
            Log.d("Error", String.valueOf(e));
        }

    }

    public void copyFile(String inputPath, String type) {

        InputStream in = null;
        OutputStream out = null;
        String folder;
        try {

            //create output directory if it doesn't exist
            if (type.equals("image")) {
                folder = Environment.getExternalStorageDirectory() + File.separator + "WhatsStatusSaver/Image";

            } else {
                folder = Environment.getExternalStorageDirectory() + File.separator + "WhatsStatusSaver/Video";
            }
            File dir = new File(folder);
            dir.setWritable(true);

            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    Log.d("Directory", "Directory Created");
                }
            }


            String[] names = inputPath.split("/");

            String s = names[names.length - 1];
            folder = dir.getAbsolutePath() + "/" + s;

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(folder);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (Exception fnfe1) {
            Log.e("tag", Objects.requireNonNull(fnfe1.getMessage()));
        }

    }

    public void shareWhatsImage(int position, Context context) {
        Uri imgUri = Uri.parse(imgList.get(position).getImage());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.setType("image/jpeg");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }


    public void shareWhatsVideo(String vpath, Context context) {
        Uri imgUri = Uri.parse(vpath);
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.setType("video/mp4");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Whats App not found", Toast.LENGTH_SHORT).show();
        }
    }

}
