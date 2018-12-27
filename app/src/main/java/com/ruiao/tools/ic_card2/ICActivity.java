package com.ruiao.tools.ic_card2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
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
import com.ruiao.tools.wuran.WuRangActivity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

/**
 * IC卡设备总览页面
 */
public class ICActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> dev_adapter;
    ArrayList<String> fac_list = new ArrayList<>();
    ArrayList<ArrayList<String>> dev_list = new ArrayList<>();

    @BindView(R.id.tag_group)
    TagGroup tagGroup;
    private Pbdialog dialog; //进度条
    @BindView(R.id.fac_name)
    TextView facName;
    @BindView(R.id.dev_name)
    TextView devName;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;
    @BindView(R.id.tv_9)
    TextView tv9;
    @BindView(R.id.tv_10)
    TextView tv10;
    @BindView(R.id.tv_p)
    TextView tvP;
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.tv_8)
    TextView tv8;
    @BindView(R.id.tv_n)
    TextView tvN;
    private boolean ready = false;
    private boolean isDataReady = false;
    ArrayList<ArrayList<DevBean>> biglist = new ArrayList<>();
    ArrayList<FactroyBean> little = new ArrayList<>();
    Date nowTime = new Date();
    @BindView(R.id.cod)
    TextView cod;
    @BindView(R.id.ll_ll2)
    LinearLayout ll_ll2;
    @BindView(R.id.nh3n)
    TextView nh3n;
    @BindView(R.id.famen_type)
    TextView famenType;
    @BindView(R.id.famen_stata)
    TextView famenStata;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatSor = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date hourTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000);  //小时数据
    private int type;   //0日数据 1小时数据 2分钟数据
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.lineChart)
    LineChart barChart1;

    private LineChartManager lineChartManager;
    private String[] arr = {"COD", "COD排放量", "NH₃N", "NH₃N排放量"};
    BarChartManager barChartManager1;
    //设置x轴的数据
    final ArrayList<Float> xValues = new ArrayList<>();
    final List<List<Float>> yValues = new ArrayList<>();
    OptionsPickerView builder = null;
    private String MonitorID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic);
        ButterKnife.bind(this);
        initView();
