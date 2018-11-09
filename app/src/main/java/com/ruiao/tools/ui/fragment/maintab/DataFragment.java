package com.ruiao.tools.ui.fragment.maintab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ruiao.tools.R;
import com.ruiao.tools.ui.adapter.CommonViewPagerAdapter;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.ui.fragment.other.DataMainFragment;
import com.ruiao.tools.widget.smarttablelayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gs on 2017/9/15.
 */

public class DataFragment extends BaseFragment {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.viewpagertab)
    SmartTabLayout smartTabLayout;
    @Override
    protected int getContentViewID() {
        return R.layout.fragment_data;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        CommonViewPagerAdapter adapter = new CommonViewPagerAdapter(getChildFragmentManager());
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(DataMainFragment.newInstance("0"));
        fragments.add(DataMainFragment.newInstance("1"));
        fragments.add(DataMainFragment.newInstance("2"));
        fragments.add(DataMainFragment.newInstance("3"));
        fragments.add(DataMainFragment.newInstance("4"));
        fragments.add(DataMainFragment.newInstance("5"));
        fragments.add(DataMainFragment.newInstance("6"));
        adapter.setData(getResources().getStringArray(R.array.main_tab_data),fragments);
        viewPager.setOffscreenPageLimit(fragments.size()-1);
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

    }

    @Override
    public void getmsg() {

    }
}
