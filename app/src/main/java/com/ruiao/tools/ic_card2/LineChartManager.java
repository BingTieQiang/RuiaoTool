package com.ruiao.tools.ic_card2;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ruiao on 2018/5/23.
 */

public class LineChartManager {
    private int comeFrome = 0;
//    "2018/5/20 8:28:00";
    private SimpleDateFormat formatSor = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private SimpleDateFormat formatSorMin = new SimpleDateFormat("MM-dd HH:mm");
    private SimpleDateFormat formatResDay = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat formatResHour = new SimpleDateFormat("MM-dd/HH");
    ArrayList<ArrayList<Entry>> bigList = new ArrayList();
    List<Entry> entries = new ArrayList<>();  //数据集合
    XAxis xAxis = null;
    LineDataSet lineDataSet = null;
    private LineChart mLineChart;
    YAxis rightYAxis = null;
    YAxis leftYAxis;
    LineData data;
    public LineChartManager(LineChart mLineChart) {
        this.mLineChart = mLineChart;
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());



        lineDataSet = new LineDataSet(entries, "");
        data = new LineData(lineDataSet);
//        mLineChart.setData(data);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);


    }

    /**
     * 初始化  可以设置颜色
     */
    public void initChart() {

        //一个LineDataSet就是一条线



        leftYAxis = mLineChart.getAxisLeft();

        //显示边界
        mLineChart.setDrawBorders(true);
        //X 轴
        xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置最小间隔
        xAxis.setGranularity(1f);
        //设置倾斜显示
        xAxis.setLabelRotationAngle(60);
        leftYAxis.setAxisMinimum(0f);


        rightYAxis = mLineChart.getAxisRight();
        rightYAxis.setEnabled(false); //右侧Y轴不显示





        Description description = new Description();
        description.setText("");
        mLineChart.setDescription(description);


        //设置Y轴不可缩放
        mLineChart.setScaleYEnabled(false);
        mLineChart.setScaleXEnabled(false);
    }

    /**
     *
     * @param beans  数据arr
     * @param type  那种类型的数据 日类型，小时数据，分钟数据
     */
    public void setData(final ArrayList<IcBean1> beans , final int type)
    {


        int count = beans.size();

        bigList.get(0).clear();
        bigList.get(1).clear();
        bigList.get(2).clear();
        bigList.get(3).clear();
        bigList.get(4).clear();
        bigList.get(5).clear();
        bigList.get(6).clear();
        bigList.get(7).clear();
        bigList.get(8).clear();
        bigList.get(9).clear();



        for (int i = 0;i < beans.size();i++)
        {
//            new Entry(i,Float.parseFloat(beans.get(i).cod));
            bigList.get(0).add(new Entry(i,Float.parseFloat(beans.get(i).cod)));
            bigList.get(1).add(new Entry(i,Float.parseFloat(beans.get(i).cods)));
            bigList.get(2).add(new Entry(i,Float.parseFloat(beans.get(i).nh3n)));
            bigList.get(3).add(new Entry(i,Float.parseFloat(beans.get(i).nh3ns)));
            bigList.get(4).add(new Entry(i,Float.parseFloat(beans.get(i).lin)));
            bigList.get(5).add(new Entry(i,Float.parseFloat(beans.get(i).linpaifan)));
            bigList.get(6).add(new Entry(i,Float.parseFloat(beans.get(i).dan)));
            bigList.get(7).add(new Entry(i,Float.parseFloat(beans.get(i).danpaifang)));
            bigList.get(8).add(new Entry(i,Float.parseFloat(beans.get(i).total)));
            bigList.get(9).add(new Entry(i,Float.parseFloat(beans.get(i).paishuiliang)));

        }
//        设置刻度，个刻度，两个小时一个刻度
        xAxis.setLabelCount(count, true);

        //设置X轴的字符串
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int pos = (int) value;
                if(0==type)
                { //分钟数据  60个数据 10 个坐标，间隔6分钟
                    try {
                        return pos%6 == 0 ? formatSorMin.format(formatSor.parse(beans.get(pos).data)):"";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return pos%6==0 ? beans.get(pos).data:"";
                }
                else if(1==type) //小时数据 7个数
                {

                    try {
                        return pos%3==0 ? formatResHour.format(formatSor.parse(beans.get(pos).data)):"";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(2==type) //日数据 7天
                {

                    return  (pos < beans.size()-1)?beans.get(pos).data:"";
                }

//                return mList[(int) value]; //mList为存有月份的集合
                return  "";
            }
        });


    }

    /**
     *
     * @param which 那种类型的数据 ，cod cods nh3n nh3ns
     */
    public void setWhich(int which,int type)
    {
        entries.clear();
        entries.addAll(bigList.get(which));
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        LineData data = new LineData(lineDataSet);

        mLineChart.setData(data);

        mLineChart.invalidate();
//        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        lineDataSet.setLineWidth(3f);

        if(0 == type){
            lineDataSet.setValueTextSize(0f);
            lineDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return "";
                }
            });
        }

    }


}
