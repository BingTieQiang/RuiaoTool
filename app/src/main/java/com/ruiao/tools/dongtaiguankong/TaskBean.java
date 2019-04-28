package com.ruiao.tools.dongtaiguankong;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ruiao on 2018/9/25.
 */

public class TaskBean implements Serializable {
    public String time;
    public String id;
    public String status;
    public String people;
    public String address;
    public String car;
    public String context;
    public  ArrayList<String> tips = new ArrayList<>();


}
