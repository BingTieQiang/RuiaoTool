package com.ruiao.tools.dongtaiguankong;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.gongdiyangceng.GongdiLishi;
import com.ruiao.tools.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DongtaiActivity extends AppCompatActivity {
    private View[] mTabs;
    protected Fragment currentFragment;
    private int currentTabIndex;
    private DongtaiBaojing baojing;
    private DongtaiMap gongdiMap;
    private JanKong data;
    private DongtaiLishi gongdiLishi;
    @BindView(R.id.main_tab1)
    View mTab1;
    @BindView(R.id.main_tab2)
    View mTab2;
    @BindView(R.id.main_tab3)
    View mTab3;
    @BindView(R.id.main_tab4)
    View mTab4;
    private final int[] mTabIcons = new int[]{R.drawable.tab_gongkuang1, R.drawable.tab_gongkuang3,R.drawable.tab_gongkuang1,R.drawable.tab_gongkuang1,};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongtai);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        initTabs();
        gongdiMap = new DongtaiMap();
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, gongdiMap).commitAllowingStateLoss();
        currentFragment = gongdiMap;
        currentTabIndex = 0;
        changeFragment(currentTabIndex);
    }

    private void initTabs() {
        mTabs = new View[]{mTab1, mTab2,mTab3,mTab4};
        String[] mTabTitles = getResources().getStringArray(R.array.dongtai);
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
                if (gongdiMap == null) {
                    gongdiMap = new DongtaiMap();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), gongdiMap);
                break;

            case 1:
                if (gongdiLishi == null) {
                    gongdiLishi = new DongtaiLishi();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), gongdiLishi);
                break;
            case 2:
                if (data == null) {   //监控
                    data = new JanKong();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), data);
                break;
            case 3:
                if (baojing == null) {
                    baojing = new DongtaiBaojing();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), baojing);

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
