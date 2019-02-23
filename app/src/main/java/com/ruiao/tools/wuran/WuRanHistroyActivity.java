package com.ruiao.tools.wuran;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

public class WuRanHistroyActivity extends AppCompatActivity {
    private int timeType = 1; //时间类型 1 分钟，2十分钟 3小时数据，4日数据
    private int isGas;
    TimePickerView thisTime;
    WuranAdapter adapter = null;
    Date nowTime = new Date();
    Date hourTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000);  //小时数据
    Date dayTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000);    //日数据
    Date minuteTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000); //分钟数据
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Pbdialog dialog; //进度条
    private ArrayList<WuranBean> list;
    private int devtype;//1开头是气，2开头是水
//    @BindView(R.id.tv_id)
//    TextView tvId;
    @BindView(R.id.tv_time)
    TextView tvTime;
//    @BindView(R.id.tv_5)
//    TextView tv5;

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tag_group)
    TagGroup tagGroup;
    @BindView(R.id.tv_1)
    TextView arr1;
    @BindView(R.id.tv_2)
    TextView arr2;
    @BindView(R.id.tv_3)
    TextView arr3;
    @BindView(R.id.tv_4)
    TextView arr4;
    @BindView(R.id.recyclerview1)
    RecyclerView recyclerview;
    private String MonitorID = null; //设备号
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        MonitorID = getIntent().getStringExtra("MonitorID");
        devtype = Integer.parseInt(getIntent().getStringExtra("devtype")); //1开头是气，2开头是水
        setContentView(R.layout.activity_wu_ran_histroy);
        ButterKnife.bind(this);
        initView();
