package com.ruiao.tools.youyan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.danyanghuawu.DanYanghuawuBean;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
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

public class HistoryShifenActivity extends AppCompatActivity {
    Date data = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH 时");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("dd日HH时mm分");
    TimePickerView thisTime;
    //氮氧化物，按照天查询
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;       //当前时间
    ArrayList<YouyanBean> table_list = new ArrayList<>();
    long time_num;
    String MonitorID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_shifen);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        MonitorID = getIntent().getStringExtra("MonitorID");

        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowTableTitle(false);
        long zero =  System.currentTimeMillis()- ( System.currentTimeMillis()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1000 * 3600 * 24;
        time_num = zero;

        tvNewDate.setText(format.format(new Date(time_num)));
        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
    }
    @OnClick({R.id.tv_pre, R.id.tv_after,R.id.tv_newDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pre:
                time_num = time_num - 1000 * 3600 * 2;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
                break;
            case R.id.tv_after:
                time_num = time_num + 1000 * 3600 * 2;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
                break;
            case R.id.tv_newDate:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time_num = date.getTime() - ( date.getTime()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 2);
                        tvNewDate.setText( format.format(new Date(time_num)));
                        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
                    }
                }).setType(new boolean[]{true, true, true, true, false, false}).setTitleText("请选择时间").build();
                thisTime.show();

                break;
        }
    }

    private void initChart(String start_time,String end_time) {
        RequestParams pa = new RequestParams();
        pa.add("MonitorID", MonitorID);
        pa.add("start", start_time);
        pa.add("end", end_time);
        pa.add("type", "shi");
        HttpUtil.get(URLConstants.YOUYAN_HISTROY, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                    table_list.clear();
                    if (response.getBoolean("success")) {
                        JSONArray array =  response.getJSONArray("data");
                        for(int i = 0; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            YouyanBean bean  = new YouyanBean();
                            bean.nongdu = obj.getString("num");
                            String ss = obj.getString("time");

                            Date date = format1.parse(ss);
                            bean.time = format2.format(date);

                            bean.fengji = obj.getString("fengji");
                            bean.jinghuaqi = obj.getString("jinghuaqi");
                            table_list.add(bean);
                        }
                        table.setData(table_list);
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
