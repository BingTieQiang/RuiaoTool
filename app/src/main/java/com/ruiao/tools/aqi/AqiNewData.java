package com.ruiao.tools.aqi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ruiao.tools.R;
import com.ruiao.tools.dongtaiguankong.TaskBean;
import com.ruiao.tools.dongtaiguankong.TaskDetailActivity;
import com.ruiao.tools.the.Baojing;
import com.ruiao.tools.ui.base.BaseFragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

public class AqiNewData extends BaseFragment {
    ArrayList<AqiBean> list = new ArrayList<>();
    @BindView(R.id.listview)
    XRecyclerView list_task;
    DataAdapter adapter = null;
    @Override
    public void getmsg() {

    }

    @Override
    protected int getContentViewID() {
         return R.layout.fragment_aqinewdata;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        adapter = new DataAdapter(getContext(), list);
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

    private void upData() {
        list.add(new AqiBean());
        list.add(new AqiBean());
        list.add(new AqiBean());
        list.add(new AqiBean());
        list.add(new AqiBean());
        list.add(new AqiBean());
        adapter.notifyDataSetChanged();
    }

    private class DataAdapter extends RecyclerView.Adapter {
        private Context context;
        private LayoutInflater mLayoutInflater;
        ArrayList<AqiBean>  mDataList;

        public DataAdapter(Context context1, ArrayList<AqiBean> mDataList) {
            mLayoutInflater = LayoutInflater.from(context1);
            this.context = context1;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_aqinewdata, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            ViewHolder viewHolder = (ViewHolder) holder;

//            viewHolder.address.setText(mDataList.get(i).people);
//            viewHolder.time.setText(mDataList.get(i).time);
//
//            viewHolder.miaoshu.setText(mDataList.get(i).context);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    TaskBean bean = (TaskBean) mDataList.get(i);
//                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
//                    intent.putExtra("task",bean);
//                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {

            return mDataList.size();

        }


        private class ViewHolder extends RecyclerView.ViewHolder {


            private TextView aqi;
            private TextView time;
            private TextView address;
            private TextView miaoshu;


            public ViewHolder(View itemView) {
                super(itemView);
//                time = (TextView) itemView.findViewById(R.id.time);
//                address = (TextView) itemView.findViewById(R.id.address);
//                miaoshu = (TextView) itemView.findViewById(R.id.miaoshu);
//                aqi = (TextView) itemView.findViewById(R.id.type);
            }
        }
    }

}
