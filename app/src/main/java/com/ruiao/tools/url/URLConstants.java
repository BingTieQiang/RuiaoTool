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
        MBASE = (String)SPUtils.get(Application.getInstance(),"BASE","");
}
//    public static String BASE = "http://222.222.220.218:11888/";//晋州   版本到7了
//    public static String BASE = "http://222.223.112.252:11888/"; //清0.
// 河
    public static String UPLOAD = MBASE+"api/cloud/upload";
    public static String LOGIN = MBASE+"login.asp";
//    public static String DOWNLOAD = MBASE+"api/cloud/testDownloadDoc";
//    public static String REFRESHTOKEN = MBASE+"api/refresh";
//    public static String LOGOUT = MBASE+"loginOut";
    public static String VERSION = MBASE+"version.asp";
//    public static String SCANDOWNLOAD = BASE+"api/template/qrCodeDownload";//扫码下载地址
//    public static String SCANUPLOAD = BASE+"api/template/upload";//扫码上传
//    public static String DOWNAPP = BASE+"api/template/upload";//下载APP接口
//    public static String TASK = BASE+"api/memberScore/index";//任务列表
//    public static String PERSONLIST = BASE+"api/memberScore/scoreInput";//被评分人员列表
//

    public static String IC = MBASE+"DataHistory.asp";// IC 卡总量计
    public static String FAC = MBASE+"facdev.asp";// 工厂 设备列表
    public static String NEW_IC = MBASE+"DataNew.asp";// 最新数据IC卡设备
    public static String CID = MBASE+"cid.asp";// 提交CID

}
/*
http://172.16.1.10:8088/app/api/cloud/upload
http://172.16.1.10:8088/app/api/cloud/download
http://172.16.1.7:8080/egovApp/app/api/memberScore/index
 */