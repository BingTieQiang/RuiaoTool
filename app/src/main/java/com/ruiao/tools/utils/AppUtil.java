package com.ruiao.tools.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;

import com.ruiao.tools.BuildConfig;

import java.io.File;

/**
 * APP 安装工具类
 * Created by lulibo on 2017-5-24.
 */

public class AppUtil {
    /**
     * 安装APK包
     * @param context 上下文
     * @param filePath APK包地址
     */
    public static void installApp(Context context, String filePath) {
        File _file = new File(filePath);
        Intent installIntent = new Intent();
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setAction(Intent.ACTION_VIEW);

        Uri apkFileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", _file);
        } else {
            apkFileUri = Uri.fromFile(_file);
        }
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installIntent.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        try {
            context.startActivity(installIntent);
        } catch (ActivityNotFoundException e) {

        }
    }
    public static void showDialog(Context context, String title, String msg, boolean cancelable, DialogInterface.OnClickListener listener, String positiveStr){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(msg).setCancelable(cancelable).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("确定", listener).create().show();
    }

}
