package com.vdx.statussaver.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vdx.statussaver.Activities.VideoShowActivity;
import com.vdx.statussaver.Models.VideoType;
import com.vdx.statussaver.R;
import com.vdx.statussaver.Utils.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;


public class VideoStatusAdpater extends RecyclerView.Adapter<VideoStatusAdpater.ViewHolder> {


    private ArrayList<VideoType> videoTypeArrayList;
    private Context context;
    private onVideoClickListener clickListener = null;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;
    private Helper helper;


    public VideoStatusAdpater(ArrayList<VideoType> list, Context context) {
        this.videoTypeArrayList = list;
        this.context = context;
        selected_items = new SparseBooleanArray();
        helper = new Helper();
    }

    public void setVideoClickListener(onVideoClickListener videoClickListener) {
        this.clickListener = videoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final VideoType videoType = videoTypeArrayList.get(position);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Glide.with(context).load(videoType.getVideoFile())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageView);

        holder.play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoShowActivity.class);
                intent.putExtra("videoPath", videoType.getVideoFile());
                context.startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener == null) return;
                clickListener.onItemClick(v, videoType, position);
            }
        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (clickListener == null) return false;
                clickListener.onItemLongClick(v, videoType, position);
                return true;
            }
        });

        toggleVideoCheckedIcon(holder, position);

    }

    @Override
    public int getItemCount() {
        return videoTypeArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton play_btn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.video_view);
            play_btn = itemView.findViewById(R.id.play_button);
        }
    }

    private void toggleVideoCheckedIcon(ViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.play_btn.setBackground(context.getResources().getDrawable(R.drawable.tick));
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.play_btn.setBackground(context.getResources().getDrawable(R.drawable.play));
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    public void toggleVideoSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
        } else {
            selected_items.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearVideoSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public int getSelectedVideoItemCount() {
        return selected_items.size();
    }

    public ArrayList<Integer> getSelectedItems() {
        ArrayList<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }


    public void removeVideoData(int position) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {

            fileDeleted(videoTypeArrayList.get(position).getVideoFile());
        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));
        }
        videoTypeArrayList.remove(position);
        resetCurrentIndex();

    }

    public void saveVideoData(int position) {
        Log.e("s", String.valueOf(position));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String path = videoTypeArrayList.get(position).getVideoFile();
            helper.copyFile(path, "video");
        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));
        }

    }

//    private void copyFile(String inputPath) {
//
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//
//            //create output directory if it doesn't exist
//            String folder = Environment.getExternalStorageDirectory() + File.separator + "WhatsStatusSaver/";
//            File dir = new File(folder);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//
//            String[] names = inputPath.split("/");
//
//            String s = names[names.length - 1];
//
//            in = new FileInputStream(inputPath);
//            out = new FileOutputStream(folder + s);
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//            in = null;
//
//            // write the output file (You have now copied the file)
//            out.flush();
//            out.close();
//            out = null;
//
//        } catch (Exception fnfe1) {
//            Log.e("tag", Objects.requireNonNull(fnfe1.getMessage()));
//        }
//
//    }


    private boolean fileDeleted(String path) {
        File deleteFile = new File(path);
        if (deleteFile.exists()) {
            if (deleteFile.delete()) {
                return true;
            }
        }
        return false;
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }


    public interface onVideoClickListener {
        void onItemClick(View view, VideoType obj, int pos);

        void onItemLongClick(View view, VideoType obj, int pos);
    }


}
