package com.ruiao.tools.ic_card2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AsynHttpTools;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.Pbdialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

/**
 * 查询历史
 * 时间点，当前时间减少2个小时，服务器才有统计
 * 选择日数据，选择小时数据，选择分钟数据
 * 向后向前  tian  shi fen xiaoshi
 */
public class IcHistroyActivity extends AppCompatActivity {
    int address = 0; //1 是晋州
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    private int nowArr = 0; //默认是0
    TimePickerView thisTime;
    @BindView(R.id.tag_group)
    TagGroup tagGroup;
    //    @BindView(R.id.chart_type_rg1)
//    RadioGroup chartTypeRg1;
    private Pbdialog dialog; //进度条
    private String MonitorID;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String time1 = format.format(hourTime);
            String time2 = format.format(new Date(hourTime.getTime() - 24 * 60 * 60 * 1000));
            initData(time1, time2, "xiaoshi");

        }
    };

    private LineChartManager lineChartManager;
    Date nowTime = new Date();
    Date hourTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000);  //小时数据
    Date dayTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000);    //日数据
    Date minuteTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000); //分钟数据
    @BindView(R.id.time_current)
    TextView timeCurrent; //当前显示的时间
    @BindView(R.id.radiogroup_time_type)
    RadioGroup radiogroupTimeType;
//    @BindView(R.id.chart_type_rg)
//    RadioGroup chartTypeRg;

    private int type;   // 0分钟数据,1小时数据,2日数据
    @BindView(R.id.lineChart)
    LineChart mLineChart;
    private RecyclerView recyclerview;
    private IcAdapter1 adapter;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_histroy);
        ButterKnife.bind(this);
        initView();
