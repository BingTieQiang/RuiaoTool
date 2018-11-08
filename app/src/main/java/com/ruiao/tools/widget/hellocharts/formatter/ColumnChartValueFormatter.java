package com.ruiao.tools.widget.hellocharts.formatter;


import com.ruiao.tools.widget.hellocharts.model.SubcolumnValue;

public interface ColumnChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SubcolumnValue value);

}