//        initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 1 * 60 * 60 * 1000)), "xiaoshi",1);
    }

    /**
     * @param start
     * @param end
     * @param type  fen shi xiaoshi day
     * @param isGas 1 开头是气分钟数据 2 开头是水 分钟数据 21 水设备 非分钟设备 11气设备 非分钟数据
     */
    private void initData(String end, String start, String type, final int isGas) {
        dialog = showdialog(context, "正在加载");
        RequestParams pa = new RequestParams();
        pa.add("start", start);
        pa.add("end", end);
        pa.add("type", type);
        pa.add("MonitorID", MonitorID);

        AsynHttpTools.httpGetMethodByParams(URLConstants.IC, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFinish() {
                super.onFinish();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // list 按照时间排列

//                public String QMyanchenliuliang; //烟尘流量
//                public String QMyancennongdu; //烟尘浓度
//                public String QMyancenzhesuan; //烟尘折算
//                public String QMeryanghualiu; //二氧化硫
//                public String QMeryanghualiuzhesuan; //二氧化硫折算
//                public String QMdanyang; //氮氧化物
//                public String QMdanyangzhesuan; //氮氧化物折算
                ArrayList<WuranBean> beans = new ArrayList<>();
                try {
                    Boolean status = response.getBoolean("success");
                    if (status) {

                        if (isGas == 1) {
                            JSONObject tem = null;
                            JSONArray yancengliuliang = response.getJSONArray("流量");
                            JSONArray yanchennongdu = response.getJSONArray("烟尘");
                            JSONArray yanchenzhesuan = response.getJSONArray("烟尘折算");
                            JSONArray so2 = response.getJSONArray("二氧化硫");
                            JSONArray so2zhesuan = response.getJSONArray("二氧化硫折算");
                            JSONArray andan = response.getJSONArray("氮氧化物");
                            JSONArray andanzhesuan = response.getJSONArray("氮氧化物折算");

                            for (int i = 0; i < yancengliuliang.length(); i++) {
                                WuranBean bean = new WuranBean();
                                bean.devtype = isGas;
                                tem = yancengliuliang.getJSONObject(i);
                                bean.QMyanchenliuliang = "" + tem.getDouble("value");
                                bean.date = "" + tem.getString("id");

                                tem = yanchennongdu.getJSONObject(i);
                                bean.QMyancennongdu = "" + tem.getDouble("value");

                                tem = yanchenzhesuan.getJSONObject(i);
                                bean.QMyancenzhesuan = "" + tem.getDouble("value");

                                tem = andan.getJSONObject(i);
                                bean.QMdanyang = "" + tem.getDouble("value");

                                tem = andanzhesuan.getJSONObject(i);
                                bean.QMdanyangzhesuan = "" + tem.getDouble("value");

                                tem = so2.getJSONObject(i);
                                bean.QMeryanghualiu = "" + tem.getDouble("value");

                                tem = so2zhesuan.getJSONObject(i);
                                bean.QMeryanghualiuzhesuan = "" + tem.getDouble("value");
                                bean.id = i;
                                beans.add(bean);
                            }

                        } else if (isGas == 2) {

                            JSONObject tem = null;
                            JSONArray liuiang = response.getJSONArray("流量");
                            JSONArray cod = response.getJSONArray("COD");
                            JSONArray nh3n = response.getJSONArray("氨氮");
                            JSONArray zonlin = null;
                            if(response.has("总磷")){
                                 zonlin = response.getJSONArray("总磷");
                            }
                            JSONArray zongdan = null;
                            if(response.has("总氮")){
                                zongdan = response.getJSONArray("总氮");
                            }

                            for (int i = 0; i < liuiang.length(); i++) {
                                WuranBean bean = new WuranBean();
                                bean.devtype = isGas;
                                tem = liuiang.getJSONObject(i);
                                bean.SMliuliang = "" + tem.getDouble("value");
                                bean.date = "" + tem.getString("id");

                                tem = cod.getJSONObject(i);
                                bean.SMcod = "" + tem.getDouble("value");

                                tem = nh3n.getJSONObject(i);
                                bean.SMnh3n = "" + tem.getDouble("value");
                                if(response.has("总磷")){
                                    tem = zonlin.getJSONObject(i);
                                    bean.SMzonlin = "" + tem.getDouble("value");
                                }

                                if(response.has("总氮")){
                                    tem = zongdan.getJSONObject(i);
                                    bean.SMzondan = "" + tem.getDouble("value");
                                }


                                bean.id = i;
                                beans.add(bean);
                            }

                        } else if (isGas == 21) {
                            /*
                            //21 水设备 非分钟设备，
                                public String liuLiangPinJun;  //流量平均值
                                public String NH3N;    //氨氮
                                public String CODPinJun;   //COD平均值
                                public String zonlin;  //总磷
                                public String zondan;   //总氮
                             */
                            JSONObject tem = null;
                            JSONArray liuliangpingjun = response.getJSONArray("流量平均值");
                            JSONArray andan = response.getJSONArray("氨氮平均值");
                            JSONArray codpingjun = response.getJSONArray("COD平均值");

                            JSONArray zonlin = null;
                            JSONArray zongdan = null;
                            String base = (String)SPUtils.get(context,"BASE","");
                            if(!base.startsWith("http://222.222.220.218")){      //晋州无此数据
                                 zonlin = response.getJSONArray("总磷平均值");
                                 zongdan = response.getJSONArray("总氮平均值");
                            }

                            for (int i = 0; i < liuliangpingjun.length(); i++) {
                                WuranBean bean = new WuranBean();
                                bean.devtype = isGas;
                                tem = liuliangpingjun.getJSONObject(i);
                                bean.liuLiangPinJun = "" + tem.getDouble("value");
                                bean.date = "" + tem.getString("id");

                                tem = andan.getJSONObject(i);
                                bean.andanPingjun = "" + tem.getDouble("value");

                                tem = codpingjun.getJSONObject(i);
                                bean.CODPinJun = "" + tem.getDouble("value");

                                if(!base.startsWith("http://222.222.220.218")){      //晋州无此数据
                                    tem = zonlin.getJSONObject(i);
                                    bean.zonlinPingJun = "" + tem.getDouble("value");
                                    tem = zongdan.getJSONObject(i);
                                    bean.zondanPingJun = "" + tem.getDouble("value");
                                }


                                bean.id = i;
                                beans.add(bean);
                            }

                        } else if (isGas == 11) {
                                /*
                                public String yanCenLiuLiang;  //烟气流量
                                public String yanCenNongDu;   //烟尘浓度
                                public String yanCenZheSuan;   //烟尘折算
                                public String so2;           //二氧化硫"
                                public String so2ZheSuan;    //二氧化硫折算
                                public String dan;            //氮氧化物
                                public String danZheSuan;     //氮氧化物折算
                                 */
                            JSONObject tem = null;
                            JSONArray yanchenliuliang = response.getJSONArray("烟气流量平均值");
                            JSONArray yancennongdu = response.getJSONArray("烟尘平均值");
                            JSONArray yanchenzhesuan = response.getJSONArray("烟尘折算平均值");
                            JSONArray eryanghualiu = response.getJSONArray("二氧化硫平均值");
                            JSONArray eryanghualiuzhesuan = response.getJSONArray("二氧化硫折算平均值");
                            JSONArray danyanghuawu = response.getJSONArray("氮氧化物平均值");
                            JSONArray danyanghuawuzhesuan = response.getJSONArray("氮氧化物折算平均值");
                            for (int i = 0; i < yanchenliuliang.length(); i++) {
                                WuranBean bean = new WuranBean();
                                bean.devtype = isGas;
                                tem = yanchenliuliang.getJSONObject(i);
                                bean.yanCenLiuLiang = "" + tem.getDouble("value");
                                bean.date = "" + tem.getString("id");

                                tem = yancennongdu.getJSONObject(i);
                                bean.yanCenNongDu = "" + tem.getDouble("value");

                                tem = yanchenzhesuan.getJSONObject(i);
                                bean.yanCenZheSuan = "" + tem.getDouble("value");

                                tem = eryanghualiu.getJSONObject(i);
                                bean.so2 = "" + tem.getDouble("value");

                                tem = eryanghualiuzhesuan.getJSONObject(i);
                                bean.so2ZheSuan = "" + tem.getDouble("value");

                                tem = danyanghuawu.getJSONObject(i);
                                bean.dan = "" + tem.getDouble("value");

                                tem = danyanghuawuzhesuan.getJSONObject(i);
                                bean.danZheSuan = "" + tem.getDouble("value");
                                bean.id = i;
                                beans.add(bean);
                            }
                        }
                        list.clear();
                        list.addAll(beans);
                        adapter.setDate(beans);
                    } else {
                        ToastHelper.shortToast(context, response.getString("message"));
                    }
                } catch (JSONException e) {
                    ToastHelper.shortToast(context, "数据解析错误");
                    //JSON数据格式错误
                    e.printStackTrace();
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


        });
    }

    private void initView() {
        StatusBarUtil.darkMode(this);
        list = new ArrayList<>();
//        adapter = new WuranAdapter(context);
        adapter = new WuranAdapter(context,tvTime,arr1,arr2,arr3,arr4);
        recyclerview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        isGas = devtype==1? 1:2;
        tagGroup.setTags(new String[]{"分钟数据","十分钟数据","小时数据","日数据"});
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                switch (tag) {
                    case "分钟数据":
                        timeType = 1;
                        isGas = devtype==1? 1:2;
                        showTimer();
                        break;
                    case "十分钟数据":
                        timeType = 2;
                        isGas = devtype==1? 11:21;
                        showTimer();
                        break;
                    case "小时数据":
                        timeType = 3;
                        isGas = devtype==1? 11:21;
                        showTimer();
                        break;
                    case "日数据":
                        timeType = 4;
                        isGas = devtype==1? 11:21;
                        showTimer();
                        break;
                }
            }
        });
