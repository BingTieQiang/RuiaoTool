package com.ruiao.tools.dongtaiguankong.histroy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.dongtaiguankong.histroy.AqiBean;
import com.ruiao.tools.aqi.TableBean;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;

//分钟数据
public class DongtaiTianActivity extends AppCompatActivity {
    Date data = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日HH时");
    TimePickerView thisTime;
    //氮氧化物，按照天查询
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;       //当前时间
    ArrayList<ShuiBean> beanlist = new ArrayList<>();
    ArrayList<QiBean> qitibeanlist = new ArrayList<>();
    long time_num;
    AqiBean bean;
    String MonitorID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongtai_tian);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        MonitorID = getIntent().getStringExtra("MonitorID");
        tvNewDate.setText(format.format(data.getTime() - 1000 * 3600 * 24));
        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowTableTitle(false);
        long zero =  System.currentTimeMillis()- ( System.currentTimeMillis()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1000 * 3600 * 24;
        time_num = zero;
        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 24)));
    }

    @OnClick({R.id.tv_pre, R.id.tv_after,R.id.tv_newDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pre:
                time_num = time_num - 1000 * 3600 * 24;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 24)));
                break;
            case R.id.tv_after:
                time_num = time_num + 1000 * 3600 * 24;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 24)));
                break;
            case R.id.tv_newDate:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time_num = date.getTime() - ( date.getTime()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1000 * 3600 * 24;
                        tvNewDate.setText( format.format(date));
                        initChart( format1.format(new Date(time_num + 1000 * 3600 * 24)),format1.format(new Date(time_num +2 * 1000 * 3600 * 24))  );
                    }
                }).setType(new boolean[]{true, true, true, false, false, false}).setTitleText("请选择时间").build();
                thisTime.show();

                break;
        }
    }

    private void initChart(String start_time,String end_time) {
        RequestParams pa = new RequestParams();
        pa.add("Start", start_time);
        pa.add("ActType", "5");
        pa.add("End", end_time);
        pa.add("username", (String) SPUtils.get(this, "username", ""));
        pa.add("Rows", "60");
        pa.add("MonitorID", MonitorID);
        HttpUtil.get(URLConstants.Dongtai_Histry, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                    beanlist.clear();
                    if (response.getBoolean("success")) {
                        if(response.has("废气")){
                            JSONArray arr_fiqi = response.getJSONArray("废气");
                            JSONArray arr_yancen = response.getJSONArray("烟尘");
                            JSONArray arr_yanzhenzesuan = response.getJSONArray("烟尘折算");
                            JSONArray arr_so2 = response.getJSONArray("二氧化硫");
                            JSONArray arr_so2zhwesuan = response.getJSONArray("二氧化硫折算");
                            JSONArray arr_danyang = response.getJSONArray("氮氧化物");
                            JSONArray arr_danyangzhesuan = response.getJSONArray("氮氧化物折算");
                            JSONArray arr_o2 = response.getJSONArray("氧气含量");
                            JSONArray arr_yanqiliusu = response.getJSONArray("烟气流速");
                            JSONArray arr_yanqiwendu = response.getJSONArray("烟气温度");
                            JSONArray arr_yanqiyali = response.getJSONArray("烟气压力");
                            JSONArray time = response.getJSONArray("time");

                            for(int i = 0 ;i < time.length();i++){
                                QiBean bean = new QiBean();
                                String ss = time.getString(i);
                                Date date = format1.parse(ss);
                                bean.time = format2.format(date);
                                bean.feiqi = arr_fiqi.getString(i);
                                bean.yanceng = arr_yancen.getString(i);
                                bean.yanchengzhesuan = arr_yanzhenzesuan.getString(i);
                                bean.so2 = arr_so2.getString(i);
                                bean.so2zhesuan = arr_so2zhwesuan.getString(i);
                                bean.danyang = arr_danyang.getString(i);
                                bean.danyangzhesuan = arr_danyangzhesuan.getString(i);
                                bean.o2 = arr_o2.getString(i);
                                bean.yanqiliusu = arr_yanqiliusu.getString(i);
                                bean.yanqiwendu = arr_yanqiwendu.getString(i);
                                bean.yanqiyali = arr_yanqiyali.getString(i);


                                qitibeanlist.add(bean);
                            }
                            table.setData(qitibeanlist);
                        }else {
                            JSONArray arr_pm10 = response.getJSONArray("污水");
                            JSONArray arr_pm25 = response.getJSONArray("化学需氧量");
                            JSONArray arr_andan = response.getJSONArray("氨氮");
                            JSONArray arr_zondan = response.getJSONArray("总氮");
                            JSONArray arr_ph = response.getJSONArray("pH");
                            JSONArray time = response.getJSONArray("time");

                            for(int i = 0 ;i < time.length();i++){
                                ShuiBean bean = new ShuiBean();
                                String ss = time.getString(i);
                                Date date = format1.parse(ss);
                                bean.time = format2.format(date);
                                bean.wushui = arr_pm10.getString(i);
                                bean.xuyangliang = ""+arr_pm25.getString(i);
                                bean.zondan = ""+arr_zondan.getString(i);
                                bean.ph = ""+arr_ph.getString(i);
                                bean.andan = ""+arr_andan.getString(i);

                                beanlist.add(bean);
                            }
                            table.setData(beanlist);
                        }

                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
