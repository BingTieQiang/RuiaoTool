package com.ruiao.tools.ic_card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ramotion.foldingcell.FoldingCell;
import com.ruiao.tools.R;

import java.util.ArrayList;


/**
 * 设备状态
 */
public class IcDeviceStateActivity1 extends AppCompatActivity {

    private Context context;
    private XRecyclerView mRecyclerView;
//    private IcDeviceStateAdapter adapter;
    private ListView listview;
    private ArrayList<Item> items = new ArrayList<>();
    FoldingCellListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_device_state1);
        this.context = this;
        initView();
        initData();
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        adapter = new FoldingCellListAdapter(this, items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,IcCardDetalActivity.class));
            }
        });
    }

    private void initData() {

        for(int i = 0 ;i<10;i++) {
            items.add(new Item());
        }
        adapter.notifyDataSetChanged();

    }




}