//        ToastHelper.shortToast(context,"请点击时间类型,开始请求相关数据");
        showTimer();
    }

    @OnClick(R.id.time)
    public void onViewClicked() {
        showTimer();
    }
    public void showTimer(){


        TimePickerBuilder builder = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if ((date.getTime()) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                    ToastHelper.shortToast(context, "此时间段无数据");
                    return;
                } else {

                    switch (timeType){
                        case 1:  //分钟
                            initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 1 * 60 * 60 * 1000)), "fen", isGas);
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日HH时 分钟数据");
                            tvEndTime.setText(format1.format(date));
                            break;
                        case 2: //十分钟
                            initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 1 * 60 * 60 * 1000)), "shi", isGas);
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日HH时 十分钟数据");
                            tvEndTime.setText(format2.format(date));
                            break;
                        case 3: //小时
                            initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 24 * 60 * 60 * 1000)), "xiaoshi", isGas);
                            SimpleDateFormat format3 = new SimpleDateFormat("yyyy年MM月dd日全天小时数据");
                            tvEndTime.setText(format3.format(date));
                            break;
                        case 4: //天
                            initData(format.format(hourTime), format.format(new Date(hourTime.getTime() - 7* 24 * 60 * 60 * 1000)), "tian", isGas);
                            SimpleDateFormat format4 = new SimpleDateFormat("yyyy年MM月dd日 一周日数据");
                            tvEndTime.setText(format4.format(date));
                            break;
                    }
                }

            }
        });
        switch (timeType){
            case 1:  //分钟
                builder.setType(new boolean[]{true, true, true, true, false, false});
                break;
            case 2: //十分钟
                builder.setType(new boolean[]{true, true, true, true, false, false});
                break;
            case 3: //小时
                builder.setType(new boolean[]{true, true, true, false, false, false});
                break;
            case 4: //天
                builder.setType(new boolean[]{true, true, true, false, false, false});
                break;
        }
        builder.setTitleText("请选择时间");

        thisTime = builder.build();
        thisTime.show();

    }
    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }


    public void changeData(View view) {
        if(true){
            if(adapter!=null){
                adapter.switchData();
            }
        }
    }

    public void chart(View view) {
        if(list.size()!=0){
            Intent intent = new Intent(context,WuRanChartActivity.class);
            intent.putExtra("list",(Serializable)list);
            intent.putExtra("timeType",timeType);
            startActivity(intent);
        }else {
            ToastHelper.shortToast(context,"无数据,请点击时间类型请求数据");
        }

    }
}
