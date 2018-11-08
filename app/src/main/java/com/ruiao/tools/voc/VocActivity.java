package com.ruiao.tools.voc;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.ui.base.BaseActivity;
import com.ruiao.tools.voc.fragment.SingleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VocActivity extends BaseActivity {
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    public static final String[] tabTitles =
            new String[]{"实时数据(FID)","实时数据(PID)","报警数据(FID)","报警数据(PID)","统计排名"};
    private List<SingleFragment> mFragments = new ArrayList<SingleFragment>();
    private int[] tabIcons = {
            R.drawable.home_selector,
            R.drawable.home_selector,
            R.drawable.home_selector,
            R.drawable.home_selector,
            R.drawable.home_selector,

    };
    @Override
    protected int getContentViewId() {
        return R.layout.activity_voc;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        for(int i = 0; i < tabTitles.length; i++)
        {
            mFragments.add(SingleFragment.createFragment(tabTitles[i]));
        }
        //为ViewPager设置FragmentPagerAdapter
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int position)
            {
                return mFragments.get(position);
            }

            @Override
            public int getCount()
            {
                return mFragments.size();
            }

            /**
             * 为TabLayout中每一个tab设置标题
             */
            @Override
            public CharSequence getPageTitle(int position)
            {
                return tabTitles[position];
            }
        });
        //TabLaout和ViewPager进行关联
        tablayout.setupWithViewPager(viewpager);
        for(int i=0;i<tabTitles.length;i++){
            tablayout.getTabAt(i).setCustomView(getTabView(i));
        }

        //防止tab太多，都拥挤在一起
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(tabTitles[position]);
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(tabIcons[position]);
        return view;
    }
}
