package com.ruiao.tools.dongtaiguankong;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "")
public class Ditubean {
    @SmartColumn(id = 0, name = "名称", fixed = true)
    public String arr;
    @SmartColumn(id = 1, name = "内容")
    public String context;

}
