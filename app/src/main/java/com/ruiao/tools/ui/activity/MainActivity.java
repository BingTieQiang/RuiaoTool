package com.ruiao.tools.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;


import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.google.zxing.activity.CaptureActivity;
import com.igexin.sdk.PushManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.Application;
import com.ruiao.tools.R;
import com.ruiao.tools.menjin.MenjinResultActivity;
import com.ruiao.tools.the.TheActivity;

import com.ruiao.tools.ui.BaseDialog;
import com.ruiao.tools.ui.base.BaseActivity;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.ui.fragment.maintab.DataFragment;
import com.ruiao.tools.ui.fragment.maintab.EnergyFragment;
import com.ruiao.tools.ui.fragment.maintab.FormFragment;
import com.ruiao.tools.ui.fragment.maintab.MineFragment;
import com.ruiao.tools.ui.fragment.maintab.NoticeFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AppUtil;
import com.ruiao.tools.utils.AsynHttpTools;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.PackageUtils;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    public final static int REQ_CODE = 1028;
    private DownloadBuilder builder;
    protected Handler mainHandler;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private ProgressDialog progressDialog;
    private static final int WRITE_EXTERNAL_STORAGE = 122;
    private String url;
    Context context;
    @BindView(R.id.main_tab1)
    View mTab1;

    @BindView(R.id.main_tab3)
    View mTab3;

    @BindView(R.id.main_tab5)
    View mTab5;
    @BindString(R.string.app_exit)
    String app_exit;
    private final int[] mTabIcons = new int[]{R.drawable.tab_main_data_selector,
            R.drawable.tab_main_energy_selector,
            R.drawable.tab_main_mine_selector,R.drawable.tab_main_form_selector,
            R.drawable.tab_main_mine_selector};
    // 当前fragment的index
    private int currentTabIndex = 0;
    private View[] mTabs;
    private long mExitTime;
    private NoticeFragment homeFragment;
    private DataFragment dataFragment;
    private EnergyFragment energyFragment;
    private FormFragment formFragment;
    private MineFragment mineFragment;
    protected BaseFragment currentFragment;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        Application.mainActivity = this;
        context = this;
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), com.ruiao.tools.service.MyServer.class);
        //  DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.ruiao.tools.service.DemoIntentService.class);
        StatusBarUtil.darkMode(this);

        homeFragment = new NoticeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, homeFragment).commitAllowingStateLoss();
        currentFragment = homeFragment;
        currentTabIndex = 1;
        initTabs();
        changeFragment(currentTabIndex);
        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
//                    print(msg.obj.toString());
                }
            }

        };
