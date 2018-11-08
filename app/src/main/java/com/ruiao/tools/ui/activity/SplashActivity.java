package com.ruiao.tools.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.ruiao.tools.R;
import com.ruiao.tools.login.LoginActivity;
import com.ruiao.tools.ui.base.BaseActivity;
import com.ruiao.tools.utils.SPUtils;


public class SplashActivity extends BaseActivity {

    private static final int sleepTime = 1000;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        long start = System.currentTimeMillis();
        new Thread(new SplashRunnable(start)).start();
    }

    class SplashRunnable implements Runnable {

        private long start;

        public SplashRunnable(long start) {
            this.start = start;
        }

        @Override
        public void run() {

            long costTime = System.currentTimeMillis() - start;
            if (sleepTime - costTime > 0) {
                try {
                    Thread.sleep(sleepTime - costTime);
                } catch (Exception e) {
                }
            }
            boolean logint =(boolean) SPUtils.get(SplashActivity.this,"login",false);


            if(logint){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }


            finish();
        }
    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}