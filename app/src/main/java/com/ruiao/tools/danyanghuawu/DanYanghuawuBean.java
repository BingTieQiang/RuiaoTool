package com.ruiao.tools.danyanghuawu;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "表")
public class DanYanghuawuBean {
    @SmartColumn(id = 0, name = "时间", fixed = true)
    public String time;
    @SmartColumn(id = 1, name = "氮氧化物")
    public String danyang;
    @SmartColumn(id = 2, name = "氧气含量")
    public String o2;
    @SmartColumn(id = 3, name = "折算氮氧化物")
    public String zhesuandangyang;
    @SmartColumn(id = 4, name = "二氧化氮")
    public String no2;
    @SmartColumn(id = 5, name = "一氧化氮")
    public String no;

}
