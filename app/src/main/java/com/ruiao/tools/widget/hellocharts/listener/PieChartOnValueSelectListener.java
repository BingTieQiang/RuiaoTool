package com.ruiao.tools.widget.hellocharts.listener;


import com.ruiao.tools.widget.hellocharts.model.SliceValue;

public interface PieChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int arcIndex, SliceValue value);

}
