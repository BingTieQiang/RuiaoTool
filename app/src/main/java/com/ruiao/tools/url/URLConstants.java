package com.ruiao.tools.url;

import com.ruiao.tools.Application;
import com.ruiao.tools.utils.SPUtils;

/**
 * 接口地址
 *
 * 清河，晋州
 *
 * 南宫，无极    在线，IC卡设备。
 *
 *宁晋
 */

public class URLConstants {
//    http://110.249.145.94:6688/login.asp
    public static String MBASE;

    static {
        MBASE = "http://222.222.220.218:11888/";
}
    public static String BASE = "http://222.222.220.218:11888/";//晋州   版本到7了
    public static String BASE1 = "http://110.249.145.94:11114/gkjk/"; //清0.
// 河

//    public static String LOGIN = MBASE+"login.asp";    原本的
    public static String LOGIN = BASE1+"login.asp";
//    public static String DOWNLOAD = MBASE+"api/cloud/testDownloadDoc";
//    public static String REFRESHTOKEN = MBASE+"api/refresh";
//    public static String LOGOUT = MBASE+"loginOut";
    public static String VERSION = MBASE+"version.asp";
    public static String WarnData = MBASE+"WarnData.asp";
//    public static String SCANDOWNLOAD = BASE+"api/template/qrCodeDownload";//扫码下载地址
//    public static String SCANUPLOAD = BASE+"api/template/upload";//扫码上传
//    public static String DOWNAPP = BASE+"api/template/upload";//下载APP接口
//    public static String TASK = BASE+"api/memberScore/index";//任务列表

//

    public static String IC = MBASE+"DataHistory.asp";// IC 卡总量计
    public static String FAC = MBASE+"facdev.asp";// 工厂 设备列表
    public static String VOC = MBASE+"DataHistory.asp";// VOC
    public static String NEW_IC = MBASE+"DataNew.asp";// 最新数据IC卡设备
    public static String CID = BASE1+"cid.asp";// 提交CID
    public static String UP = BASE1+"PutTask.asp";    //上传信息


    public static String UPLOAD = BASE+"api/cloud/upload";
    public static String INFO = BASE+"geren.asp";    //个人信息
    public static String TASKLIST = BASE1+"tasklist.asp";    //任务列表
    public static String WARNDATA = BASE1+"WarnData.asp";    //报警历史
    public static String DANZHAN = BASE1+"DataHistory.asp";    //单站工况
    public static String DITU = BASE1+"gps.asp";    //地图信息


    public static String SHISHI = BASE1+"DataGongKX.asp";    //实时工况
    public static String SHEBEI = BASE1+"facdev.asp";    //请求设备

    public static String AQI ="http://110.249.145.94:11114/gckqz/gps.asp";    //空气站
    public static String AQI1 ="http://110.249.145.94:11114/gckqz/DataHistory.asp";    //空气站


}
