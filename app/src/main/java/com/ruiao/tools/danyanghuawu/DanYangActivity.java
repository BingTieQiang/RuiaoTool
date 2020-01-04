package com.ruiao.tools.danyanghuawu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.KeyRadioGroupV1;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;

public class DanYangActivity extends AppCompatActivity {
    Date data = new Date();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日hh:mm");
    @BindView(R.id.arr_yangqi)
    TextView arrYangqi;   //氧气含量
    @BindView(R.id.arr_danyanghuawu)
    TextView arrDanyanghuawu;  //氧气含量
    @BindView(R.id.arr_zhesuandanyanghuawu)
    TextView arrZhesuandanyanghuawu;   //折算
    @BindView(R.id.arr_eryanghuadan)
    TextView arrEryanghuadan;        //二氧化硫
    @BindView(R.id.arr_yiyanghuadan)
    TextView arrYiyanghuadan;         //一氧化硫
    @BindView(R.id.tv_time)
    TextView arrTime;         //时间
    @BindView(R.id.Chart)
    BarChart Chart;
    long time_num;
    ArrayList<ArrayList<BarEntry>> lists = new ArrayList<>();
    BarChartManager manager;
    @BindView(R.id.krg_main_1)
    KeyRadioGroupV1 group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dan_yang);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        manager = new BarChartManager(Chart);
        manager.initLineChart("十二小时数据");
        String time = format.format(data);
        arrTime.setText(time);
        group.setOnCheckedChangeListener(new KeyRadioGroupV1.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(KeyRadioGroupV1 group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_danyanghuawu:
                        if(lists.size() == 0){
                            return;
                        }
                        manager.setData(lists.get(0));
                        break;
                    case R.id.btn_yangqihanliang:
                        if(lists.size() == 0){
                            return;
                        }
                        manager.setData(lists.get(1));
                        break;
                    case R.id.btn_zhesuan:
                        if(lists.size() == 0){
                            return;
                        }
                        manager.setData(lists.get(2));
                        break;
                    case R.id.btn_no2:
                        if(lists.size() == 0){
                            return;
                        }
                        manager.setData(lists.get(3));
                        break;
                    case R.id.btn_no1:
                        if(lists.size() == 0){
                            return;
                        }
                        manager.setData(lists.get(4));
                        break;
                }
            }
        });
        long zero =  System.currentTimeMillis()- ( System.currentTimeMillis()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
        time_num = zero;

        long ltime1 = new Date().getTime();
        long ltime2 = ltime1 - 1000 * 60 * 60 * 12;
        initChart(format1.format(new Date(ltime2)) , format1.format(ltime1));
        initChart1();
    }

    private void initChart1() {
        Date date = new Date();
        RequestParams pa = new RequestParams();
        pa.add("DevID", "1");
        pa.add("start", format1.format(new Date( date.getTime() - 1000 * 60 * 10)  ));
        pa.add("end", format1.format(date));
        pa.add("type", "1");
        HttpUtil.get(URLConstants.DANYANGHUAWU, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    lists.clear();
                    if (response.getBoolean("success")) {

                        JSONArray arr_aqi = response.getJSONArray("danyanghuawu");
                        JSONArray arr_pm10 = response.getJSONArray("o2");
                        JSONArray arr_pm25 = response.getJSONArray("zhesuandanyang");
                        JSONArray arr_co = response.getJSONArray("no2");
                        JSONArray arr_fengsu = response.getJSONArray("no");
//                        JSONArray arr_time = response.getJSONArray("time");

                        int lentgt = arr_aqi.length();
                        arrDanyanghuawu.setText( ""+ arr_aqi.getDouble(lentgt-1));
                        arrYangqi.setText(""+ arr_pm10.getDouble(lentgt-1));
                        arrZhesuandanyanghuawu.setText(""+ arr_pm25.getDouble(lentgt-1));
                        arrEryanghuadan.setText(""+ arr_co.getDouble(lentgt-1));
                        arrYiyanghuadan.setText(""+ arr_fengsu.getDouble(lentgt-1));


                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initChart(String start_time,String end_time) {
        RequestParams pa = new RequestParams();
        pa.add("DevID", "1");
        pa.add("start", start_time);
        pa.add("end", end_time);
        pa.add("type", "5");
        HttpUtil.get(URLConstants.DANYANGHUAWU, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    lists.clear();
                    if (response.getBoolean("success")) {

                        JSONArray arr_aqi = response.getJSONArray("danyanghuawu");
                        JSONArray arr_pm10 = response.getJSONArray("o2");
                        JSONArray arr_pm25 = response.getJSONArray("zhesuandanyang");
                        JSONArray arr_co = response.getJSONArray("no2");
                        JSONArray arr_fengsu = response.getJSONArray("no");
                        JSONArray arr_time = response.getJSONArray("time");

                        ArrayList<BarEntry> aqilist = new ArrayList<>();
                        for (int i = 0; i < arr_aqi.length(); i++) {
                            aqilist.add(new BarEntry(i, (float) arr_aqi.getDouble(i)));
                        }
                        lists.add(aqilist);

                        ArrayList<BarEntry> pm10list = new ArrayList<>();
                        for (int i = 0; i < arr_pm10.length(); i++) {
                            pm10list.add(new BarEntry(i, (float) arr_pm10.getDouble(i)));
                        }
                        lists.add(pm10list);

                        ArrayList<BarEntry> pm25list = new ArrayList<>();
                        for (int i = 0; i < arr_pm25.length(); i++) {
                            pm25list.add(new BarEntry(i, (float) arr_pm10.getDouble(i)));
                        }
                        lists.add(pm25list);

                        ArrayList<BarEntry> colist = new ArrayList<>();
                        for (int i = 0; i < arr_co.length(); i++) {
                            colist.add(new BarEntry(i, (float) arr_co.getDouble(i)));
                        }
                        lists.add(colist);

                        ArrayList<BarEntry> fengsulist = new ArrayList<>();
                        for (int i = 0; i < arr_fengsu.length(); i++) {
                            fengsulist.add(new BarEntry(i, (float) arr_fengsu.getDouble(i)));
                        }
                        lists.add(fengsulist);

                        ArrayList<String> times = new ArrayList<>();
                        for (int i = 0; i < arr_fengsu.length(); i++) {
                            times.add(arr_time.getString(i));
                        }
                        manager.setTime(times);
                        manager.setData(lists.get(0));

                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnClick({R.id.ll_24, R.id.ll_tian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_24:
                startActivity(new Intent(getContext(),DanyangFenzhongActivity.class));
                break;
            case R.id.ll_tian:
                startActivity(new Intent(getContext(),DanyangDayActivity.class));
                break;
        }
    }
}
