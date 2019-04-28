package com.ruiao.tools.ui.fragment.maintab;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.about.AboutActivity;
import com.ruiao.tools.login.LoginActivity;
import com.ruiao.tools.ui.BaseDialog;
import com.ruiao.tools.ui.activity.MainActivity;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AppUtil;
import com.ruiao.tools.utils.AsynHttpTools;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.PackageUtils;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Gs on 2017/4/12.
 */

public class MineFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.address)
    TextView address;
    private String url;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private ProgressDialog progressDialog;
    @BindView(R.id.rr_about)
    RelativeLayout about;  //关于软件
    Unbinder unbinder;
    private DownloadBuilder builder;
    private static final int WRITE_EXTERNAL_STORAGE = 122;

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        tvNickname.setText((String) SPUtils.get(mContext, "username", "")); //保存用户名字);
        String base = (String)SPUtils.get(getContext(),"BASE","");
        if(base.startsWith("http://222.222.220.218")){
            address.setText("晋州");
        }else if(base.startsWith("http://222.223.112.252")){
            address.setText("清河");
        }else if(base.startsWith("http://110.249.145.94")){
            address.setText("宁晋");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rr_city, R.id.rr_newversion, R.id.rr_about, R.id.rr_clear, R.id.rr_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rr_city:
                MainActivity activity = (MainActivity) getActivity();
                activity.speek();
                break;
            case R.id.rr_newversion:
                sendRequest();
                break;
            case R.id.rr_about:  //关于软件
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
            case R.id.rr_clear:
                Toast.makeText(mContext, "缓存已清理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rr_logout:
                SPUtils.put(mContext, "login", false);  //登出
                ToastHelper.shortToast(mContext, "退出登录");
                startActivity(new Intent(mContext, LoginActivity.class));
                getActivity().finish();
                break;
        }
    }




    /**
     * 检查是否有权限
     */
    @AfterPermissionGranted(WRITE_EXTERNAL_STORAGE)
    private void startPermissTask() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!
//            Toast.makeText(getActivity(), "WRITE_EXTERNAL_STORAGE", Toast.LENGTH_LONG).show();

        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "更新软件需要存储空间，请允许", WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 授权成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//        newVersion();
    }

    /**
     * 授权失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastHelper.shortToast(getContext(), "未授权，更新中断");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void getmsg() {

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
                        Toast.makeText(getContext(), "request failed", Toast.LENGTH_SHORT).show();

                    }
                }).setShowDownloadingDialog(true)
                .setShowNotification(true)
                .setShowDownloadFailDialog(true)
                .setCustomVersionDialogListener(createCustomDialogTwo())
                .setCustomDownloadingDialogListener(createCustomDownloadingDialog())
                .setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/Ruiao/");
        builder.executeMission(getContext());
    }

    private void checkVersion() {
        HttpUtil.get("http://app.lulibo.xyz/ruiao",new RequestParams(),new HttpUtil.SimpJsonHandle(getContext()){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int version = response.getInt("version");
                    if(getVersionCode(getContext())<version){
                        sendRequest();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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



    private NotificationBuilder createCustomNotification() {
        return NotificationBuilder.create()
                .setRingtone(true)
                .setIcon(R.mipmap.ic_launcher)
                .setTicker("custom_ticker")
                .setContentTitle("custom title")
                .setContentText("custom 内容 ");
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
}
