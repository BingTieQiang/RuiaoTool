package com.ruiao.tools.ui.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import android.widget.TextView;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTNotificationMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.Application;
import com.ruiao.tools.R;
import com.ruiao.tools.ic_card2.DevBean;
import com.ruiao.tools.ic_card2.FactroyBean;
import com.ruiao.tools.ic_card2.ICActivity;
import com.ruiao.tools.tts.AutoCheck;
import com.ruiao.tools.tts.InitConfig;
import com.ruiao.tools.tts.MySyntherizer;
import com.ruiao.tools.tts.NonBlockSyntherizer;
import com.ruiao.tools.tts.UiMessageListener;
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
import com.ruiao.tools.utils.PackageUtils;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer synthesizer;
    protected String appId = "11454794";
    protected Handler mainHandler;
    protected String appKey = "VpgX8i5VrQcTyKge2f8XNEuB";

    protected String secretKey = "1IMIQCAz7Ud0f1tRmEI6jcGrdqTqwXNL";
    private TtsMode ttsMode = TtsMode.ONLINE;
    protected SpeechSynthesizer mSpeechSynthesizer;
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
//        initTTs();
        initialTts();
        initVersion();


//
    }
    public void speek(){
        synthesizer.speak("新的消息");
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        Map<String, String> params = getParams();


        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
//                        toPrint(message); // 可以用下面一行替代，在logcat中查看代码
                         Log.w("AutoCheckMessage", message);
                    }
                }
            }

        });
        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }
    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

//        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
//        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
//        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
//        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
//                offlineResource.getModelFilename());
        return params;
    }

    /**
     *更新版本
     */
    private void initVersion() {
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
//                finish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    url = response.getString("url");
                    final long size = response.getLong("size");
                    int version = response.getInt("version");
                    int versionCode = PackageUtils.getVersionCode(context);
                    if (version > versionCode) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
//                        ToastHelper.shortToast(MainActivity.this, response.getString("message"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
     * 下载APK包，并且安装
     *
     * @param totalByte
     */
    private void downApp(final long totalByte, String  url) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMax(100);
        progressDialog.setTitle("正在下载");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (true) {//非强制更新允许后台更新
            progressDialog.setCancelable(true);
            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "后台更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mBuilder = new NotificationCompat.Builder(context);
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
                AppUtil.installApp(context, file.getPath());

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
        if (EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!
//            Toast.makeText(getActivity(), "WRITE_EXTERNAL_STORAGE", Toast.LENGTH_LONG).show();
            //拥有权限
            downApp(100l,url);
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
        changeFragment(0);
        GTNotificationMessage message = (GTNotificationMessage) msg.obj;
        String title = message.getTitle();
        changeFragment(0);
        NoticeFragment fag = (NoticeFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
        fag.upData();

    }

    public void sendCid(Message msg) {
        String cid = (String) msg.obj;
        RequestParams pa = new RequestParams();
        pa.put("username",SPUtils.get(context, "username", ""));
        pa.put("cid", cid);
        AsynHttpTools.httpGetMethodByParams(URLConstants.VERSION, pa, new JsonHttpResponseHandler("GB2312"){
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
}
