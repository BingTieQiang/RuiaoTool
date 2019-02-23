package com.ruiao.tools.autowater;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ruiao.tools.voc.VocBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    List<Entry> entries1 = new ArrayList<>();  //数据集合
    XAxis xAxis = null;
    LineDataSet lineDataSet = null;
    private LineChart mLineChart;
    YAxis rightYAxis = null;
    YAxis leftYAxis;
    LineData data;
    int address = 0; //地址 1表示晋州
    public void setAddress ( int address)
    {
        this.address = address;

    }


    public LineChartManager(LineChart mLineChart) {
        this.mLineChart = mLineChart;
        bigList.add(new ArrayList<Entry>());
        bigList.add(new ArrayList<Entry>());
        lineDataSet = new LineDataSet(entries, "");
        data = new LineData(lineDataSet);
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
        xAxis.setLabelRotationAngle(10);
        leftYAxis.setAxisMinimum(0f);
        rightYAxis = mLineChart.getAxisRight();
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        Description description = new Description();
        description.setText("");
        mLineChart.setDescription(description);
        //设置Y轴不可缩放
        mLineChart.setScaleYEnabled(false);
        mLineChart.setScaleXEnabled(false);
        xAxis.setTextSize(9);
    }

    /**
     *
     * @param beans  数据arr
     *
     */
    public void setData(final ArrayList<WatorBean> beans )
    {
        int count = beans.size();
        bigList.get(0).clear();
        bigList.get(1).clear();
        entries.clear();
        entries1.clear();
        for (int i = 0;i < beans.size();i++)
        {

            entries.add(new Entry(i,Float.parseFloat(beans.get(i).cod)));
            entries1.add(new Entry(i,Float.parseFloat(beans.get(i).dan)));
        }

//        设置刻度，个刻度，两个小时一个刻度
        xAxis.setLabelCount(count, true);

        LineDataSet lineDataSet1 = new LineDataSet(entries, "COD（mg/L）");
        LineDataSet lineDataSet2= new LineDataSet(entries1, "氨氮（mg/L）");
        lineDataSet1.setColor(Color.RED);
        lineDataSet1.setCircleColor(Color.RED);
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet2.setDrawCircleHole(false);
        LineData data = new LineData();
        data.addDataSet(lineDataSet1);
        data.addDataSet(lineDataSet2);

        mLineChart.setData(data);

        mLineChart.invalidate();

        //设置X轴的字符串

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int pos = (int) value;
                if (pos == 1 || pos == 5 || pos == 8) {
                    return beans.get(pos).date;
                }
                return "";
            }
        });

        /*
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
                return  "";
            }
        });

*/
    }




}
