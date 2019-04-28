package com.ruiao.tools.the;

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
import com.ruiao.tools.autowater.WatorBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ruiao on 2018/5/23.
 */

public class GongkuangChartManager {
    private int comeFrome = 0;
//    "2018/5/20 8:28:00";
    private SimpleDateFormat formatSorformatSor = new SimpleDateFormat("HH点mm分");
    private SimpleDateFormat formatSorMin = new SimpleDateFormat("MM-dd HH:mm");
    private SimpleDateFormat formatResDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatResHour = new SimpleDateFormat("MM-dd/HH");
    ArrayList<ArrayList<Entry>> bigList = new ArrayList();
    List<Entry> entries = new ArrayList<>();  //数据集合
    List<Entry> entries1 = new ArrayList<>();  //数据集合
    List<Entry> entries2 = new ArrayList<>();  //数据集合
    List<Entry> entries3 = new ArrayList<>();  //数据集合
    List<Entry> entries4 = new ArrayList<>();  //数据集合
    List<Entry> entries5 = new ArrayList<>();  //数据集合
    List<Entry> entries6 = new ArrayList<>();  //数据集合
    List<Entry> entries7 = new ArrayList<>();  //数据集合
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


    public GongkuangChartManager(LineChart mLineChart) {
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
        xAxis.setLabelRotationAngle(30);
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
    public void setData(final ArrayList<GongkuangBean> beans )
    {
        int count = beans.size();
        bigList.get(0).clear();
        bigList.get(1).clear();
        entries.clear();
        entries1.clear();
        entries2.clear();
        entries3.clear();
        entries4.clear();
        entries5.clear();
        entries6.clear();
        entries7.clear();
        for (int i = 0;i < beans.size();i++)
        {

            entries.add(new Entry(i,Float.parseFloat(beans.get(i).dianlu)));
            entries1.add(new Entry(i,Float.parseFloat(beans.get(i).qilu)));
            entries2.add(new Entry(i,Float.parseFloat(beans.get(i).lasizong)));
            entries3.add(new Entry(i,Float.parseFloat(beans.get(i).yihao)));
            entries4.add(new Entry(i,Float.parseFloat(beans.get(i).erhao)));
            entries5.add(new Entry(i,Float.parseFloat(beans.get(i).sanhao)));
            entries6.add(new Entry(i,Float.parseFloat(beans.get(i).sihao)));
            entries7.add(new Entry(i,Float.parseFloat(beans.get(i).wuhao)));
        }

//        设置刻度，个刻度，两个小时一个刻度
        xAxis.setLabelCount(count, true);

        LineDataSet lineDataSet1 = new LineDataSet(entries, "电炉");
        LineDataSet lineDataSet2= new LineDataSet(entries1, "气炉");
        LineDataSet lineDataSet3= new LineDataSet(entries1, "拉丝总");
        LineDataSet lineDataSet4= new LineDataSet(entries1, "1号大中拔生产线");
        LineDataSet lineDataSet5= new LineDataSet(entries1, "2号大中拔生产线");
        LineDataSet lineDataSet6= new LineDataSet(entries1, "3号大中拔生产线");
        LineDataSet lineDataSet7= new LineDataSet(entries1, "4号大中拔生产线");
        LineDataSet lineDataSet8= new LineDataSet(entries1, "5号大中拔生产线");


        lineDataSet1.setColor(Color.RED);
        lineDataSet1.setCircleColor(Color.RED);

        lineDataSet2.setColor(Color.BLUE);
        lineDataSet2.setCircleColor(Color.BLUE);

        lineDataSet3.setColor(Color.YELLOW);
        lineDataSet3.setCircleColor(Color.YELLOW);

        lineDataSet4.setColor(Color.GREEN);
        lineDataSet4.setCircleColor(Color.GREEN);

        lineDataSet5.setColor(Color.BLUE);
        lineDataSet5.setCircleColor(Color.BLUE);

        lineDataSet6.setColor(Color.CYAN);
        lineDataSet6.setCircleColor(Color.CYAN);

        lineDataSet7.setColor(Color.BLACK);
        lineDataSet7.setCircleColor(Color.BLACK);

        lineDataSet8.setColor(Color.MAGENTA);
        lineDataSet8.setCircleColor(Color.MAGENTA);

        lineDataSet1.setDrawCircleHole(false);
        lineDataSet2.setDrawCircleHole(false);
        LineData data = new LineData();

        data.addDataSet(lineDataSet1);
        data.addDataSet(lineDataSet2);
        data.addDataSet(lineDataSet3);
        data.addDataSet(lineDataSet4);
        data.addDataSet(lineDataSet5);
        data.addDataSet(lineDataSet6);
        data.addDataSet(lineDataSet7);
        data.addDataSet(lineDataSet8);

        mLineChart.setData(data);

        mLineChart.invalidate();

        //设置X轴的字符串

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int pos = (int) value;
//                if (pos == 1 || pos == 5 || pos == 8) {
//                    return beans.get(pos).date;
//                }
                try {
                    Date data = formatResDay.parse(beans.get(pos).date);
                    return formatSorformatSor.format(data);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return  "" ;
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
