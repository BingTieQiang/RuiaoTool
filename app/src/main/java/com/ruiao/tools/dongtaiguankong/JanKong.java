package com.ruiao.tools.dongtaiguankong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.gongdiyangceng.GongdiBaojingBean;
import com.ruiao.tools.gongdiyangceng.GongdiBaojingList;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class JanKong extends Fragment {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Unbinder unbinder;
    DataAdapter adapter = null;
    @BindView(R.id.listview)
    XRecyclerView listview;
    DateFormat bf = new SimpleDateFormat("MM月dd日HH时mm分");
    ArrayList<TaskBean> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dongtai_data, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        adapter = new DataAdapter(getContext(), list);
        listview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        listview.setLayoutManager(layoutManager);
        //设置分割符号

        //禁止加载更多
        listview.setLoadingMoreEnabled(false);
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                upData();
            }

            @Override
            public void onLoadMore() {
                upData();
            }
        });
        listview.refresh();
    }
    private void upData() {
        long time_num = new Date().getTime();
        RequestParams pa = new RequestParams();
        pa.add("username", "15801299706");
        pa.add("end", format1.format(new Date()));
        pa.add("start", format1.format(new Date(time_num - 1000 * 3600 * 24 * 4)));
        list.clear();
        HttpUtil.get("http://lulibo.club/jiankong/", pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                listview.refreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getBoolean("success")) {
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            TaskBean bean = new TaskBean();

                            bean.time = obj.getString("time");
                            bean.company = obj.getString("company");
                            bean.zhandian = obj.getString("zhandian");
                            bean.url = obj.getString("url");
                            bean.type = obj.getString("type");
                            bean.MonitorID = obj.getString("MonitorID");
                            list.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private class DataAdapter extends RecyclerView.Adapter {
        private Context context;
        private LayoutInflater mLayoutInflater;
        ArrayList<TaskBean> mDataList;

        public DataAdapter(Context context1, ArrayList<TaskBean> mDataList) {
            mLayoutInflater = LayoutInflater.from(context1);
            this.context = context1;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           DataAdapter.ViewHolder viewHolder = new DataAdapter.ViewHolder(mLayoutInflater.inflate(R.layout.item_dongtaiguankong, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            DataAdapter.ViewHolder viewHolder = (DataAdapter.ViewHolder) holder;


//            viewHolder.type.setText(mDataList.get(i).type);
            viewHolder.company.setText(mDataList.get(i).company);
            viewHolder.type.setText(mDataList.get(i).zhandian);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskBean bean = mDataList.get(i);
                    Intent intent = new Intent(getContext(), JiankongDetaiActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", bean);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {

            return mDataList.size();

        }


        private class ViewHolder extends RecyclerView.ViewHolder {


            private TextView time;    //时间
            private TextView type;    //报警种类
            private TextView context;  //内容
            private TextView company;  //公司
            private TextView point;  //检测点位


            public ViewHolder(View itemView) {
                super(itemView);

                type = (TextView) itemView.findViewById(R.id.type);
                context = (TextView) itemView.findViewById(R.id.miaoshu);
                company = (TextView) itemView.findViewById(R.id.qiye);
                point = (TextView) itemView.findViewById(R.id.address);


            }
        }
    }

    public static class TaskBean implements Serializable {
        public String time;
        public String type;
        public String MonitorID;
        public String company;
        public String item; //项目
        public String zhandian; //站点
        public String url; //图片地址
    }

}
