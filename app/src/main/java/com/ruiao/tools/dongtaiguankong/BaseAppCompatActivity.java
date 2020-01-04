package com.ruiao.tools.dongtaiguankong;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ruiao.tools.R;


/**
 * Created by ruiao on 2018/9/24.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    public String[] PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    public static final int OPEN_SET_REQUEST_CODE = 15;
    public LoadingDia dia;
    private static final String TAG = BaseAppCompatActivity.class.getSimpleName();
    private TextView mToolbarTitle;
    private TextView mToolbarSubTitle;
    private Toolbar mToolbar;
    private String thetile;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        context = this;
        initView();
        thetile = SetTitle();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       /*
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub Title");
        */
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarSubTitle = (TextView) findViewById(R.id.toolbar_subtitle);
        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            //设置默认的标题不显示
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        setToolBarTitle(thetile);
        dia = new LoadingDia(context);
    }

    protected abstract void initView();

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if (null != getToolbar() && isShowBacking()) {
            showBack();
        }
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    public abstract String SetTitle();

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getSubTitle() {
        return mToolbarSubTitle;
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        } else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     *
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar().setNavigationIcon(null);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBacking() {
        return true;
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     *
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dia = null;
        Log.v(TAG, "onDestroy...");

    }

    public void showDialog() {

        dia.show();
    }

    public boolean lacksPermissions(String... permissions) {

        for (String permission : permissions) {

            if (lacksPermission(permission)) {

                return true;

            }

        }

        return false;

    }

    public boolean lacksPermission(String permission) {

        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED;

    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {

        for (int grantResult : grantResults) {

            if (grantResult == PackageManager.PERMISSION_DENIED) {//PERMISSION_GRANTED 授予

                return false;

            }

        }

        return true;

    }

    public void showPermissionDialog(String text, int requestCode) {

/*此处弹出Dialog显示内容为text（需要去设置页面打开***权限）具体Dialog我就不写了,

        点击确定按钮调用toSetActivity（OPEN_SET_REQUEST_CODE）方法*/

        toSetActivity(requestCode);

    }

    public void toSetActivity(int requestCode) {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

        Uri uri = Uri.fromParts("package", getPackageName(), null);

        intent.setData(uri);

        startActivityForResult(intent, requestCode);

    }

}




