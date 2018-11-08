package com.ruiao.tools.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;


/**
 * 网络请求类库 ，异步
 * version 2.0
 * 2017-02-24
 */
public class AsynHttpTools {
    public static final String ISTOKENSUCCESS = "isTokenSuccess";
    public static final String ACCESS_TOKEN = "access_token";
    public static String REQUESTTOKENPARAM = "app_token"; //请求Token 参数名称
    public static String BASE_URL = "";/*UrlConstants.IP*/
    public static AsyncHttpClient asynClient = new AsyncHttpClient();//异步请求
    public static SyncHttpClient syncHttpClient = new SyncHttpClient();//同步请求


    /**
     * 检查token 刷新功能
     *
     * @param url
     * @param params
     * @param result
     */
    //private static int count = 1;

    static {

        asynClient.setTimeout(21000);// 请求超时时间
        syncHttpClient.setTimeout(2000);
    }

    private AsynHttpTools() {

    }




    public static void setHeader(String header, String value) {
        asynClient.addHeader(header, value);
    }

    public static void removeHeader(String header) {
        asynClient.removeHeader(header);
    }

    public static void removeAllHeader() {
        asynClient.removeAllHeaders();
    }

    /**
     * Get请求
     *
     * @param url             请求地址
     * @param responseHandler 回调接口
     */
    public static void httpGetMethod(String url,
                                     AsyncHttpResponseHandler responseHandler) {

        asynClient.get((url), responseHandler);
    }

    /**
     * Get 请求
     *
     * @param url             请求地址
     * @param params          请求参数
     * @param responseHandler 回调接口
     */
    public static void httpGetMethodByParams(String url, RequestParams params,
                                             AsyncHttpResponseHandler responseHandler) {
        Log.i("lulibo", "Get-URL::::" + (url) + "?" + params.toString());
        asynClient.get((url), params, responseHandler);

    }




    /**
     * POST 请求
     *
     * @param url             请求地址
     * @param responseHandler 回调接口
     */
    public static void httpPostMethod(String url,
                                      AsyncHttpResponseHandler responseHandler) {
        asynClient.post(url, responseHandler);

    }

    /**
     * POST 请求
     *
     * @param url             请求地址
     * @param params          请求参数
     * @param responseHandler 回调接口
     */
    public static void httpPostMethodByParams(String url, RequestParams params,
                                              AsyncHttpResponseHandler responseHandler) {

        asynClient.post(url, params, responseHandler);


    }









}
