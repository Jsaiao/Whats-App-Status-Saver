package com.vdx.statussaver.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.vdx.statussaver.Models.ImageType;
import com.vdx.statussaver.R;
import com.vdx.statussaver.Utils.Helper;

import java.io.File;
import java.util.ArrayList;


public class ImageStatusAdpater extends RecyclerView.Adapter<ImageStatusAdpater.ViewHolder> {

    private ArrayList<ImageType> imageTypeArrayList;
    private Context context;

    private OnClickListener onClickListener = null;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;
    private Helper helper;
    private ImageType imageType;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public ImageStatusAdpater(ArrayList<ImageType> list, Context context) {
        this.imageTypeArrayList = list;
        this.context = context;
        helper = new Helper();

        selected_items = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // String s = list.get(position);
        imageType = imageTypeArrayList.get(position);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        holder.image.setImageBitmap(BitmapFactory.decodeFile(imageType.getImage()));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ImageShowActivity.class);
//                intent.putExtra("pos", position);
//                context.startActivity(intent);
//
                if (onClickListener == null) return;
                onClickListener.onItemClick(v, imageType, position);
            }
        });


        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MenuBuilder menuBuilder = new MenuBuilder(context);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.poupup_menu, menuBuilder);
                MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, v);
                optionsMenu.setForceShowIcon(true);
                // Set Item Click Listener
//                menuBuilder.setCallback(new MenuBuilder.Callback() {
//                    @Override
//                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.save: // Handle option1 Click
//                                Toast.makeText(context, "Clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                                return true;
//                            case R.id.share: // Handle option2 Click
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//
//                    @Override
//                    public void onMenuModeChange(MenuBuilder menu) {
//                    }
//                });
//
//                optionsMenu.show();

                if (onClickListener == null) return false;
                onClickListener.onItemLongClick(v, imageType, position);
                return true;
            }
        });

        toggleCheckedIcon(holder, position);

    }

    @Override
    public int getItemCount() {
        return imageTypeArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, select;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            select = itemView.findViewById(R.id.select);
        }
    }

    private void toggleCheckedIcon(ViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.select.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.select.setVisibility(View.GONE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    public ImageType getItem(int position) {
        return imageTypeArrayList.get(position);
    }

    public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
        } else {
            selected_items.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    public ArrayList<Integer> getSelectedItems() {
        ArrayList<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        try {

            fileDeleted(imageTypeArrayList.get(position).getImage());
        } catch (
                Exception e) {
            Log.e("ERROR", String.valueOf(e));
        }

        imageTypeArrayList.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }


    private void fileDeleted(String path) {
        File deleteFile = new File(path);
        if (deleteFile.exists()) {
            if (deleteFile.delete()) ;
            {
                Log.d("DELEETED", "DELETED");
            }
        }
    }

    public void saveData(int position) {
        Log.e("s", String.valueOf(position));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String path = imageTypeArrayList.get(position).getImage();
            helper.copyFile(path, "image");
        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));
        }

    }

    public interface OnClickListener {
        void onItemClick(View view, ImageType obj, int pos);

        void onItemLongClick(View view, ImageType obj, int pos);
    }


}
