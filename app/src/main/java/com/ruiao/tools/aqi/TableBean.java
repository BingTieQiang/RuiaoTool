package com.ruiao.tools.aqi;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "销售计划表")
public class TableBean {
    @SmartColumn(id = 0, name = "时间", fixed = true)
    public String time;
    @SmartColumn(id = 1, name = "AQI")
    public String aqi;
    @SmartColumn(id = 2, name = "PM2.5")
    public String pm25;
    @SmartColumn(id = 3, name = "pm10")
    public String pm10;
    @SmartColumn(id = 4, name = "一氧化碳")
    public String co;
    @SmartColumn(id = 5, name = "二氧化氮")
    public String no2;
    @SmartColumn(id = 6, name = "二氧化硫")
    public String so2;
    @SmartColumn(id = 7, name = "臭氧")
    public String o3;
    @SmartColumn(id = 8, name = "风速")
    public String fengsu;
    @SmartColumn(id = 9, name = "风向")
    public String fengxiang;
    @SmartColumn(id = 10, name = "气温")
    public String wendu;
    @SmartColumn(id = 11, name = "气压")
    public String qiya;
    @SmartColumn(id = 12, name = "湿度")
    public String shidu;
}
