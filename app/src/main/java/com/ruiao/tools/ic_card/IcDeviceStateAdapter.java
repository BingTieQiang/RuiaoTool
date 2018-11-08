package com.ruiao.tools.ic_card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.ruiao.tools.R;
import com.ruiao.tools.notice.NoticeBean;

import java.util.ArrayList;

/**
 * Created by ruiao on 2018/5/10.
 */

public class IcDeviceStateAdapter extends RecyclerView.Adapter {
    public interface OnItemClickListener{
        void onClick( int position,FoldingCell cell);
        void onLongClick( int position);
    }
    private OnItemClickListener mOnItemClickListener;
    private LayoutInflater mLayoutInflater;
    protected ArrayList<NoticeBean> mDataList = new ArrayList<>();

    public IcDeviceStateAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

    }
    public void setDate()
    {
        mDataList.clear();

        for ( int i = 0;i<10;i++)
        {
            mDataList.add(new NoticeBean("VOC超标" + i,"voc超标信息:2018年5月8日 14:12:11" + i));
        }

        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_ic_device_state, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
//            viewHolder.title.setText(mDataList.get(position).title);
//            viewHolder.context.setText(mDataList.get(position).msg);
        if( mOnItemClickListener!= null){
            viewHolder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position,((ViewHolder) holder).foldingCell);
                }
            });
            viewHolder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();

    }



    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView context;
        private FoldingCell foldingCell;
        public ViewHolder(View itemView) {
            super(itemView);
//                title = (TextView) itemView.findViewById(R.id.tv_message_title);
//                context = (TextView) itemView.findViewById(R.id.tv_message_context);
            foldingCell = (FoldingCell) itemView.findViewById(R.id.folding_cell);
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
}
