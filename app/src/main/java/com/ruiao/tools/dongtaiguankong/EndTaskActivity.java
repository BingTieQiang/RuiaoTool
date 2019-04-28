package com.ruiao.tools.dongtaiguankong;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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



public class EndTaskActivity extends BaseAppCompatActivity implements  TakePhoto.TakeResultListener, InvokeListener {
    private LinearLayout tasktip;
    private TaskBean bean;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private ImageView img1;
    private ArrayList<Uri> imgList;
    private Button btn;
    ArrayList<TImage> list;
    private EditText log;
    private boolean hasphoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        bean = (TaskBean) getIntent().getSerializableExtra("task");
        imgList = new ArrayList<>();
//        ArrayList<String> arrayList = bean.tips;
//        for (final String tip:arrayList) {
//            CheckBox tv = new CheckBox(context);
//            tv.setTextSize(20);
//            tv.setText(tip);
//            tv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if(b){
//                        String temp = log.getText().toString();
//                        if("".equals(temp)){
//                            log.setText(temp + tip );
//                        }else {
//                            log.setText(temp + "\n" + tip );
//                        }
//                    }
//                }
//            });
//            tasktip.addView(tv);
//        }
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LocationUtils.getInstance( this ).removeLocationUpdatesListener();
    }

    private void initData() {
        File file1 = new File(Environment.getExternalStorageDirectory(), "/yunwei/" + 3 + ".jpg");
        if(file1.exists()){
            file1.delete();
        }
        imgList.add(Uri.fromFile(file1));
    }

    @Override
    protected void initView() {
        tasktip = findViewById(R.id.tasktip);
        log = findViewById(R.id.et_log);
        btn = findViewById(R.id.btn_next);
        img1 = findViewById(R.id.img1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePhoto.onPickFromCapture(imgList.get(0));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasphoto){
                    ToastHelper.shortToast(context,"请拍摄照片记录");
                    return;
                }
//                if (lacksPermissions(PERMISSION)) {
//
//                    ActivityCompat.requestPermissions(EndTaskActivity.this, PERMISSION, OPEN_SET_REQUEST_CODE);
//
//                } else {

                    // Log.i(TAG, "已经全部打开权限");
                    updata();
//                }
            }


        });
        CompressConfig compressConfig=new CompressConfig.Builder().setMaxSize(60*1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig,true);
    }
    private void updata() {
//        Location location = LocationUtils.getInstance( context ).showLocation();
//        double lat = location.getLatitude();//维度
//        double lon = location.getLongitude();//经度


        RequestParams pa = new RequestParams();
        pa.put("taskname",bean.id);
        pa.put("Action","h");
        pa.put("GPS_J_2","");
        pa.put("GPS_W_2","");
        String ss = log.getText().toString();
        ss.trim();
        String strBase64 = Base64.encodeToString(ss.getBytes(), Base64.DEFAULT);
        pa.put("tasklog",strBase64);

        try {
            pa.put("photo1",new File(list.get(0).getCompressPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HttpUtil.httppost(URLConstants.UP,pa,new HttpUtil.SimpJsonHandle(context){
            @Override
            public void onStart() {
                super.onStart();
                showDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ToastHelper.shortToast(context,"本次运维完成");
                finish();
//                        Intent intent = new Intent(context,EndTaskActivity.class);
//                        intent.putExtra("task",bean);
//                        startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("err",responseString.toString());
                throwable.printStackTrace();
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
    @Override
    public String SetTitle() {
        return "完成运维";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_end_task;
    }



    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        hasphoto = true;
        list = result.getImages();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.yujiazai)
                .error(R.drawable.jiazaishibai)
                .priority(Priority.HIGH)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                ;
        Glide.with(this).load(list.get(0).getCompressPath()).apply(options).into(img1);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

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
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
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


}
