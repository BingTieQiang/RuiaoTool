package com.ruiao.tools.ui.fragment.maintab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.ruiao.tools.R;
import com.ruiao.tools.advertisement.AdActivity;
import com.ruiao.tools.autowater.WatorActivity;
import com.ruiao.tools.ic_card.IcCardActivity;
import com.ruiao.tools.ic_card.IcDeviceStateActivity;
import com.ruiao.tools.ic_card.IcDeviceStateActivity1;
import com.ruiao.tools.ic_card2.ICActivity;
import com.ruiao.tools.pup.Mypop;
import com.ruiao.tools.ui.activity.functionfragment.WordReportAdapter;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.ui.rollpagerAdapter.RollPagerAdapter;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.voc.VocActivity;
import com.ruiao.tools.wuran.WuRangActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by gs on 2017/9/15.
 */

public class EnergyFragment extends BaseFragment {

    @BindView(R.id.rollpager)
    RollPagerView rollpager;
    @BindView(R.id.option_fucntion_grid)
    GridView optionFucntionGrid;

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_energy;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        StatusBarUtil.darkMode(getActivity());
        optionFucntionGrid.setAdapter(new WordReportAdapter(getActivity()));
        optionFucntionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String base = (String)SPUtils.get(getContext(),"BASE","");
                Intent intent = new Intent();
                ArrayList<Class> list = new ArrayList<>();
                if(position==0){//VOC
                    if(base.startsWith("http://222.222.220.218")){ //晋州
                        startActivity(new Intent(mContext,VocActivity.class));
                    }



                }else if(position==2){


                    startActivity(new Intent(mContext, WuRangActivity.class));
                }


                else if(position==8){

                    if(base.startsWith("http://110.249.145.94:11888/")){
                        ToastHelper.shortToast(mContext,"宁晋暂未安装此设备");
                        return;
                    }
                    startActivity(new Intent(mContext,ICActivity.class));

                }else if(position==4){
                    if(base.startsWith("http://222.222.220.218")){ //晋州
                        startActivity(new Intent(mContext,WatorActivity.class));
                    }else {
                        ToastHelper.shortToast(mContext,"暂未安装此设备");
                    }
                }

                else {
                    ToastHelper.shortToast(mContext,"未安装此设备");
                }

            }
        });
        initRollpager();

    }

    public void initRollpager()
    {

        rollpager.setAdapter(new RollPagerAdapter());
        rollpager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                startActivity(new Intent(mContext, AdActivity.class));
            }
        });
    }


    @Override
    public void getmsg() {

    }
}
