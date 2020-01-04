package com.ruiao.tools.fenbiao;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "")
public class FenbiaoBean {
    @SmartColumn(id = 0, name = "时间", fixed = true)
    public String time;
    @SmartColumn(id = 1, name = "A相电压")
    public String av;
    @SmartColumn(id = 2, name = "B相电压")
    public String bv;
    @SmartColumn(id = 3, name = "C相电压")
    public String cv;
    @SmartColumn(id = 4, name = "A相电流")
    public String ai;
    @SmartColumn(id = 5, name = "B相电流")
    public String bi;
    @SmartColumn(id = 6, name = "C相电流")
    public String ci;
    @SmartColumn(id = 7, name = "正向有功电能")
    public String dianneg;
    @SmartColumn(id = 8, name = "A相有功功率")
    public String aw;
    @SmartColumn(id = 9, name = "B相有功功率")
    public String bw;
    @SmartColumn(id = 10, name = "C相有功功率")
    public String cw;
    @SmartColumn(id = 11, name = "总功率因数")
    public String gonglv;
}
