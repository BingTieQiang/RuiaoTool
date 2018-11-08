package com.ruiao.tools.ui.rollpagerAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.ruiao.tools.R;

/**
 * Created by ruiao on 2018/4/9.
 */

public class RollPagerAdapter extends StaticPagerAdapter {
    public RollPagerAdapter() {

    }

    private int[] image = {R.mipmap.img1, R.mipmap.img1, R.mipmap.img1, R.mipmap.img1};
    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(image[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;

    }

    @Override
    public int getCount() {

        return image.length;
    }
}
