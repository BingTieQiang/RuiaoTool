package com.ruiao.tools.fenbiao;

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
import com.ruiao.tools.the.PageChangeLinster;
import com.ruiao.tools.the.QiYeBean;
import com.ruiao.tools.the.TheActivity;
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

public class FragmentFengbiaoBaojing extends Fragment implements PageChangeLinster {


    ArrayList<QiYeBean> qiYeBeans = new ArrayList<>();

    private Pbdialog dialog; //进度条
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1389) {


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
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_fengbiao_notice, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list_task.refresh();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        list_task.refresh();
    }



    public void upData() {
        RequestParams pa = new RequestParams();


        pa.add("username", (String) SPUtils.get(getContext(), "username1", ""));
        HttpUtil.get(URLConstants.Fenbiao_Baojin, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                    if (response.getBoolean("success")) {
                        JSONArray arr = response.getJSONArray("报警数据");
                        JSONObject temp;
                        for (int i = 0; i < arr.length(); i++) {
                            TaskBean bean = new TaskBean();
                            temp = arr.getJSONObject(i);
//                            bean.id =  temp.getString("id");
                            bean.address = temp.getString("监测位置");
                            bean.people = temp.getString("企业名称");  //企业名称
                            bean.time = temp.getString("报警时间");
                            bean.status = temp.getString("报警类型");  //报警类型
//                            bean.car =  temp.getString("car");
                            bean.context = temp.getString("报警说明");  //报警说明
                            mDataList.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
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
                Date date = sdf.parse(mDataList.get(i).time);
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
                    Intent intent = new Intent(getContext(), FenbiaoDetailActivity.class);
                    intent.putExtra("task", bean);
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




    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }
}
