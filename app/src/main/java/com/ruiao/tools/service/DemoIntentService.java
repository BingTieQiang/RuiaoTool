package com.ruiao.tools.service;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.GTServiceManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.ruiao.tools.Application;
import com.ruiao.tools.ui.activity.MainActivity;

/**
 * Created by ruiao on 2018/5/9.
 */

public class DemoIntentService extends GTIntentService {
    private static final String TAG = "GetuiSdkDemo";
    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
//        Log.e(TAG,msg.);
        byte[] payload = msg.getPayload();
        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload); //data就是透传的数据
            Log.d("data",data);

        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        Message msgx = Message.obtain();
        msgx.what = 22;
        msgx.obj = clientid;
        Application.sendMessage(msgx);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }
    //透传消息，开启语音通知
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e("bbb1", cmdMessage.toString());

    }
    //普通通知
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        Log.e("bbb2", msg.getTitle()+msg.getContent());
    }
    //通知点击事件
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
        Log.d("Notification","onNotificationMessageClicked");
        Message msgx = Message.obtain();
        msgx.what = 11;
        msgx.obj = msg;
        Application.sendMessage(msgx);
    }


}
