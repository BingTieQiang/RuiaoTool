package com.ruiao.tools.dongtaiguankong;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.ToastHelper;


import org.apache.http.Header;
import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;



public class StartTaskActivity extends BaseAppCompatActivity implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {
    private TaskBean bean;
    private boolean hasphoto = false;
    int i;
    private ImageView img1, img2, img3, img4, img5;
    private TextView tv_renwu, tv_time, tv_people, tv_address;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private ArrayList<Uri> imgList;
    ArrayList<TImage> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        bean = (TaskBean) getIntent().getSerializableExtra("task");
        imgList = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory(), "/yunwei/" + "xxx" + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LocationUtils.getInstance(this).removeLocationUpdatesListener();
    }

    private void initData() {
        tv_renwu.setText(bean.id);
        tv_time.setText(bean.time);
        tv_people.setText(bean.people);
        tv_address.setText(bean.address);

        File file1 = new File(Environment.getExternalStorageDirectory(), "/yunwei/" + 1 + ".jpg");
        File file2 = new File(Environment.getExternalStorageDirectory(), "/yunwei/" + 2 + ".jpg");
        imgList.add(Uri.fromFile(file1));
        imgList.add(Uri.fromFile(file2));
        if (file1.exists()) {
            file1.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(60 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, true);

    }

    @Override
    protected void initView() {
        img1 = findViewById(R.id.img1);
        img1.setOnClickListener(this);
        tv_renwu = findViewById(R.id.tv_renwuhao);
        tv_time = findViewById(R.id.tv_time);
        tv_people = findViewById(R.id.tv_people);
        tv_address = findViewById(R.id.tv_address);
    }

    @Override
    public String SetTitle() {
        return "拍照开始任务";

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start_tasksss;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img1:
                i = 0;
                takePhoto.onPickFromCapture(imgList.get(0));
                break;


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_SET_REQUEST_CODE) {
            if (lacksPermissions(PERMISSION)) {

//                Log.i(TAG, "缺少权限去申请");

                ActivityCompat.requestPermissions(this, PERMISSION, OPEN_SET_REQUEST_CODE);
            }
        }
    }


    @Override
    public void takeSuccess(TResult result) {
        TImage img = result.getImages().get(0);
//        Glide.with(this).load(new File(img.getOriginalPath())).into(img1);

        list = result.getImages();

        if (i == 0) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.yujiazai)
                    .error(R.drawable.jiazaishibai)
                    .priority(Priority.HIGH)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(this).load(list.get(0).getCompressPath()).apply(options).into(img1);
            hasphoto = true;

        } else if (i == 1) {
            Glide.with(this).load(list.get(1).getCompressPath()).into(img2);

        }

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OPEN_SET_REQUEST_CODE) {

            if (hasAllPermissionsGranted(grantResults)) {

//                Log.i(TAG, "用户允许打开权限");
                updata();
            } else {

//                Log.i(TAG, "用户拒绝打开权限");

                showPermissionDialog("请去设置页面打开权限", OPEN_SET_REQUEST_CODE);
            }

        }

        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    public void go(View view) {
        if (!hasphoto) {
            ToastHelper.shortToast(context, "请拍摄照片记录");
            return;
        }
//        if (lacksPermissions(PERMISSION)) {
//
//            ActivityCompat.requestPermissions(this, PERMISSION, OPEN_SET_REQUEST_CODE);
//
//        } else {

            // Log.i(TAG, "已经全部打开权限");
            updata();
//        }


    }

    private void updata() {
//        Location location = LocationUtils.getInstance(context).showLocation();
//        double lat = location.getLatitude();//维度
//        double lon = location.getLongitude();//经度
//
//        Log.d("location",""+lat+lon);

        RequestParams pa = new RequestParams();
        pa.put("taskname", bean.id);
        pa.put("Action", "q");
        pa.put("GPS_J_1", "" );
        pa.put("GPS_W_1", "" );
        Log.d("param", pa.toString());
        try {
            pa.put("photo1", new File(list.get(0).getCompressPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpUtil.httppost(URLConstants.UP, pa, new HttpUtil.SimpJsonHandle(context) {
            @Override
            public void onStart() {
                super.onStart();
                showDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ToastHelper.shortToast(context, "上传成功");
                finish();
                Intent intent = new Intent(context, EndTaskActivity.class);
                intent.putExtra("task", bean);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                dia.dismiss();
            }
        });


    }
}