//        initNowData();
//        initData();
        initFacDev();
    }

    /**
     * 污染源           1
     * 工地扬尘        2
     * 重点园区监测 3
     * 水质自动站    4
     * VOC监测      5
     * 厂界监测       6
     * 空气自动站    8
     * IC卡总量控制 12
     * 空气微型站    14
     * 油烟在线监测 15
     */

    private void initFacDev() {
        dialog = showdialog(this, "正在加载.......");
        RequestParams pa = new RequestParams();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fac_list);
        pa.add("username", (String) SPUtils.get(ICActivity.this, "username", ""));
        pa.add("SID", "12");
        AsynHttpTools.httpGetMethodByParams(URLConstants.FAC, pa, new JsonHttpResponseHandler("GB2312") {
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
                finish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                fac_list.clear();
                try {
                    Boolean status = response.getBoolean("success");
                    if (status) {
                        JSONArray arr = response.getJSONArray("factory");
                        JSONObject fac = null;
                        JSONArray devs = null;
                        JSONObject dev = null;
                        for (int i = 0; i < arr.length(); i++) {
                            fac = arr.getJSONObject(i);
                            String facname = fac.getString("name"); //工厂名字

                            fac_list.add(""+i+" "+facname);

                            little.add(new FactroyBean(facname));
                            devs = fac.getJSONArray("device");

                            ArrayList<String> temp = new ArrayList<>();
                            ArrayList<DevBean> mlist = new ArrayList<>();
                            for (int j = 0; j < devs.length(); j++) {
                                dev = devs.getJSONObject(j);
                                String devNane = dev.getString("name");
                                String devId = dev.getString("id");

                                mlist.add(new DevBean(devNane, devId));

                                temp.add(devNane);
                            }
                            dev_list.add(temp);
                            biglist.add(mlist);
                        }
                        ready = true;
                        initPickerView();

                    } else {
                        ToastHelper.shortToast(ICActivity.this, response.getString("message"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initNowData(String MonitorID) {
        RequestParams pa = new RequestParams();
//        pa.add("start", format.format(new Date(hourTime.getTime() - 2 * 60 * 1000)));
//        pa.add("end", format.format(hourTime));
//        pa.add("type", "fen");
        pa.add("MonitorID", MonitorID);
//        pa.add("username", (String)SPUtils.get(ICActivity.this,"username",""));
        AsynHttpTools.httpGetMethodByParams(URLConstants.NEW_IC, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int code, Header[] heads, Throwable throwable, JSONObject json) {

                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        tvNewDate.setText("最新数据：刷新时间:" + response.getString("MonitorTime")+"(点击切换企业)");
                        String base = (String)SPUtils.get(ICActivity.this,"BASE","");
                        if(base.startsWith("http://222.222.220.218")){   //"晋州"
                            tv9.setText("COD排放量");
                            tv10.setText("NH3N排放量");
                            cod.setText("" + response.getDouble("COD"));
                            nh3n.setText("" + response.getDouble("NH3N"));
                            famenStata.setText("" + response.getString("阀门状态"));
                            famenType.setText("" + response.getString("阀门控制方式"));
                            tvP.setText("" + response.getDouble("COD排放量"));
                            tvN.setText("" + response.getDouble("NH3N排放量"));
                            tv7.setText("" + response.getDouble("流量"));
                            tv8.setText("" + response.getDouble("NH3N排放量"));
                        }else{
                            cod.setText("" + response.getDouble("COD"));
//                        cods.setText("" + response.getDouble("COD排放量"));
                            nh3n.setText("" + response.getDouble("NH3N"));
//                        nh3ns.setText("" + response.getDouble("NH3N排放量"));

                            famenStata.setText("" + response.getString("阀门状态"));
                            famenType.setText("" + response.getString("阀门控制方式"));
                            tvN.setText("" + response.getDouble("总氮"));
//                        tvNs.setText("" + response.getDouble("总氮排放量"));
                            tvP.setText("" + response.getDouble("总磷"));
//                        tvPs.setText("" + response.getDouble("总磷排放量"));
                        }



                    } else {
                        ToastHelper.shortToast(ICActivity.this, response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    private void initData(String MonitorID) {
        dialog = showdialog(this, "正在加载.......");
        RequestParams pa = new RequestParams();
        pa.add("start", format.format(new Date(hourTime.getTime() - 12 * 60 * 60 * 1000)));
        pa.add("end", format.format(hourTime));
        pa.add("type", "xiaoshi");
        pa.add("MonitorID", MonitorID);
        pa.add("username", (String) SPUtils.get(ICActivity.this, "username", ""));
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
                int address = 0; //1为晋州
                // list 按照时间排列
                ArrayList<IcBean1> beans = new ArrayList<>();
                try {
                    String base = (String)SPUtils.get(ICActivity.this,"BASE","");
                    if(base.startsWith("http://222.222.220.218")){   //"晋州"
                        address = 1;
                    }
                    lineChartManager.setAddress(address);
                    Boolean status = response.getBoolean("success");
                    if (status) {

                            IcBean1 bean = null;
                            JSONArray codarr = response.getJSONArray("COD");
                            JSONArray codsarr = response.getJSONArray("COD排放量");
                            JSONArray nh3sarr = response.getJSONArray("NH3N");
                            JSONArray nh3ssarr = response.getJSONArray("NH3N排放量");
                            JSONArray totalarr = response.getJSONArray("流量");
                            JSONArray paishuiliang = response.getJSONArray("排水量");

                            JSONArray totalin = null;
                            JSONArray totalinpaifang = null ;
                            JSONArray totaldan = null ;
                            JSONArray totaldanpaifang = null ;
                            if(address != 1){
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
                                if(address != 1)
                                {
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
                        lineChartManager.setData(beans, 2);
                        lineChartManager.setWhich(2, 0); //选定小时显示x轴   0个数据
                        isDataReady = true;
                    } else {
                        ToastHelper.shortToast(ICActivity.this, response.getString("message"));
                    }
                } catch (JSONException e) {
                    ToastHelper.shortToast(ICActivity.this, "数据解析错误");
                    //JSON数据格式错误
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        int address = 0;
        StatusBarUtil.darkMode(this);
        lineChartManager = new LineChartManager(barChart1);
        lineChartManager.initChart();
        String base = (String)SPUtils.get(this,"BASE","");
        if(base.startsWith("http://222.222.220.218")){   //"晋州"
            tv9.setText("COD排放量");
            tv10.setText("NH3N排放量");
            ll_ll2.setVisibility(View.VISIBLE);
            address = 1;
        }else if(base.startsWith("http://222.223.112.252")){  //清河

        }else if(base.startsWith("http://110.249.145.94")){   //宁晋

        }


        if(address == 1){
            tagGroup.setTags(new String[]{"COD", "COD排放量", "NH3N", "NH3N排放量", "流量","排水量"});
        }else {
            tagGroup.setTags(new String[]{"COD", "COD排放量", "NH3N", "NH3N排放量", "总磷", "总磷排放量", "总氮", "总氮排放量", "流量","排水量"});
        }

        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                switch (tag){
                    case "COD":
                        lineChartManager.setWhich(0, type);
                        break;
                    case "COD排放量":
                        lineChartManager.setWhich(1, type);
                        break;
                    case "NH3N":
                        lineChartManager.setWhich(2, type);
                        break;
                    case "NH3N排放量":
                        lineChartManager.setWhich(3, type);
                        break;
                    case "总磷":
                        lineChartManager.setWhich(4, type);
                        break;
                    case "总磷排放量":
                        lineChartManager.setWhich(5, type);
                        break;
                    case "总氮":
                        lineChartManager.setWhich(6, type);
                        break;
                    case "总氮排放量":
                        lineChartManager.setWhich(7, type);
                        break;
                    case "流量":
                        lineChartManager.setWhich(8, type);
                        break;
                    case "排水量":
                        lineChartManager.setWhich(9, type);
                        break;
            }
        }});


    }

    public void startHistroy(View view) {
        Intent intent = new Intent(this, IcHistroyActivity.class);
        if (null == MonitorID) {
            ToastHelper.shortToast(ICActivity.this, "请选择设备");
            return;
        }

        intent.putExtra("MonitorID", MonitorID);
        startActivity(intent);
    }

    /**
     * 初始化工厂，设备  选择器
     */
    private void initPickerView() {
        if (!ready) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_lonch);
        builder.setTitle("选择企业：");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
//                Toast.makeText(MainActivity.this, "你点击了第"+which+"个item", 1000).show();;

                showDev(which);
            }
        });
        builder.show();
    }
    public void showDev(final int bwhich){
        dev_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dev_list.get(bwhich));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_lonch);
        builder.setTitle("选择设备：");
        builder.setAdapter(dev_adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = biglist.get(bwhich).get(which).getId(); //获取设备ID
                MonitorID = id;
                facName.setText(little.get(bwhich).getFacname());
                devName.setText(biglist.get(bwhich).get(which).getDevNane());
//                MonitorID = MonitorID;
                isDataReady = true;
                initNowData(id);
                initData(id);
            }
        });
        builder.show();
    }

    @OnClick(R.id.select)
    public void onViewClicked() {
        initPickerView();
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

}
