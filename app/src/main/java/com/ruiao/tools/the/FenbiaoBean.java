package com.ruiao.tools.the;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.io.Serializable;

@SmartTable(name = "数据")
public class FenbiaoBean implements Serializable {
    @SmartColumn(id = 0, name = "时间", fixed = true)
    public String time;
    @SmartColumn(id = 1, name = "A相电压")
    public String Ua;
    @SmartColumn(id = 2, name = "B相电压")
    public String Ub;
    @SmartColumn(id = 3, name = "C相电压")
    public String Uc;
    @SmartColumn(id = 4, name = "A相电流")
    public String Ia;
    @SmartColumn(id = 5, name = "B相电流")
    public String Ib;
    @SmartColumn(id = 6, name = "C相电流")
    public String Ic;
    @SmartColumn(id = 7, name = "正向有功电能(3/4)")
    public String Dianneng4;
    @SmartColumn(id = 8, name = "正向有功电能(3/3)")
    public String Dianneng3;
    @SmartColumn(id = 9, name = "A相有功功率")
    public String Pa;
    @SmartColumn(id = 10, name = "B相有功功率")
    public String Pb;
    @SmartColumn(id = 11, name = "C相有功功率")
    public String Pc;
    @SmartColumn(id = 12, name = "总功率因数")
    public String Pzong;
/*

 "time":"2019-09-21 09:32:53",
         "A相电压":"230.89",
         "B相电压":"229.5",
         "C相电压":"230.04",
         "A相电流":"4.172",
         "B相电流":"4.068",
         "C相电流":"3.812",
         "正向有功电能(3/4)":"22.8",
         "正向有功电能(3/3)":"1.14",
         "A相有功功率":"764",
         "B相有功功率":"720",
         "C相有功功率":"640",
         "总功率因数":"0.775"
*/

}
