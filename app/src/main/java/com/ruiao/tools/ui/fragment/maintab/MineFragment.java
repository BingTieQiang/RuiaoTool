package com.ruiao.tools.ui.fragment.maintab;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.about.AboutActivity;
import com.ruiao.tools.login.LoginActivity;
import com.ruiao.tools.ui.activity.MainActivity;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AppUtil;
import com.ruiao.tools.utils.AsynHttpTools;
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
                newVersion();
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

    private void newVersion() {
        RequestParams pa = new RequestParams();
//        pa.add("username", (String) SPUtils.get(MainActivity.this,"username",""));
        AsynHttpTools.httpGetMethodByParams(URLConstants.VERSION, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int code, Header[] heads, Throwable throwable, JSONObject json) {

                throwable.printStackTrace();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    url = response.getString("url");
                    final long size = response.getLong("size");
                    int version = response.getInt("version");
                    int versionCode = PackageUtils.getVersionCode(mContext);
                    if (version > versionCode) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        Spanned spanned = Html.fromHtml(response.getString("describe"));
                        builder.setTitle(Html.fromHtml("版本更新"));
                        builder.setMessage(spanned);
                        builder.setCancelable(false);
                        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                startPermissTask();


                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        ToastHelper.shortToast(mContext, "已经是最新版本");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 下载APK包，并且安装
     *
     * @param totalByte
     */
    private void downApp(final long totalByte, String url) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMax(100);
        progressDialog.setTitle("正在下载");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (true) {//非强制更新允许后台更新
            progressDialog.setCancelable(true);
            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "后台更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    mBuilder = new NotificationCompat.Builder(mContext);
                    mBuilder.setContentTitle("版本更新")
                            .setContentText("正在下载...")
                            .setContentInfo("0%")
                            .setSmallIcon(R.drawable.icon_cloudy);
                }
            });

        } else {
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        String apkpath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "phone.apk";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(1000);
        client.get(url, new FileAsyncHttpResponseHandler(new File(apkpath)) {

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, File file) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, File file) {

                if (mNotifyManager != null) {
                    mBuilder.setContentText("正在下载...")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mNotifyManager.notify(1, mBuilder.build());
                    mNotifyManager.cancel(1);
                }
                //开始安装
                AppUtil.installApp(mContext, file.getPath());

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);

                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                if (mBuilder != null) {
                    mBuilder.setProgress(100, count, false);
                    mBuilder.setContentInfo(count + "%");

                    mNotifyManager.notify(1, mBuilder.build());
                }


                progressDialog.setProgress(count);


            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });

    }

    /**
     * 检查是否有权限
     */
    @AfterPermissionGranted(WRITE_EXTERNAL_STORAGE)
    private void startPermissTask() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!
//            Toast.makeText(getActivity(), "WRITE_EXTERNAL_STORAGE", Toast.LENGTH_LONG).show();
            //拥有权限
            downApp(100l, url);
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
}
