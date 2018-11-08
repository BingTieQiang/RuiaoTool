package com.ruiao.tools.widget.hellocharts.listener;


import com.ruiao.tools.widget.hellocharts.model.BubbleValue;

public interface BubbleChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int bubbleIndex, BubbleValue value);

}
