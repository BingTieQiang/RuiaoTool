package com.ruiao.tools.login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.ruiao.tools.R;
import com.ruiao.tools.animator.JellyInterpolator;
import com.ruiao.tools.ui.activity.MainActivity;
import com.ruiao.tools.ui.activity.SplashActivity;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AsynHttpTools;

import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录 添加服务器选择
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    private TextView mBtnLogin;
    private EditText et_name;
    private View progress;
    private EditText et_password;
    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
    }

    /**
     * 登录提交方法
     * 如果成功，finish本页面，进入主功能页面
     * 失败弹出土司
     */
    public void submit() {
        RequestParams pa = new RequestParams();
        pa.add("username",et_name.getText().toString().trim());
        pa.add("password",et_password.getText().toString().trim());
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        pa.add("uid",ANDROID_ID);
        AsynHttpTools.httpGetMethodByParams(URLConstants.LOGIN, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFinish() {
                super.onFinish();
                recovery();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
                ToastHelper.shortToast(context, "" + statusCode);
            }
            @Override
            public void  onFailure(int code, Header[] heads, Throwable throwable, JSONObject json)
            {
                throwable.printStackTrace();
                finish();
            }
            //{"version":"2018.05.26","describe":"版本更改的内容","url":"http://222.222.220.218:11888/2018.05.26.apk"}
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                recovery();
                try {

                        if( "true" .endsWith( response.getString("success"))){
                            SPUtils.put(context,"login",true);  //登录已经登录
                            SPUtils.put(context,"username",et_name.getText().toString().trim());  //保存用户名字

                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        }else {
                            ToastHelper.shortToast(context,response.getString("message"));

                            startActivity(new Intent(context, LoginActivity.class));
                            finish();
                        }





                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
    private void initView() {
        et_name = (EditText) findViewById(R.id.et_user);
        et_password = (EditText)findViewById(R.id.et_password);
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

        mBtnLogin.setOnClickListener(this);



        ArrayAdapter dev_adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,new String[]{"晋州","清河","宁晋"});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_lonch);
        builder.setTitle("请选择服务器地址");
        builder.setAdapter(dev_adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              switch (which){
                  case 0:
                      SPUtils.put(context,"BASE","http://222.222.220.218:11888/");
                      break;
                  case 1:
                      SPUtils.put(context,"BASE","http://222.223.112.252:11888/");
                      break;
                  case 2:
                      SPUtils.put(context,"BASE","http://110.249.145.94:11888/");      //宁晋
                      break;
              }
            }
        });
        builder.show();
    }


    @Override
    public void onClick(View view) {
        int viewid =  view.getId();

        switch (viewid) {
            case R.id.main_btn_login:
                login();

                break;

            default:
                break;
        }



    }

    private void login() {
        int len_name = et_name.getText().toString().trim().length();
        int len_password = et_password.getText().toString().trim().length();
        if(len_name==0){
            ToastHelper.shortToast(context,"请输入用户名");
            return;
        }
        if(len_password<3){
            ToastHelper.shortToast(context,"密码格式错误，密码长度应大于等于3");
            return;
        }
        // 计算出控件的高与宽
        mWidth = mBtnLogin.getMeasuredWidth();
        mHeight = mBtnLogin.getMeasuredHeight();
        // 隐藏输入框
        mName.setVisibility(View.INVISIBLE);
        mPsw.setVisibility(View.INVISIBLE);

        inputAnimator(mInputLayout, mWidth, mHeight);
        submit();

    }

    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }
    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
    /**
     * 恢复初始状态
     */
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }

}
