package com.ruiao.tools.the;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.dongtaiguankong.TaskBean;
import com.ruiao.tools.dongtaiguankong.TaskDetailActivity;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.Pbdialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2019/3/1.
 */

public class Baojing extends Fragment implements PageChangeLinster{


    ArrayList<QiYeBean> qiYeBeans = new ArrayList<>();

    private Pbdialog dialog; //进度条
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1389){


                upData();
            }

        }
    };
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");   //2019-02-26 10:00
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
    Context mContext = getActivity();
    DataAdapter adapter = null;
    ArrayList<TaskBean> mDataList = new ArrayList<>();
    @BindView(R.id.listview)
    XRecyclerView list_task;
    @BindView(R.id.tv_quyu)
    TextView tvArea;
    @BindView(R.id.tv_leibie)
    TextView tvLeibie;
    @BindView(R.id.tv_com)
    TextView tvCom;
    @BindView(R.id.ll_serch)
    LinearLayout llSerch;
    @BindView(R.id.tv_sousuotiaojian)
    TextView tvSousuotiaojian;
    @BindView(R.id.tv_leixing)
    TextView tvLeixing;
    @BindView(R.id.tv_yujingleixing)
    TextView tvYuJingLeiXin;
    @BindView(R.id.tv_yujing1)
    TextView tvYuJingLeiXin1;
    Unbinder unbinder;
    private int Page = 1;
    private int  type = 0; //报警类型

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_notice1, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        qiYeBeans.clear();
        qiYeBeans.add(new QiYeBean("昌兴五金丝网制品厂","3","东里庄镇","金属制品业"));
        qiYeBeans.add(new QiYeBean("石家庄宝康生物工程有限公司","9","工业路循环经济园区","化学原料和化学制品业"));
        qiYeBeans.add(new QiYeBean("晋州市中兴装饰用布有限公司","10","工业路循环经济园区","印染业"));
        qiYeBeans.add(new QiYeBean("晋州市盖鑫医药中间体有限公司","11","工业路循环经济园区","化学原料和化学制品业"));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list_task.refresh();
    }

    private void initView() {
//        adapter = new DataAdapter(mContext);
//        recyclerview.setAdapter(adapter);

        adapter = new DataAdapter(getContext(), mDataList);
        list_task.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        list_task.setLayoutManager(layoutManager);
        //设置分割符号

        //禁止加载更多
        list_task.setLoadingMoreEnabled(false);

        list_task.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                upData();
            }

            @Override
            public void onLoadMore() {
                upData();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("onHiddenChanged",""+hidden);

        list_task.refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    public void upData() {
        RequestParams pa = new RequestParams();
        TheActivity activity = (TheActivity)getActivity() ;
        if(!activity.id.isEmpty()){
            pa.put("DevID", activity.id);
        }
        if(type != 0){
            pa.add("AlarmType", ""+type);
        }
        pa.add("username", (String) SPUtils.get(getContext(), "username1", ""));
        HttpUtil.get(URLConstants.WARNDATA,pa,new HttpUtil.SimpJsonHandle(getContext()){
            @Override
            public void onStart() {
                super.onStart();

            }
            @Override
            public void onFinish() {
                super.onFinish();
                list_task.refreshComplete();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mDataList.clear();
                try {
                    if( response.getBoolean("success")){
                        JSONArray arr = response.getJSONArray("报警数据");
                        JSONObject temp;
                        for(int i = 0;i<arr.length();i++){
                            TaskBean bean = new TaskBean();
                            temp = arr.getJSONObject(i);
//                            bean.id =  temp.getString("id");
                            bean.address =  temp.getString("监测位置");
                            bean.people = temp.getString("企业名称");  //企业名称
                            bean.time =  temp.getString("报警时间");
                            bean.status =  temp.getString("报警类型");  //报警类型
//                            bean.car =  temp.getString("car");
                            bean.context = temp.getString("报警说明");  //报警说明
                            mDataList.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        ToastHelper.shortToast(getContext(),response.getString("message"));
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void setInfo(String id) {

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                dialog = showdialog(getContext(), "正在加载.......");
//                handler.sendEmptyMessage(1389);
//            }
//        }, 1500); //指定启动定时器5s之后运行定时器任务run方法，并且只运行一次



    }

    private class DataAdapter extends RecyclerView.Adapter {
        private Context context;
        private LayoutInflater mLayoutInflater;
        private ArrayList<TaskBean> mDataList;

        public DataAdapter(Context context1, ArrayList<TaskBean> mDataList) {
            mLayoutInflater = LayoutInflater.from(context1);
            this.context = context1;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.tasklist, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            ViewHolder viewHolder = (ViewHolder) holder;
            try {
                Date date=sdf.parse(mDataList.get(i).time);
                viewHolder.time.setText(sdf2.format(date));
            } catch (ParseException e) {

                e.printStackTrace();
            }

//            viewHolder.type.setText(mDataList.get(i).status);
            viewHolder.address.setText(mDataList.get(i).people);
            viewHolder.time.setText(mDataList.get(i).time);
            viewHolder.type.setText(mDataList.get(i).status);
            viewHolder.miaoshu.setText(mDataList.get(i).context);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskBean bean = (TaskBean) mDataList.get(i);
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtra("task",bean);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {

            return mDataList.size();

        }


        private class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView ico;
            private TextView type;
            private TextView time;
            private TextView address;
            private TextView miaoshu;


            public ViewHolder(View itemView) {
                super(itemView);
                time = (TextView) itemView.findViewById(R.id.time);
                address = (TextView) itemView.findViewById(R.id.address);
                miaoshu = (TextView) itemView.findViewById(R.id.miaoshu);
                type = (TextView) itemView.findViewById(R.id.type);
            }
        }
    }
    @OnClick({R.id.ll_quyu, R.id.ll_leibie, R.id.ll_com, R.id.txt_setting,R.id.ll_yujingleixing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_quyu:
                quyu();
                break;
            case R.id.ll_leibie:
                leixing();
                break;
            case R.id.ll_com:
                qiye();
                break;
            case R.id.txt_setting:
                if (llSerch.getVisibility() == View.GONE) {
                    llSerch.setVisibility(View.VISIBLE);

                } else if (llSerch.getVisibility() == View.VISIBLE) {
                    llSerch.setVisibility(View.GONE);
                    submit();
                }
                break;
            case R.id.ll_yujingleixing:
                yujingleixing();
                break;
        }
    }

    private void yujingleixing() {
        ArrayList<String> listxx = new ArrayList<>();
        listxx.add("全部类型");
        listxx.add("指标超限报警");
        listxx.add("设备离线报警");
        listxx.add("设备故障");
        listxx.add("违规生产报警");


        OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvYuJingLeiXin.setText(listxx.get(options1));
                tvYuJingLeiXin1.setText(listxx.get(options1));
                type = options1;

            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {


            }
        }).build();


        view.setPicker(listxx);
        view.show();
    }

    private void submit() {
        list_task.refresh();
    }
    ArrayList<String> qiye_list = new ArrayList<>();
    ArrayList<String> id_list = new ArrayList<>();
    private void qiye() {
        id_list.clear();
        qiye_list.clear();
        for (int i=0;i<qiYeBeans.size();i++){
            QiYeBean bean = qiYeBeans.get(i);
            if(tvArea.getText().equals("全部") && tvLeibie.getText().equals("全部")){
                qiye_list.add(bean.name);
                id_list.add(bean.devid);
            }else if(!tvArea.getText().equals("全部") && tvLeibie.getText().equals("全部")){
                if(bean.quyu.equals(tvArea.getText())  ){
                    qiye_list.add(bean.name);
                    id_list.add(bean.devid);
                }
            }else if(tvArea.getText().equals("全部") && !tvLeibie.getText().equals("全部")){
                if( bean.leixing.equals(tvLeibie.getText()) ){
                    qiye_list.add(bean.name);
                    id_list.add(bean.devid);
                }
            }else if(!tvArea.getText().equals("全部") && !tvLeibie.getText().equals("全部")){
                if(bean.quyu.equals(tvArea.getText()) && bean.leixing.equals(tvLeibie.getText()) ){
                    qiye_list.add(bean.name);
                    id_list.add(bean.devid);
                }
            }


        }
        OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                tvCom.setText(qiye_list.get(options1));
                TheActivity activity = (TheActivity)getActivity() ;
                activity.id = id_list.get(options1);

            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {


            }
        }).build();


        view.setPicker(qiye_list);
        view.show();
    }

    private void quyu() {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        list.add("东里庄镇");
        list.add("工业路循环经济园区");
        list.add("晋州镇");
        list.add("小樵镇");
        list.add("马于镇");
        list.add("桃园镇");
        list.add("总十庄镇");
        list.add("东卓宿镇");
        list.add("槐树镇");
        list.add("营里镇");
        list.add("周家庄乡");

        OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvSousuotiaojian.setText("区域："+list.get(options1));
                tvArea.setText(list.get(options1));

            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {


            }
        }).build();


        view.setPicker(list);
        view.show();
    }
    private void leixing() {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        list.add("印染业");
        list.add("金属制品业");
        list.add("非金属矿物制品业");
        list.add("化学原料和化学制品业");
        list.add("橡胶和塑料制品业");
        list.add("电力，热力生产和工业业");

        OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvLeixing.setText("类别："+list.get(options1));
                tvLeibie.setText(list.get(options1));

            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {


            }
        }).build();


        view.setPicker(list);
        view.show();
    }
    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }
}
