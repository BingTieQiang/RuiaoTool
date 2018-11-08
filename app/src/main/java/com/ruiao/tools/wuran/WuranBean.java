package com.ruiao.tools.wuran;

import java.io.Serializable;

/**
 * Created by ruiao on 2018/6/4.
 *
 */
/*
"烟气流量":6.07,
"烟尘浓度":7.02,
"烟尘折算":8.57,
"二氧化硫":1.7,
"二氧化硫折算":2.08,
"氮氧化物":100.36,
"氮氧化物折算":122.45,


"流量":0,
"COD":0,
"NH3N":0,
"总磷":0,
"总氮":0,

"流量平均值":[],
"COD平均值":[],
"氨氮平均值":[],
"总磷平均值":[],
"总氮平均值":



"烟气流量":5.59,
"烟尘浓度":7.19,
"烟尘折算":9.78,
"二氧化硫":1.97,
"二氧化硫折算":2.68,
"氮氧化物":105.17,
"氮氧化物折算":143.09,
 */
public class WuranBean  implements Serializable{
    public int id;
    public String date;
    public int devtype;//设备类型 1开头是气分钟数据， 2开头是水 分钟数据  11气设备 非分钟数据 21 水设备 非分钟设备，

    //1 开头是气分钟数据
    public String QMyanchenliuliang; //流量
    public String QMyancennongdu; //烟尘
    public String QMyancenzhesuan; //烟尘折算
    public String QMeryanghualiu; //二氧化硫
    public String QMeryanghualiuzhesuan; //二氧化硫折算
    public String QMdanyang; //氮氧化物
    public String QMdanyangzhesuan; //氮氧化物折算


    //2 开头是水 分钟数据
    public String SMliuliang;   //流量
    public String SMcod;        //COD
    public String SMnh3n;       //NH3N
    public String SMzonlin;     //总磷
    public String SMzondan;     //总氮




    //21 水设备 非分钟设备，
    public String liuLiangPinJun;  //流量平均值
    public String andanPingjun;    //氨氮
    public String CODPinJun;   //COD平均值
    public String zonlinPingJun;  //总磷
    public String zondanPingJun;   //总氮

    //11气设备 非分钟数据
    public String yanCenLiuLiang;  //烟气流量
    public String yanCenNongDu;   //烟尘浓度
    public String yanCenZheSuan;   //烟尘折算
    public String so2;           //二氧化硫"
    public String so2ZheSuan;    //二氧化硫折算
    public String dan;            //氮氧化物
    public String danZheSuan;     //氮氧化物折算


}
