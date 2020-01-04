package com.ruiao.tools.dongtaiguankong.histroy;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.io.Serializable;
@SmartTable(name = "")
class QiBean implements Serializable {
    @SmartColumn(id = 0, name = "时间", fixed = true)
    public String time;
    @SmartColumn(id = 1, name = "废气")
    public String feiqi;
    @SmartColumn(id = 2, name = "烟尘")
    public String yanceng;
    @SmartColumn(id = 3, name = "烟尘折算")
    public String yanchengzhesuan;
    @SmartColumn(id = 4, name = "二氧化硫")
    public String so2;
    @SmartColumn(id = 5, name = "二氧化硫折算")
    public String so2zhesuan;
    @SmartColumn(id = 6, name = "氮氧化物")
    public String danyang;
    @SmartColumn(id = 7, name = "氮氧化物折算")
    public String danyangzhesuan;
    @SmartColumn(id = 8, name = "氧气含量")
    public String o2;
    @SmartColumn(id = 9, name = "烟气流速")
    public String yanqiliusu;
    @SmartColumn(id = 10, name = "烟气温度")
    public String yanqiwendu;
    @SmartColumn(id = 11, name = "烟气压力")
    public String yanqiyali;

    /*
    JSONArray arr_fiqi = response.getJSONArray("废气");
                            JSONArray arr_yancen = response.getJSONArray("烟尘");
                            JSONArray arr_yanzhenzesuan = response.getJSONArray("烟尘折算");
                            JSONArray arr_so2 = response.getJSONArray("二氧化硫");
                            JSONArray arr_so2zhwesuan = response.getJSONArray("二氧化硫折算");
                            JSONArray arr_danyang = response.getJSONArray("氮氧化物");
                            JSONArray arr_danyangzhesuan = response.getJSONArray("氮氧化物折算");
                            JSONArray arr_o2 = response.getJSONArray("氧气含量");
                            JSONArray arr_yanqiliusu = response.getJSONArray("烟气流速");
                            JSONArray arr_yanqiwendu = response.getJSONArray("烟气温度");
                            JSONArray arr_yanqiyali = response.getJSONArray("烟气压力");
                            JSONArray time = response.getJSONArray("time");
     */

}