//        initVersion();
        checkVersion();
    }
    public void speek(){

    }



    /**
     * 初始化底部4个导航按钮
     */

    private void initTabs() {
        mTabs = new View[]{mTab1,mTab3,mTab5};
        String[] mTabTitles = getResources().getStringArray(R.array.main_tab_titles);
        mTabs[2].setSelected(true);
        for (int i = 0; i < mTabs.length; i++) {
            initTab(i, mTabTitles[i], mTabIcons[i]);
        }
    }

    /**
     * 初始化单个导航布局
     *
     * @param position  位置
     * @param mTabTitle 标题
     * @param mTabIcon  按钮Res
     */
    private void initTab(final int position, String mTabTitle, int mTabIcon) {
        View tab = mTabs[position];

        ImageView tab_icon_iv = (ImageView) tab.findViewById(R.id.tab_icon_iv);
        TextView tab_title_tv = (TextView) tab.findViewById(R.id.tab_title_tv);

        tab_icon_iv.setImageResource(mTabIcon);
        tab_title_tv.setText(mTabTitle);
        mTabs[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(position);
            }
        });

    }

    public void changeFragment(int position) {
        for (int i = 0; i < mTabs.length; i++) {
            mTabs[i].setSelected(i == position);
        }
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new NoticeFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), homeFragment);
                break;

            case 1:
                if (energyFragment == null) {
                    energyFragment = new EnergyFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), energyFragment);
                break;

            case 2:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), mineFragment);
                break;

        }
        currentTabIndex = position;
    }

    /**
     * 添加或者显示 fragment
     *
     * @param transaction
     * @param fragment
     */
    protected void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(R.id.main_content, fragment).commitAllowingStateLoss();
        } else {
            transaction.hide(currentFragment).show(fragment).commitAllowingStateLoss();
        }
        currentFragment = (BaseFragment) fragment;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastHelper.shortToast(this, app_exit);
                mExitTime = System.currentTimeMillis();
                return true;
            } else {
                Application.getInstance().exit();
                System.exit(0);
                //android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 检查是否有权限
     */
    @AfterPermissionGranted(WRITE_EXTERNAL_STORAGE)
    private void startPermissTask() {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!
//            Toast.makeText(getActivity(), "WRITE_EXTERNAL_STORAGE", Toast.LENGTH_LONG).show();
            //拥有权限

        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "更新软件需要存储空间，请允许", WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastHelper.shortToast(context,"未授权，更新中断");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    public void getNotice(Message msg){
        Intent intent = new Intent(context, TheActivity.class);
        intent.putExtra("msg",true);
        startActivity(intent);


/*
        changeFragment(0);
        GTNotificationMessage message = (GTNotificationMessage) msg.obj;
        String title = message.getTitle();
//        NoticeFragment fag = (NoticeFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
//        fag.upData();
        currentFragment.getmsg();
     */

    }

    public void sendCid(Message msg) {
        String cid = (String) msg.obj;
        RequestParams pa = new RequestParams();
        pa.put("username",SPUtils.get(context, "username", ""));
        pa.put("cid", cid);
        AsynHttpTools.httpGetMethodByParams(URLConstants.CID, pa, new JsonHttpResponseHandler("GB2312"){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void sendRequest() {

        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("http://app.lulibo.xyz/ruiao")
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        try {
                            JSONObject json = new JSONObject(result);
                            return crateUIData(json.getString("title"),json.getString("context"),json.getString("url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return crateUIData("","","");
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        Toast.makeText(context, "request failed", Toast.LENGTH_SHORT).show();

                    }
                }).setShowDownloadingDialog(true)
                .setShowNotification(true)
                .setShowDownloadFailDialog(true)
                .setCustomVersionDialogListener(createCustomDialogTwo())
                .setCustomDownloadingDialogListener(createCustomDownloadingDialog())
                .setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/Ruiao/");
        builder.executeMission(this);
    }

    private void checkVersion() {
        HttpUtil.get("http://app.lulibo.xyz/ruiao",new RequestParams(),new HttpUtil.SimpJsonHandle(context){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int version = response.getInt("version");
                    Log.d("version","old"+getVersionCode(context)+"new"+version);
                    if(getVersionCode(context)<version){

                        sendRequest();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                throwable.printStackTrace();
            }
        });
    }


    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }




    private UIData crateUIData(String title,String context,String url) {
        UIData uiData = UIData.create();
        uiData.setTitle(title);
        uiData.setDownloadUrl(url);
        uiData.setContent(context);
        return uiData;
    }

    private CustomVersionDialogListener createCustomDialogTwo() {
        return (context, versionBundle) -> {
            BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_two_layout);
            TextView textView = baseDialog.findViewById(R.id.tv_msg);
            TextView title = baseDialog.findViewById(R.id.tv_title);
            title.setText(versionBundle.getTitle());
            textView.setText(versionBundle.getContent());
            baseDialog.setCanceledOnTouchOutside(true);
            return baseDialog;
        };
    }

    private CustomDownloadingDialogListener createCustomDownloadingDialog() {
        return new CustomDownloadingDialogListener() {
            @Override
            public Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_download_layout);
                return baseDialog;
            }

            @Override
            public void updateUI(Dialog dialog, int progress, UIData versionBundle) {
                TextView tvProgress = dialog.findViewById(R.id.tv_progress);
                ProgressBar progressBar = dialog.findViewById(R.id.pb);
                progressBar.setProgress(progress);
                tvProgress.setText(getString(R.string.versionchecklib_progress, progress));
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if(resultCode==0){
                String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
                Intent intent = new Intent(this, MenjinResultActivity.class);
                intent.putExtra("mn",result);
                startActivity(intent);
            }
        }
    }
}
