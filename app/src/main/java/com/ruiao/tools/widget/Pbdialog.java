package com.ruiao.tools.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ruiao.tools.R;


/**
 * Created by Administrator on 2017/5/21.
 */

public class Pbdialog extends AlertDialog {
    private  TextView msg;


    public Pbdialog( Context context) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pbdialog, null);
        msg = (TextView) view.findViewById(R.id.message);
        setView(view);
    }
    public void setMessage(CharSequence message) {
        msg.setText(message);
    }
}
