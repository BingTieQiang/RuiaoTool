package com.ruiao.tools.youyan;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.io.Serializable;
@SmartTable(name = "数据")
public class YouyanBean implements Serializable {
    @SmartColumn(id = 0, name = "刷新时间", fixed = true)
    public String time;
    @SmartColumn(id = 1, name = "油烟浓度")
    public String nongdu;
    @SmartColumn(id = 2, name = "净化器状态")
    public String jinghuaqi;
    @SmartColumn(id = 3, name = "风机状态")
    public String fengji;



}
