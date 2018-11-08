package com.ruiao.tools.ic_card2;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by ruiao on 2018/5/26.
 */

public class DevBean implements IPickerViewData {
    public DevBean(String devNane, String id) {
        this.devNane = devNane;
        this.id = id;
    }

    public String getDevNane() {
        return devNane;
    }

    public void setDevNane(String devNane) {
        this.devNane = devNane;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String devNane;
    private String id;

    @Override
    public String getPickerViewText() {
        return devNane;
    }
}
