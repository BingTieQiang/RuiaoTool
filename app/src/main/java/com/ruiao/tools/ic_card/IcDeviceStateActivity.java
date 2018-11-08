package com.ruiao.tools.ic_card;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ramotion.foldingcell.FoldingCell;
import com.ruiao.tools.R;
import com.ruiao.tools.notice.NoticeBean;
import com.ruiao.tools.ui.fragment.maintab.NoticeFragment;

import java.util.ArrayList;

import static com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView.BallSpinFadeLoader;
import static com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView.LineSpinFadeLoader;

/**
 * 设备状态
 */
public class IcDeviceStateActivity extends AppCompatActivity {
    private Context context;
    private XRecyclerView mRecyclerView;
    private IcDeviceStateAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_device_state);
        this.context = this;
        initView();
        initData();
    }

    private void initData() {
        adapter.setDate();
    }

    private void initView() {
        adapter = new IcDeviceStateAdapter(context);

        mRecyclerView = (XRecyclerView) findViewById(R.id.xrecycle);
        LinearLayoutManager layoutManager =  new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.loadMoreComplete();
//        mRecyclerView.refreshComplete();
//        mRecyclerView.refresh();
        mRecyclerView.setRefreshProgressStyle(BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(LineSpinFadeLoader);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
            }

            @Override
            public void onLoadMore() {
                // load more data here
            }
        });
        adapter.setOnItemClickListener(new IcDeviceStateAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                //点击相应的item进入相关设备的详细
////                startActivity(new Intent(context,IcCardDetalActivity.class));
//
//            }

            @Override
            public void onClick(int position, FoldingCell cell) {
                cell.toggle(false);
            }

            @Override
            public void onLongClick(int position) {

            }
        });



    }

}
