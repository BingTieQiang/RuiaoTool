package com.ruiao.tools.wuran;

import android.content.Context;

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
import com.ruiao.tools.ic_card2.IcBean1;
import com.ruiao.tools.utils.SPUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruiao on 2018/5/23.
 */

public class WuRanChartManager {
    private Context context;
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
    public WuRanChartManager(LineChart mLineChart,Context context) {
        this.context = context;
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
    public void setData(final ArrayList<WuranBean> beans , final int type)
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
/*
  //1 开头是气分钟数据
    public String QMyanchenliuliang; //流量
    public String QMyancennongdu; //烟尘
    public String QMyancenzhesuan; //烟尘折算
    public String QMeryanghualiu; //二氧化硫
    public String QMeryanghualiuzhesuan; //二氧化硫折算
    public String QMdanyang; //氮氧化物
    public String QMdanyangzhesuan; //氮氧化物折算

    public String yanCenLiuLiang;  //烟气流量 11
    public String yanCenNongDu;   //烟尘浓度
    public String yanCenZheSuan;   //烟尘折算
    public String so2;           //二氧化硫"
    public String so2ZheSuan;    //二氧化硫折算
    public String dan;            //氮氧化物
    public String danZheSuan;     //氮氧化物折算

     //2 开头是水 分钟数据
    public String SMliuliang;   //流量
    public String SMcod;        //COD
    public String SMnh3n;       //NH3N
    public String SMzonlin;     //总磷
    public String SMzondan;     //总氮

     //21 水设备 非分钟设备，
    public String liuLiangPinJun;  //流量平均值
    public String andanPingjun;    //氨氮
    public String CODPinJun;   //COD平均值
    public String zonlinPingJun;  //总磷
    public String zondanPingJun;   //总氮
 */
        String base = (String) SPUtils.get(context,"BASE","");
        for (int i = 0;i < beans.size();i++)
        {
//            new Entry(i,Float.parseFloat(beans.get(i).cod));
            if(beans.get(0).devtype == 1){ //气 分钟数据
                bigList.get(0).add(new Entry(i,Float.parseFloat(beans.get(i).QMyanchenliuliang)));
                bigList.get(1).add(new Entry(i,Float.parseFloat(beans.get(i).QMyancennongdu)));
                bigList.get(2).add(new Entry(i,Float.parseFloat(beans.get(i).QMyancenzhesuan)));
                bigList.get(3).add(new Entry(i,Float.parseFloat(beans.get(i).QMeryanghualiu)));
                bigList.get(4).add(new Entry(i,Float.parseFloat(beans.get(i).QMeryanghualiuzhesuan)));
                bigList.get(5).add(new Entry(i,Float.parseFloat(beans.get(i).QMdanyang)));
                bigList.get(6).add(new Entry(i,Float.parseFloat(beans.get(i).QMdanyangzhesuan)));

            }else if(beans.get(0).devtype == 11){ //气 非分钟数据
                bigList.get(0).add(new Entry(i,Float.parseFloat(beans.get(i).yanCenLiuLiang)));
                bigList.get(1).add(new Entry(i,Float.parseFloat(beans.get(i).yanCenNongDu)));
                bigList.get(2).add(new Entry(i,Float.parseFloat(beans.get(i).yanCenZheSuan)));
                bigList.get(3).add(new Entry(i,Float.parseFloat(beans.get(i).so2)));
                bigList.get(4).add(new Entry(i,Float.parseFloat(beans.get(i).so2ZheSuan)));
                bigList.get(5).add(new Entry(i,Float.parseFloat(beans.get(i).dan)));
                bigList.get(6).add(new Entry(i,Float.parseFloat(beans.get(i).danZheSuan)));
            }else if(beans.get(0).devtype == 2){ //水 分钟数据
                bigList.get(0).add(new Entry(i,Float.parseFloat(beans.get(i).SMliuliang)));
                bigList.get(1).add(new Entry(i,Float.parseFloat(beans.get(i).SMcod)));
                bigList.get(2).add(new Entry(i,Float.parseFloat(beans.get(i).SMnh3n)));
                if(base.startsWith("http://222.222.220.218")){ //晋州
                   continue;
                }
                bigList.get(3).add(new Entry(i,Float.parseFloat(beans.get(i).SMzonlin)));
                bigList.get(4).add(new Entry(i,Float.parseFloat(beans.get(i).SMzondan)));

            }else if(beans.get(0).devtype == 21){ //水 非分钟数据
                bigList.get(0).add(new Entry(i,Float.parseFloat(beans.get(i).liuLiangPinJun)));
                bigList.get(1).add(new Entry(i,Float.parseFloat(beans.get(i).CODPinJun)));
                bigList.get(2).add(new Entry(i,Float.parseFloat(beans.get(i).andanPingjun)));
                if(base.startsWith("http://222.222.220.218")){ //晋州
                    continue;
                }
                bigList.get(3).add(new Entry(i,Float.parseFloat(beans.get(i).zonlinPingJun)));
                bigList.get(4).add(new Entry(i,Float.parseFloat(beans.get(i).zondanPingJun)));
            }


        }
//        设置刻度，个刻度，两个小时一个刻度
        xAxis.setLabelCount(count, true);
/*
 private SimpleDateFormat formatSor = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private SimpleDateFormat formatSorMin = new SimpleDateFormat("MM-dd HH:mm");
    private SimpleDateFormat formatResDay = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat formatResHour = new SimpleDateFormat("MM-dd/HH");
 */
        //设置X轴的字符串
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int pos = (int) value;
                if(1==type)
                { //分钟数据  60个数据 10 个坐标，间隔6分钟
                    try {
                        return pos%6 == 0 ? formatSorMin.format(formatSor.parse(beans.get(pos).date)):"";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return pos%6==0 ? beans.get(pos).date:"";
                }
                else if(2==type) //10分钟 数据 6个数
                {

                    try {
                        return formatSorMin.format(formatSor.parse(beans.get(pos).date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return  (pos < beans.size()-1)?beans.get(pos).date:"";
                }
                else if(3==type) //小时 数据 24个
                {

                    try {
                        return pos%4 == 0 ? formatResHour.format(formatSor.parse(beans.get(pos).date)):"";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return  (pos < beans.size()-1)?beans.get(pos).date:"";
                }

                else if(4==type) //日数据 7天
                {
                    try {
                        return formatResDay.format(formatSor.parse(beans.get(pos).date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return  (pos < beans.size()-1)?beans.get(pos).date:"";
                }

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
//
        lineDataSet.setLineWidth(3f);
//
        if(1 == type){
            lineDataSet.setValueTextSize(0f);
//            lineDataSet.setValueFormatter(new IValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                    return "";
//                }
//            });
        }

    }


}
