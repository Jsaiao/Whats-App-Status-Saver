package com.vdx.statussaver.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.vdx.statussaver.Models.ImageType;
import com.vdx.statussaver.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ImageType> imageTypeList;


    public ImagePageAdapter(Context context, ArrayList<ImageType> sheet1List) {
        this.context = context;
        this.imageTypeList = sheet1List;
    }

    @Override
    public int getCount() {
        return imageTypeList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.pager_item, null);
        PhotoView imageView = view.findViewById(R.id.wallpaper_image);
        final ImageType list = imageTypeList.get(position);
        String img = list.getImage();

        imageView.setImageBitmap(BitmapFactory.decodeFile(img));
        container.addView(view);
        return view;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

}
