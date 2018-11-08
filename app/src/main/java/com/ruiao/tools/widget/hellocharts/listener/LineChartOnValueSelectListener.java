package com.ruiao.tools.widget.hellocharts.listener;


import com.ruiao.tools.widget.hellocharts.model.PointValue;

public interface LineChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int lineIndex, int pointIndex, PointValue value);

}
