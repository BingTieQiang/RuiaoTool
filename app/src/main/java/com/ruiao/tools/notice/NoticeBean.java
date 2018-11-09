package com.ruiao.tools.notice;

import java.io.Serializable;

/**
 * Created by ruiao on 2018/5/4.
 */

public class NoticeBean implements Serializable {
    public NoticeBean(String title, String msg,String time) {
        this.title = title;
        this.msg = msg;
        this.time = time;
    }
    public NoticeBean(String title, String msg) {
        this.title = title;
        this.msg = msg;

    }

    public String title;
    public String msg;
    public String time;
    public String com;
    public String tvChaobaio;
    public String tvStand;
}
