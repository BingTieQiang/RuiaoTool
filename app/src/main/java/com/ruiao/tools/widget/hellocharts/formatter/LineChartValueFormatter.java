package com.ruiao.tools.widget.hellocharts.formatter;


import com.ruiao.tools.widget.hellocharts.model.PointValue;

public interface LineChartValueFormatter {

    public int formatChartValue(char[] formattedValue, PointValue value);
}
