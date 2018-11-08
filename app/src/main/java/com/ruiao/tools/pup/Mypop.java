package com.ruiao.tools.pup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;


import com.ruiao.tools.R;

import java.util.ArrayList;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by ruiao on 2018/5/7.
 */

public class Mypop extends BasePopupWindow implements View.OnClickListener{
    private View popupView;
    private Context context;
    private ArrayList<Class> list = null;
    public Mypop(Context context,ArrayList list) {
        super(context);
        this.context = context;
        bindEvent();
        this.list = list;
    }


    @Override
    protected Animation initShowAnimation() {
        return getDefaultScaleAnimation();
    }

    @Override
    protected Animation initExitAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.pup_layout, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
            popupView.findViewById(R.id.tx_4).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1:
                context.startActivity(new Intent(context,list.get(0)));
                dismiss();
                break;
            case R.id.tx_2:  //设备状态

                context.startActivity(new Intent(context,list.get(1)));
                break;
            case R.id.tx_3:  //历史数据

                context.startActivity(new Intent(context,list.get(2)));
                break;
            case R.id.tx_4:  //累计排水量

                context.startActivity(new Intent(context,list.get(3)));
                break;
            default:
                break;
        }

    }
}
