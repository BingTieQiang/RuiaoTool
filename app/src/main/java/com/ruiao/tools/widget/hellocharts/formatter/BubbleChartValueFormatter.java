package com.ruiao.tools.widget.hellocharts.formatter;


import com.ruiao.tools.widget.hellocharts.model.BubbleValue;

public interface BubbleChartValueFormatter {

    public int formatChartValue(char[] formattedValue, BubbleValue value);
}
