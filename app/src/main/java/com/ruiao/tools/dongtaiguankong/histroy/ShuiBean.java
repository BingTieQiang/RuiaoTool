package com.ruiao.tools.dongtaiguankong.histroy;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.io.Serializable;
@SmartTable(name = "")
class ShuiBean implements Serializable {
    @SmartColumn(id = 0, name = "时间", fixed = true)
    public String time;
    @SmartColumn(id = 1, name = "污水")
    public String wushui;
    @SmartColumn(id = 2, name = "化学需氧量")
    public String xuyangliang;
    @SmartColumn(id = 3, name = "氨氮")
    public String andan;
    @SmartColumn(id = 4, name = "总氮")
    public String zondan;
    @SmartColumn(id = 5, name = "pH")
    public String ph;





}
