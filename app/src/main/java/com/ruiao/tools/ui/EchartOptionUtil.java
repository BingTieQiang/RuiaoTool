package com.ruiao.tools.ui;

import com.github.abel533.echarts.Grid;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;

import java.util.ArrayList;

public class EchartOptionUtil {
    public static GsonOption getLineChartOptions(String[] names , Object[] xAxis, ArrayList<Object[]> yAxis) {
        GsonOption option = new GsonOption();
        option.title("");   //总名称
        option.legend(names);
        option.tooltip().trigger(Trigger.axis);

        ValueAxis valueAxis = new ValueAxis();
        option.yAxis(valueAxis);

        CategoryAxis categorxAxis = new CategoryAxis();
        categorxAxis.axisLine().onZero(false);
        categorxAxis.boundaryGap(true);
        categorxAxis.data(xAxis);
        option.xAxis(categorxAxis);
        Grid grid = new Grid();
        grid.setTop("25%");
        option.setGrid(grid);
        for(int i = 0;i<yAxis.size();i++){
            Line line2 = new Line();
            line2.smooth(false).name(names[i]).data(yAxis.get(i)).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
            option.series(line2);
        }
        return option;
    }
}
