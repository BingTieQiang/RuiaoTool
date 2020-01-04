package com.ruiao.tools.youyan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.aqi.AqiMap;
import com.ruiao.tools.aqi.AqiNewData;
import com.ruiao.tools.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 油烟在线 主页
 *
 * 进入刷新获取最新 数据
 */
public class YouyanActivity extends AppCompatActivity {
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.main_tab1)
    View mTab1;
    @BindView(R.id.main_tab2)
    View mTab2;

    private YouyanMap map;
    private YouyanNewData newdata;
    private View[] mTabs;
    protected Fragment currentFragment;
    private int currentTabIndex;
    private final int[] mTabIcons = new int[]{R.drawable.tab_gongkuang1, R.drawable.tab_gongkuang3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youyan);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        initTabs();
        map = new YouyanMap();
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, map).commitAllowingStateLoss();
        currentFragment = map;
        currentTabIndex = 0;
        changeFragment(currentTabIndex);
    }
    private void initTabs() {
        mTabs = new View[]{mTab1, mTab2};
        String[] mTabTitles = getResources().getStringArray(R.array.youyan);
        mTabs[0].setSelected(true);
        for (int i = 0; i < mTabs.length; i++) {
            initTab(i, mTabTitles[i], mTabIcons[i]);
        }
    }
    private void initTab(final int position, String mTabTitle, int mTabIcon) {
        View tab = mTabs[position];

        ImageView tab_icon_iv = (ImageView) tab.findViewById(R.id.tab_icon_iv);
        TextView tab_title_tv = (TextView) tab.findViewById(R.id.tab_title_tv);

        tab_icon_iv.setImageResource(mTabIcon);
        tab_title_tv.setText(mTabTitle);
        mTabs[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(position);
            }
        });

    }
    public void changeFragment(int position) {
        for (int i = 0; i < mTabs.length; i++) {
            mTabs[i].setSelected(i == position);
        }
        switch (position) {
            case 0:
                if (map == null) {
                    map = new YouyanMap();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), map);
                break;

            case 1:
                if (newdata == null) {
                    newdata = new YouyanNewData();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), newdata);
                break;



        }
        currentTabIndex = position;
    }

    protected void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(R.id.main_content, fragment).commitAllowingStateLoss();
        } else {
            transaction.hide(currentFragment).show(fragment).commitAllowingStateLoss();
        }
        currentFragment = fragment;
    }
}