//        initData("2018-5-17 15:00:00","2018-5-18 15:57:00");

        new Thread() {
            @Override
            public void run() {
                super.run();
                handler.sendMessageDelayed(new Message(), 500);
            }
        }.start();
    }

    private void initView() {
        StatusBarUtil.darkMode(this);
        String base = (String) SPUtils.get(this,"BASE","");
        if(base.startsWith("http://222.222.220.218")){   //"晋州"
            address = 1;
        }else if(base.startsWith("http://222.223.112.252")){  //清河

        }else if(base.startsWith("http://110.249.145.94")){   //宁晋

        }
        type = 0;// 默认日数据
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new IcAdapter1(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);

        //选择组 选中切换
        radiogroupTimeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.btn_0:
                        type = 0;  //分钟
                        initData(format.format(minuteTime), format.format(new Date(minuteTime.getTime() - 1 * 60 * 60 * 1000)), "fen");
                        break;
                    case R.id.btn_1:
                        type = 1;  //小时  显示最近24小时的数据
                        initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 24 * 60 * 60 * 1000)), "xiaoshi");
                        break;
                    case R.id.btn_2:
                        type = 2;  //日数据
                        initData(format.format(dayTime), format.format(new Date(dayTime.getTime() - 7 * 24 * 60 * 60 * 1000)), "tian");
                        break;
                    default:
                        break;

                }
            }
        });
        if(address ==1)
        {
            tagGroup.setTags(new String[]{"COD", "COD排放量", "NH3N", "NH3N排放量", "流量", "排水量"});
        }else {
            tagGroup.setTags(new String[]{"COD", "COD排放量", "NH3N", "NH3N排放量", "总磷", "总磷排放量", "总氮", "总氮排放量", "流量", "排水量"});
        }

        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                switch (tag) {
                    case "COD":
                        lineChartManager.setWhich(0, type);
                        if (nowArr == 1) {
                            nowArr = 0;
                            adapter.notifyChange(nowArr);
                            selectState();
                        }
                        break;
                    case "COD排放量":
                        lineChartManager.setWhich(1, type);
                        if (nowArr == 1) {
                            nowArr = 0;
                            adapter.notifyChange(nowArr);
                            selectState();
                        }
                        break;
                    case "NH3N":
                        lineChartManager.setWhich(2, type);
                        if (nowArr == 1) {
                            nowArr = 0;
                            adapter.notifyChange(nowArr);
                            selectState();
                        }
                        break;
                    case "NH3N排放量":
                        lineChartManager.setWhich(3, type);
                        if (nowArr == 1) {
                            nowArr = 0;
                            adapter.notifyChange(nowArr);
                            selectState();
                        }
                        break;
                    case "总磷":
                        lineChartManager.setWhich(4, type);
                        if (nowArr == 0) {
                            nowArr = 1;
                            adapter.notifyChange(nowArr);
                            selectState();
                        }
                        break;
                    case "总磷排放量":
                        lineChartManager.setWhich(5, type);
                        if (nowArr == 0) {
                            nowArr = 1;
                            adapter.notifyChange(nowArr);
                            selectState();
                        }
                        break;
                    case "总氮":
                        lineChartManager.setWhich(6, type);
                        if (nowArr == 0) {
                            nowArr = 1;
                            adapter.notifyChange(nowArr);
                            selectState();
                        }
                        break;
                    case "总氮排放量":
                        lineChartManager.setWhich(7, type);
                        if (nowArr == 0) {
                            nowArr = 1;
                            adapter.notifyChange(nowArr);
                            selectState();
                        }
                        break;
                    case "流量":
                        lineChartManager.setWhich(8, type);
                        break;
                    case "排水量":
                        lineChartManager.setWhich(9, type);
                        break;
                    default:

                        break;

                }
            }
        });

        timeCurrent.setText(format.format(hourTime));
        lineChartManager = new LineChartManager(mLineChart);
        lineChartManager.initChart();
    }

    private void selectState() {
        if(0==nowArr){
            tv1.setText("COD(mg/L)");
            tv2.setText("COD排放量(kg)");
            tv3.setText("NH₃N(mg/L)");
            tv4.setText("NH₃N排放量(kg)");
            tv5.setText("排水量(m3)");
        }else {
            tv1.setText("总磷(mg/L)");
            tv2.setText("总磷排放量(kg)");
            tv3.setText("总氮(mg/L)");
            tv4.setText("总氮排放量(kg)");
            tv5.setText("流量(L/s)");
        }

    }

    /**
     * @param end   终止时间
     * @param start 前推时间
     */
    private void initData(String end, String start, final String typex) {
        String MonitorID = getIntent().getStringExtra("MonitorID");

//        Log.d("ttt",time1+""+time2);
        RequestParams pa = new RequestParams();
        pa.add("start", start);
        pa.add("end", end);
        pa.add("type", typex);
        pa.add("MonitorID", MonitorID);

        dialog = showdialog(this, "正在加载.......");
        AsynHttpTools.httpGetMethodByParams(URLConstants.IC, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFinish() {
                super.onFinish();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();


            }

            @Override
            public void onFailure(int code, Header[] heads, Throwable throwable, JSONObject json) {
                throwable.printStackTrace();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // list 按照时间排列
                ArrayList<IcBean1> beans = new ArrayList<>();
                try {

                    Boolean status = response.getBoolean("success");
                    if (status) {
                        IcBean1 bean = null;
                        JSONArray codarr = response.getJSONArray("COD");
                        JSONArray codsarr = response.getJSONArray("COD排放量");
                        JSONArray nh3sarr = response.getJSONArray("NH3N");
                        JSONArray nh3ssarr = response.getJSONArray("NH3N排放量");
                        JSONArray totalarr = response.getJSONArray("流量");
                        JSONArray paishuiliang =  response.getJSONArray("排水量");
                        JSONArray totalin = null;
                        JSONArray totalinpaifang = null;
                        JSONArray totaldan = null;
                        JSONArray totaldanpaifang = null;

                        if(address == 1)
                        {
                        }else{
                             totalin = response.getJSONArray("总磷");
                             totalinpaifang = response.getJSONArray("总磷排放量");
                             totaldan = response.getJSONArray("总氮");
                             totaldanpaifang = response.getJSONArray("总氮排放量");
                        }
                        JSONObject onj;

                        for (int i = 0; i < codarr.length(); i++) {
                            bean = new IcBean1();
                            onj = codarr.getJSONObject(i);

                            bean.nom = i;
                            bean.data = onj.getString("id");
                            bean.cod = "" + onj.getDouble("value");

                            onj = codsarr.getJSONObject(i);
                            bean.cods = "" + onj.getDouble("value");

                            onj = nh3sarr.getJSONObject(i);
                            bean.nh3n = "" + onj.getDouble("value");

                            onj = nh3ssarr.getJSONObject(i);
                            bean.nh3ns = "" + onj.getDouble("value");


                            onj = totalarr.getJSONObject(i);
                            bean.total = "" + onj.getDouble("value");
                            if(address == 1)
                            {
                            }else{
                                onj = totalin.getJSONObject(i);
                                bean.lin = "" + onj.getDouble("value");

                                onj = totalinpaifang.getJSONObject(i);
                                bean.linpaifan = "" + onj.getDouble("value");

                                onj = totaldan.getJSONObject(i);
                                bean.dan = "" + onj.getDouble("value");

                                onj = totaldanpaifang.getJSONObject(i);
                                bean.danpaifang = "" + onj.getDouble("value");
                            }


                            onj = paishuiliang.getJSONObject(i);
                            bean.paishuiliang = "" + onj.getDouble("value");

                            beans.add(bean);
                        }
                        adapter.setDate(beans);
                        lineChartManager.setAddress(address);
                        lineChartManager.setData(beans, type);
                        lineChartManager.setWhich(1, type);
                    } else {
                        ToastHelper.shortToast(IcHistroyActivity.this, response.getString("message"));
                    }
                } catch (JSONException e) {
                    ToastHelper.shortToast(IcHistroyActivity.this, "数据解析错误");
                    //JSON数据格式错误
                    e.printStackTrace();
                }
            }
        });

    }


    @OnClick({R.id.tim_back, R.id.time_go, R.id.time_current})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tim_back:  //时间回退
                if (0 == type)  //分钟
                {
                    minuteTime.setTime(minuteTime.getTime() - 1 * 60 * 60 * 1000);
                    initData(format.format(minuteTime), format.format(new Date(minuteTime.getTime() - 1 * 60 * 60 * 1000)), "fen");
                    timeCurrent.setText(format.format(minuteTime));
                } else if (1 == type) { //小时
                    hourTime.setTime(hourTime.getTime() - 24 * 60 * 60 * 1000);
                    initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 24 * 60 * 60 * 1000)), "xiaoshi");
                    timeCurrent.setText(format.format(hourTime));
                } else if (2 == type) {  //日
                    dayTime.setTime(dayTime.getTime() - 7 * 24 * 60 * 60 * 1000);
                    initData(format.format(dayTime), format.format(new Date(dayTime.getTime() - 7 * 24 * 60 * 60 * 1000)), "tian");
                    timeCurrent.setText(format.format(dayTime));
                }
                break;
            case R.id.time_go:   //时间前进


                if (0 == type)  //10分钟
                {
                    if ((minuteTime.getTime() + 1 * 60 * 60 * 1000) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                        ToastHelper.shortToast(IcHistroyActivity.this, "此时间段无数据");
                        return;
                    }
                    minuteTime.setTime(minuteTime.getTime() + 1 * 60 * 60 * 1000);
                    initData(format.format(minuteTime), format.format(new Date(minuteTime.getTime() - 1 * 60 * 60 * 1000)), "fen");
                    timeCurrent.setText(format.format(minuteTime));
                } else if (1 == type) { //小时

                    if ((hourTime.getTime() + 24 * 60 * 60 * 1000) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                        ToastHelper.shortToast(IcHistroyActivity.this, "此时间段无数据");
                        return;
                    }
                    hourTime.setTime(hourTime.getTime() + 24 * 60 * 60 * 1000);
                    initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 24 * 60 * 60 * 1000)), "xiaoshi");
                    timeCurrent.setText(format.format(hourTime));
                } else if (2 == type) {  //日

                    if ((dayTime.getTime() + 7 * 24 * 60 * 60 * 1000) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                        ToastHelper.shortToast(IcHistroyActivity.this, "此时间段无数据");
                        return;
                    }
                    dayTime.setTime(dayTime.getTime() + 7 * 24 * 60 * 60 * 1000);
                    initData(format.format(dayTime), format.format(new Date(dayTime.getTime() - 7 * 24 * 60 * 60 * 1000)), "tian");
                    timeCurrent.setText(format.format(dayTime));
                }
                break;
            case R.id.time_current:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {

                        if (0 == type) {
                            if ((date.getTime()) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                                ToastHelper.shortToast(IcHistroyActivity.this, "此时间段无数据");
                                return;
                            }
                            minuteTime.setTime(date.getTime());
                            initData(format.format(minuteTime), format.format(new Date(minuteTime.getTime() - 1 * 60 * 60 * 1000)), "fen");
                        } else if (1 == type) {
                            if ((date.getTime()) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                                ToastHelper.shortToast(IcHistroyActivity.this, "此时间段无数据");
                                return;
                            }
                            hourTime.setTime(date.getTime());
                            initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 24 * 60 * 60 * 1000)), "xiaoshi");
                        } else if (2 == type) {
                            if ((date.getTime()) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                                ToastHelper.shortToast(IcHistroyActivity.this, "此时间段无数据");
                                return;
                            }
                            dayTime.setTime(date.getTime());
                            initData(format.format(dayTime), format.format(new Date(dayTime.getTime() - 7 * 24 * 60 * 60 * 1000)), "tian");
                        }

                        timeCurrent.setText(format.format(date));
                    }
                }).setType(new boolean[]{true, true, true, true, true, true}).setTitleText("请选择时间").build();
                thisTime.show();
                break;
        }
    }

    /**
     * 展示进度条
     *
     * @param context
     * @param msg
     * @return
     */
    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }


    public void xiehuan(View view) {

        if (nowArr == 0) {
            nowArr = 1;
            adapter.notifyChange(nowArr);
            selectState();
        }else if(nowArr == 1){
            nowArr = 0;
            adapter.notifyChange(nowArr);
            selectState();
        }
    }
}
