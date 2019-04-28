package com.ruiao.tools.dongtaiguankong;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by ruiao on 2018/9/28.
 */

public class LoadingDia extends Dialog {
    private String msg;
    private AVLoadingIndicatorView loading;
    private Context context;
    private TextView tv_loading;
    public LoadingDia(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    public LoadingDia(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_dialog, null);
        setContentView(view);
        loading =view.findViewById(R.id.avi);
        loading.show();
//        tv_loading = view.findViewById(R.id.tv_loading);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.3); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }



}
