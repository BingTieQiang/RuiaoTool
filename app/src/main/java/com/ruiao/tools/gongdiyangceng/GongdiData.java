package com.ruiao.tools.gongdiyangceng;

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
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.youyan.YouYanHistryActivity;
import com.ruiao.tools.youyan.YouyanGpsBean;
import com.ruiao.tools.youyan.YouyanNewData;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GongdiData extends Fragment  {
    Unbinder unbinder;
    DateFormat bf = new SimpleDateFormat("MM月dd日HH时mm分");
    ArrayList<GongdiNowBean> list = new ArrayList<>();
    @BindView(R.id.listview)
    XRecyclerView list_task;
    DataAdapter adapter = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_gongdi_data, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initData();
    }

    private void initData() {
        adapter = new DataAdapter(getContext(), list);
        list_task.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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
        list_task.refresh();
    }

    private void upData() {
        RequestParams pa = new RequestParams();
        pa.add("username", (String) SPUtils.get(getContext(), "username", ""));
        list.clear();
        HttpUtil.get(URLConstants.GONGDI_NOWDATA, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                list_task.refreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getBoolean("success")) {
                        JSONArray array =  response.getJSONArray("data");
                        for(int i = 0; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            GongdiNowBean bean  = new GongdiNowBean();
                            bean.name = obj.getString("name");
                            bean.time = obj.getString("time");
                            bean.pm10 = obj.getString("pm10");
                            bean.pm25 = obj.getString("pm25");
                            bean.zaosheng = obj.getString("sound");
                            bean.wendu = obj.getString("temp");
                            bean.shidu = obj.getString("shidu");
                            bean.fengsu = obj.getString("fengsu");
                            bean.fengxiang = obj.getString("fengxiang");
                            bean.qiye = obj.getString("qiye");
                            list.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class DataAdapter extends RecyclerView.Adapter {
        private Context context;
        private LayoutInflater mLayoutInflater;
        ArrayList<GongdiNowBean>  mDataList;

        public DataAdapter(Context context1, ArrayList<GongdiNowBean> mDataList) {
            mLayoutInflater = LayoutInflater.from(context1);
            this.context = context1;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            DataAdapter.ViewHolder viewHolder = new DataAdapter.ViewHolder(mLayoutInflater.inflate(R.layout.item_gongdi_newdata, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            DataAdapter.ViewHolder viewHolder = (DataAdapter.ViewHolder) holder;

            viewHolder.time.setText(bf.format(new Date()));
            viewHolder.pm10.setText(mDataList.get(i).pm10);
            viewHolder.pm25.setText(mDataList.get(i).pm25);
            viewHolder.zaosheng.setText(mDataList.get(i).zaosheng);
            viewHolder.wendu.setText(mDataList.get(i).wendu);
            viewHolder.shidu.setText(mDataList.get(i).shidu);
            viewHolder.fengsu.setText(mDataList.get(i).fengsu);
            viewHolder.fengxiang.setText(mDataList.get(i).fengxiang);
            viewHolder.diqu.setText(mDataList.get(i).qiye + mDataList.get(i).name);

//
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    GongdiNowBean bean = mDataList.get(i);
//                    Intent intent = new Intent(getContext(), YouYanHistryActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("bean",bean);
//                    intent.putExtra("bundle",bundle);
//                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {

            return mDataList.size();

        }


        private class ViewHolder extends RecyclerView.ViewHolder {


            private TextView time;
            private TextView pm10;
            private TextView pm25;
            private TextView zaosheng;
            private TextView wendu;
            private TextView shidu;
            private TextView fengsu;
            private TextView fengxiang;
            private TextView diqu;



            public ViewHolder(View itemView) {
                super(itemView);
                time = (TextView) itemView.findViewById(R.id.tv_time);
                pm10 = (TextView) itemView.findViewById(R.id.tv_pm10);
                pm25 = (TextView) itemView.findViewById(R.id.tv_pm25);
                zaosheng = (TextView) itemView.findViewById(R.id.tv_zaosheng);
                wendu = (TextView) itemView.findViewById(R.id.tv_temp);
                shidu = (TextView) itemView.findViewById(R.id.tv_shidu);
                fengsu = (TextView) itemView.findViewById(R.id.tv_fengsu);
                fengxiang = (TextView) itemView.findViewById(R.id.tv_fengxiang);
                diqu = (TextView) itemView.findViewById(R.id.tv_diqu);

            }
        }
    }
}
