package com.ruiao.tools.danyanghuawu;

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
//分钟数据
public class DanyangFenzhongActivity extends AppCompatActivity {
    Date data = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH 时");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("HH时mm分");
    TimePickerView thisTime;
    //氮氧化物，按照天查询
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;       //当前时间
    ArrayList<DanYanghuawuBean> beanlist = new ArrayList<>();
    long time_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danyang_fen);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        tvNewDate.setText(format.format(data));
        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowTableTitle(false);
        long zero =  System.currentTimeMillis()- ( System.currentTimeMillis()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1000 * 3600 * 24;
        time_num = zero;
        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
    }

    @OnClick({R.id.tv_pre, R.id.tv_after,R.id.tv_newDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pre:
                time_num = time_num - 1000 * 3600 * 1;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                break;
            case R.id.tv_after:
                time_num = time_num + 1000 * 3600 * 1;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                break;
            case R.id.tv_newDate:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time_num = date.getTime() - ( date.getTime()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
                        tvNewDate.setText( format.format(new Date(time_num)));
                        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                    }
                }).setType(new boolean[]{true, true, true, true, false, false}).setTitleText("请选择时间").build();
                thisTime.show();

                break;
        }
    }

    private void initChart(String start_time,String end_time) {
        RequestParams pa = new RequestParams();
        pa.add("DevID", "1");
        pa.add("start", start_time);
        pa.add("end", end_time);
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
                    beanlist.clear();
                    if (response.getBoolean("success")) {
                        JSONArray danyanghuawu = response.getJSONArray("danyanghuawu");
                        JSONArray o2 = response.getJSONArray("o2");
                        JSONArray zhesuandanyang = response.getJSONArray("zhesuandanyang");
                        JSONArray no2 = response.getJSONArray("no2");
                        JSONArray no = response.getJSONArray("no");
                        JSONArray arr_time = response.getJSONArray("time");
                        for(int i = 0 ;i < arr_time.length();i++){
                            DanYanghuawuBean bean = new DanYanghuawuBean();
                            String ss  =  arr_time.getString(i);
                            Date date = format1.parse(ss);
                            bean.time = format2.format(date);
                            bean.danyang = ""+danyanghuawu.getDouble(i);
                            bean.o2 = ""+o2.getDouble(i);
                            bean.zhesuandangyang = ""+zhesuandanyang.getDouble(i);
                            bean.no2 = ""+no2.getDouble(i);
                            bean.no =""+ no.getDouble(i);
                            beanlist.add(bean);
                        }
                        table.setData(beanlist);
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
