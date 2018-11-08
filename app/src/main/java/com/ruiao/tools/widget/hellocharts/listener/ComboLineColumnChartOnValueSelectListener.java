package com.ruiao.tools.widget.hellocharts.listener;


import com.ruiao.tools.widget.hellocharts.model.PointValue;
import com.ruiao.tools.widget.hellocharts.model.SubcolumnValue;

public interface ComboLineColumnChartOnValueSelectListener extends OnValueDeselectListener {

    public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

    public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value);

}
