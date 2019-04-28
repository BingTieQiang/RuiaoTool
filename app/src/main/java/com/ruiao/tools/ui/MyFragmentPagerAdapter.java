package com.ruiao.tools.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruiao on 2018/9/6.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    Fragment currentFragment;
    private List<Fragment> list;
    public MyFragmentPagerAdapter(FragmentManager fm , ArrayList<Fragment> list) {
        super(fm);
        this.list = list;

    }
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
