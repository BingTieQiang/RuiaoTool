package com.ruiao.tools.gongkuang;

import java.util.ArrayList;

/**
 * Created by ruiao on 2019/2/28.
 */

public class ShishigongkuangBean {
    public int zhuangtai; //0 全部企业 1 违规生产 2合法生产
    public String id;
    public String qiye;
    public String jiance;
    public String shengchan;
    public String wangluo;
    public String shengchansheshi;
    public String zhilisheshi;
    public String shijian;
    public String zuixin;
    public String yingjicuoshi;  //应急措施

    public String a1;
    public String a2;
    public String a3;
    public String a4;
    public String a5;
    public String xingzhengquyu;
    public String hangye;
    public String qilu;
    public String lasi;
    public String yujing;



    public  ArrayList<canshu> lists ;

    public static class canshu{
        public canshu(String name, String value,String zhuangtai) {
            this.name = name;
            this.value = value;
            this.zhuangtai = zhuangtai;
        }

        public String name;
        public String value;
        public String zhuangtai;
    }
}
