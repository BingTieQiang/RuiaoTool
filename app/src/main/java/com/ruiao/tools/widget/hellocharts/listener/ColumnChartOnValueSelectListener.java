package com.ruiao.tools.widget.hellocharts.listener;


import com.ruiao.tools.widget.hellocharts.model.SubcolumnValue;

public interface ColumnChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

}
