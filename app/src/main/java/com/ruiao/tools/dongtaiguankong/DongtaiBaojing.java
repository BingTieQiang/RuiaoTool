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
import com.ruiao.tools.gongdiyangceng.GongdiBaojingActivity;
import com.ruiao.tools.gongdiyangceng.GongdiBaojingBean;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.ToastHelper;

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
import butterknife.Unbinder;

public class DongtaiBaojing extends Fragment implements Serializable{
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DataAdapter adapter;
    Unbinder unbinder;
    @BindView(R.id.listview)
    XRecyclerView listview;
    ArrayList<TaskBean> list = new ArrayList();
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
        HttpUtil.get("http://lulibo.club/baojing/", pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                            bean.context = obj.getString("context");
                            bean.people = obj.getString("people");
                            bean.type = obj.getString("type");
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
            ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_dongtaibaojing, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.time.setText(mDataList.get(i).time);
            viewHolder.context.setText(mDataList.get(i).context);
            viewHolder.company.setText(mDataList.get(i).company);
//            viewHolder.point.setText(mDataList.get(i).point);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskBean bean = mDataList.get(i);
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
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
            private TextView type;    //进度
            private TextView context;  //内容
            private TextView company;  //公司
            private TextView point;  //检测点位


            public ViewHolder(View itemView) {
                super(itemView);
                time = (TextView) itemView.findViewById(R.id.tv_message_time);
                context = (TextView) itemView.findViewById(R.id.tv_message_title);
                company = (TextView) itemView.findViewById(R.id.tv_message_context);



            }
        }
    }


    public static class TaskBean implements Serializable{
        public String time;
        public String company;
        public String context; //项目
        public String type; //状态
        public String people; //运维人员
    }

}
