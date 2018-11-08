package com.ruiao.tools.ic_card2;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by ruiao on 2018/5/26.
 */

public class FactroyBean implements IPickerViewData {
    public String getFacname() {
        return facname;
    }

    public void setFacname(String facname) {
        this.facname = facname;
    }

    public FactroyBean(String facname) {

        this.facname = facname;
    }

    private String facname;
    @Override
    public String getPickerViewText() {
        return facname;
    }
}
