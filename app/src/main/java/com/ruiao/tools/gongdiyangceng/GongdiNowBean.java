package com.ruiao.tools.gongdiyangceng;

import android.widget.TextView;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.io.Serializable;
@SmartTable(name = "数据")
public class GongdiNowBean implements Serializable {
    @SmartColumn(id = 0, name = "刷新时间", fixed = true)
    public String time;
    @SmartColumn(id = 1, name = "企业")
    public String qiye;
    @SmartColumn(id = 2, name = "监测点")
    public String name;
    @SmartColumn(id = 3, name = "PM10")
    public String pm10;
    @SmartColumn(id = 4, name = "Pm2.5")
    public String pm25;
    @SmartColumn(id = 5, name = "噪声")
    public String zaosheng;
    @SmartColumn(id = 6, name = "温度")
    public String wendu;
    @SmartColumn(id = 7, name = "湿度")
    public String shidu;
    @SmartColumn(id = 8, name = "风速")
    public String fengsu;
    @SmartColumn(id = 9, name = "风向")
    public String fengxiang;

}
