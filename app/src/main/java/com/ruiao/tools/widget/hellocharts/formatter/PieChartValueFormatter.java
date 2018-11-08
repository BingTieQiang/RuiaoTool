package com.ruiao.tools.widget.hellocharts.formatter;


import com.ruiao.tools.widget.hellocharts.model.SliceValue;

public interface PieChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SliceValue value);
}
