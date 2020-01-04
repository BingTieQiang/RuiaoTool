package com.ruiao.tools.fenbiao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.the.Baojing;
import com.ruiao.tools.the.LishiQuxian;
import com.ruiao.tools.the.Map;
import com.ruiao.tools.the.PageChangeLinster;
import com.ruiao.tools.the.ShishiGongkuang1;
import com.ruiao.tools.ui.MyFragmentPagerAdapter;
import com.ruiao.tools.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FenbiaoActivity extends AppCompatActivity {
    public String id = "11";
    public boolean isSelectMap = true;  //点击了地图，这个按钮
    @BindView(R.id.main_tab1)
    View mTab1;
    @BindView(R.id.main_tab2)
    View mTab2;
    @BindView(R.id.main_tab3)   View mTab3;
    @BindView(R.id.main_tab4)
    View mTab4;
    Intent intent;
//    @BindView(R.id.mypager)
//    MyRollPager myviewpager;
    MyFragmentPagerAdapter adapter = null;
    ArrayList<Fragment> fragments = null;
    private View[] mTabs;
    protected Fragment currentFragment;
    private final int[] mTabIcons = new int[]{R.drawable.tab_gongkuang1, R.drawable.tab_gongkuang2, R.drawable.tab_gongkuang3, R.drawable.tab_gongkuang4};
    FenbiaoMap fragment3;
    FengbiaoLishi lishiQuxian;
    FragmentGongkuang fragment2;
    FragmentFengbiaoBaojing baojing;
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenbiao);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        intent = getIntent();
        initTabs();
        boolean ismsg = intent.getBooleanExtra("msg", false);
        if (ismsg) {

        }
        fragment3 = new FenbiaoMap();
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, fragment3).commitAllowingStateLoss();
        currentFragment = fragment3;
        currentTabIndex = 0;
        changeFragment(currentTabIndex);
    }

//    public void setPager(int page) {
//        myviewpager.setCurrentItem(page);
//    }


    private void initTabs() {
        mTabs = new View[]{mTab1, mTab2, mTab3, mTab4};
        String[] mTabTitles = getResources().getStringArray(R.array.gongkuang);
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
//        myviewpager.setCurrentItem(position);

        switch (position) {
            case 0:
                if (fragment3 == null) {
                    fragment3 = new FenbiaoMap();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), fragment3);
                break;

            case 1:
                if (lishiQuxian == null) {
                    lishiQuxian = new FengbiaoLishi( );  //历史
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), lishiQuxian);
                break;

            case 2:
                if (fragment2 == null) {
                    fragment2 = new FragmentGongkuang();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), fragment2);
                break;
            case 3:
                if (baojing == null) {
                    baojing = new FragmentFengbiaoBaojing();
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
        currentFragment =  fragment;
    }

    public void setPager(int position, String str_mark){
        id = str_mark;
        changeFragment(position);
        PageChangeLinster pageChangeLinster = (PageChangeLinster) currentFragment;
        pageChangeLinster.setInfo(str_mark);
    }
}
