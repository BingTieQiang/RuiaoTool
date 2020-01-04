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

    public static String MBASE = "http://222.222.220.218:11888/";

    static {
        MBASE =(String) SPUtils.get(Application.getInstance(),"BASE","");
}
    public static String BASE = "http://222.222.220.218:11888/";//
    public static String BASE1 = "http://110.249.145.94:11114/gkjk/"; //清0.

    public static String LOGIN = MBASE+"login.asp";

    public static String VERSION = MBASE+"version.asp";
    public static String WarnData = MBASE+"WarnData.asp";


//

    public static String IC = MBASE+"DataHistory.asp";// IC 卡总量计
    public static String FAC = MBASE+"facdev.asp";// 工厂 设备列表
    public static String VOC = MBASE+"DataHistory.asp";// VOC
    public static String NEW_IC = MBASE+"DataNew.asp";// 最新数据IC卡设备
    public static String CID = MBASE+"cid.asp";// 提交CID
    public static String UP = MBASE+"PutTask.asp";    //上传信息


    public static String UPLOAD = BASE+"api/cloud/upload";
    public static String INFO = BASE+"geren.asp";    //个人信息
    public static String TASKLIST = BASE1+"tasklist.asp";    //任务列表
    public static String WARNDATA = BASE1+"WarnData.asp";    //报警历史
    public static String DANZHAN = BASE1+"DataHistory.asp";    //单站工况
    public static String DITU = BASE1+"gps.asp";    //地图信息


    public static String SHISHI = BASE1+"DataGongKX.asp";    //实时工况
    public static String SHEBEI = BASE1+"facdev.asp";    //请求设备


//    public static String AQI =MBASE+"gckqz/gps.asp";    //通用版本空气站
//    public static String AQI1 =MBASE+"gckqz/DataHistory.asp";    //空气站

    public static String AQI =MBASE+"gckqz/gps.asp";    // 新乐 空气站
    public static String AQI1 =MBASE+"gckqz/DataHistory.asp";    //空气站


//    public static String YOUYAN ="http://183.196.178.13:11888/gpsu.asp";    //油烟 地图
    public static String YOUYAN =MBASE+"gpsu.asp";    //油烟 地图
//    public static String YOUYAN_HISTROY ="http://183.196.178.13:11888/DataHistoryU.asp";    //油烟历史
    public static String YOUYAN_HISTROY =MBASE+"DataHistoryU.asp";    //油烟历史

    public static String DANYANGHUAWU =MBASE+"Nox/DataHistory.asp";    //氮氧化物
//    public static String DANYANGHUAWU ="http://222.222.220.218:11888/Nox/DataHistory.asp";    //氮氧化物
//    public static String DANYANGHUAWU_HISTROY ="http://183.196.178.13:11888/DataHistoryU.asp";    //油烟历史
    public static String DANYANGHUAWU_HISTROY =MBASE+"DataHistoryU.asp";    //油烟历史

    public static String MENJIN = "http://110.249.145.94:11114/dtgk/doorsta.asp";    //门禁
    public static String MENJIN_OPEN = "http://110.249.145.94:11114/dtgk/dooract.asp";    //门禁 开启
    public static String GONGDI_NOWDATA = "http://222.223.121.13:11888/DataNewC.asp";    //
    public static String GONGDI_WARNNING = "http://222.223.121.13:11888/WarnningC.asp";    //工地报警 列表
//    public static String GONGDI_Histry = "http://222.223.121.13:11888/DataHistoryC.asp";    //
    public static String GONGDI_Histry = MBASE+"DataHistoryC.asp";    //
    public static String Dongtai_Histry = "http://110.249.145.94:11114/dtgk/DataHistory.asp";    //
//    public static String GONGDI_FAC = "http://222.223.121.13:11888/facdevC.asp";    //
    public static String GONGDI_FAC = MBASE+"facdevC.asp";    //
    public static String Dongtai_FAC = "http://110.249.145.94:11114/dtgk/facdev.asp";    //
    public static String Zhaopian = "http://110.249.145.94:11114/dtgk/photo.asp";    //
    public static String Fenbiao_HISTROY = MBASE+"http://110.249.145.94:11114/gkjk/DataHistoryF.asp";//分表计电
    public static String Fenbiao_Company=MBASE+ "fbjd/facdev.asp";//分表公司列表
    public static String Fenbiao_Histroy= MBASE+"fbjd/DataHistory.asp";//分表历史数据
    public static String Fenbiao_Yujing=MBASE+ "fbjd/yujing.asp";//分表预警
    public static String Fenbiao_Baojin=MBASE+ "fbjd/WarnData.asp";//分表报警
    public static String Fenbiao_YichangyiCe= "WarnData.asp";//一厂一策

//    public static String Fenbiao_Company= "http://183.196.178.13:11889/facdev.asp";//分表公司列表
//    public static String Fenbiao_Histroy= "http://183.196.178.13:11889/DataHistory.asp";//分表历史数据
//    public static String Fenbiao_Yujing= "http://183.196.178.13:11889/yujing.asp";//分表预警
//    public static String Fenbiao_Baojin= "http://183.196.178.13:11889/WarnData.asp";//分表报警
//    public static String Fenbiao_YichangyiCe= "http://183.196.178.13:11889/WarnData.asp";//一厂一策


}
